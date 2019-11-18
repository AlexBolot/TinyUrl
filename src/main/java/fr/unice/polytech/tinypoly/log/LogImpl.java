package fr.unice.polytech.tinypoly.log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.ThreadManager;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import fr.unice.polytech.tinypoly.dto.HttpReply;
import fr.unice.polytech.tinypoly.entities.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static fr.unice.polytech.tinypoly.dto.HttpReply.Status.FAIL;
import static fr.unice.polytech.tinypoly.dto.HttpReply.Status.SUCCESS;

public class LogImpl implements Log {

    private static final Logger logger = LoggerFactory.getLogger(LogImpl.class);

    private final ObjectMapper mapper = new ObjectMapper();

    public LogImpl() {
        File directory = new File(this.getClass().getClassLoader().getResource(".").getFile() + "/logs");

        if (!directory.exists()) {
            if (directory.mkdirs()) {
                logger.info("Directory logs was successfully created");
            } else {
                logger.warn("Directory logs was not created");
            }
        }
    }

    @Override
    public String sendTask() throws IOException {
        int leaseAmount = 1;
        int leaseTime = 3600;
        TimeUnit timeUnit = TimeUnit.SECONDS;

        AtomicReference<HttpReply> reply = new AtomicReference<>();

        ThreadManager.backgroundThreadFactory().newThread(() -> {
            Queue queue = QueueFactory.getQueue("generate-logs");
            List<TaskHandle> tasks = queue.leaseTasks(leaseTime, timeUnit, leaseAmount);
            try {
                reply.set(processTasks(tasks, queue));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return mapper.writeValueAsString(reply.get());
    }

    //Method to process and delete tasks
    private HttpReply processTasks(List<TaskHandle> tasks, Queue queue) throws IOException {
        for (TaskHandle task : tasks) {
            String payload = new String(task.getPayload());

            JsonNode jsonNode = mapper.readValue(payload, JsonNode.class);
            String ptitu = jsonNode.get("ptitu").asText();

            try {
                long count = readLogs(ptitu).size();

                queue.deleteTask(task);

                return new HttpReply(SUCCESS, Long.toString(count));
            } catch (Exception e) {
                e.printStackTrace();
                return new HttpReply(FAIL, e.getMessage());
            }

        }

        return new HttpReply(FAIL, "No task to process !");
    }

    private List<LogEntry> readLogs(String ptitu) throws IOException {
        File file = new File(this.getClass().getClassLoader().getResource("./logs").getFile() + "/" + ptitu + ".txt");

        List<String> lines = Files.readAllLines(file.toPath());
        List<LogEntry> entries = new ArrayList<>();

        for (String line : lines) {
            entries.add(mapper.readValue(line, LogEntry.class));
        }

        return entries;
    }

}
