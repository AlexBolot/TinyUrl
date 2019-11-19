package fr.unice.polytech.tinypoly.controller;

import com.google.cloud.storage.*;
import com.google.cloud.tasks.v2.AppEngineHttpRequest;
import com.google.cloud.tasks.v2.CloudTasksClient;
import com.google.cloud.tasks.v2.QueueName;
import com.google.cloud.tasks.v2.Task;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import com.googlecode.objectify.ObjectifyService;
import fr.unice.polytech.tinypoly.dto.HttpReply;
import fr.unice.polytech.tinypoly.entities.Image;
import fr.unice.polytech.tinypoly.entities.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Clock;
import java.time.Instant;

import static com.google.cloud.tasks.v2.HttpMethod.POST;
import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/image")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private RestTemplate restTemplate;

    private Bucket bucket;

    private Storage storage;

    public ImageController() {
        storage = StorageOptions.getDefaultInstance().getService();
        this.bucket = storage.get("tiny_images");
        if (this.bucket == null) {
            this.bucket = storage.create(BucketInfo.of("tiny_images"));
            logger.info("Bucket created");
        } else {
            logger.info("Bucket retrieve");
        }
    }

    @GetMapping(value = "/{hash}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestHeader(name = "Host") final String host, @PathVariable long hash, HttpServletRequest request) {
        logger.info("Get image");

        Blob blob = bucket.get(String.valueOf(hash));

        if (blob != null) {
            Image image = ObjectifyService.run(() -> ofy().load().type(Image.class).id(hash).now());
            LogEntry logEntry = new LogEntry(String.valueOf(hash), image.getEmail(), getClientIp(request), System.currentTimeMillis(), LogEntry.Type.IMAGE);
            restTemplate.postForObject("http://" + host + "/logs/add", logEntry, Void.class);
            return blob.getContent();
        } else {
            throw new ResponseStatusException(NOT_FOUND, "The picture you're looking for doesn't exist or have been removed.");
        }
    }

    @PostMapping(value = "/delete")
    public void deleteImage(@RequestBody String hash) {
        BlobId blobId = BlobId.of(bucket.getName(), hash);
        boolean deleted = storage.delete(blobId);
        if (deleted) logger.info("Image deleted");
        else logger.warn("Image not deleted");
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String createImage(@RequestHeader(name = "Host") final String host, @RequestParam(value = "File") MultipartFile image, @RequestParam(value = "Email") String email) throws IOException {
        if (restTemplate.postForObject("http://" + host + "/checkid/account", email, HttpReply.class).getStatus() != HttpReply.Status.SUCCESS)
            throw new ResponseStatusException(UNAUTHORIZED, "You need an account to create short url.");

        if (image.getSize() > 4000000) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "Image too big, maximum size of 4Mb, request image size : " + image.getSize());
        }

        long hash = image.hashCode();
        String url = host + "/image/" + hash;

        bucket.create(String.valueOf(hash), image.getBytes());

        logger.info("Creating image");

        // Instantiates a client.
        try (CloudTasksClient client = CloudTasksClient.create()) {

            // Variables provided by the CLI.
            String projectId = "tinypoly-257609";
            String queueName = "queue-delete-image";
            String location = "europe-west1";
            String payload = String.valueOf(hash);

            // Construct the fully qualified queue name.
            String queuePath = QueueName.of(projectId, location, queueName).toString();

            // Construct the task body.
            Task.Builder taskBuilder = Task
                    .newBuilder()
                    .setAppEngineHttpRequest(AppEngineHttpRequest.newBuilder()
                            .setBody(ByteString.copyFrom(payload, Charset.defaultCharset()))
                            .setRelativeUri("/image/delete")
                            .setHttpMethod(POST)
                            .build());

            int seconds = 60 * 5;
            taskBuilder.setScheduleTime(Timestamp
                    .newBuilder()
                    .setSeconds(Instant.now(Clock.systemUTC()).plusSeconds(seconds).getEpochSecond()));

            // Send create task request.
            Task task = client.createTask(queuePath, taskBuilder.build());
            logger.info("Task created: " + task.getName());
        }

        Image entity = new Image(hash, image.getOriginalFilename(), email, 0);

        ObjectifyService.run(() -> {
            ofy().save().entities(entity).now();
            return entity;
        });

        logger.info("Image created");
        return url;
    }

    private String getClientIp(HttpServletRequest request) {

        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }

}
