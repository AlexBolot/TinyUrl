package fr.unice.polytech.tinypoly.controller;

import com.google.cloud.storage.*;
import fr.unice.polytech.tinypoly.task.DeleteImageTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import java.io.IOException;

import static fr.unice.polytech.tinypoly.task.DeleteImageTask.DELAY_MS;

@RestController
@RequestMapping("/image")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    private Bucket bucket;

    public ImageController() {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        this.bucket = storage.get("tiny_images");
        if (this.bucket == null) {
            this.bucket = storage.create(BucketInfo.of("tiny_images"));
            logger.info("Bucket created");
        } else {
            logger.info("Bucket retrieve");
        }
    }

    @GetMapping(value = "/{hash}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable long hash) {
        Blob blob = bucket.get(String.valueOf(hash));

        logger.info("Get image");
        return blob.getContent();
    }

    @PostMapping(value = "/create",
            consumes = "multipart/form-data")
    public String createImage(@RequestHeader(name = "Host") final String host,
                              @RequestParam(value = "File") MultipartFile image) throws IOException {
        long hash = image.hashCode();
        String url = host + "/image/" + hash;

        bucket.create(String.valueOf(hash), image.getBytes());

        // Add the task to the default queue.
        Queue queue = QueueFactory.getDefaultQueue();

        // Wait 5 minutes to run for demonstration purposes
        queue.add(
                TaskOptions.Builder.withPayload(new DeleteImageTask())
                        .etaMillis(System.currentTimeMillis() + DELAY_MS));


        logger.info("Creating image");
        return url;
    }

}
