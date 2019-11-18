package fr.unice.polytech.tinypoly.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.tinypoly.dto.HttpReply;
import fr.unice.polytech.tinypoly.entities.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static fr.unice.polytech.tinypoly.dto.HttpReply.Status.FAIL;
import static fr.unice.polytech.tinypoly.dto.HttpReply.Status.SUCCESS;

@RestController
@RequestMapping(path = "/logs", produces = "application/json")
public class LogController {

    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    public LogController() {
        try {
            File directory = new File(this.getClass().getClassLoader().getResource(".").getFile() + "/logs");

            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    logger.info("Directory logs was successfully created");
                } else {
                    logger.warn("Directory logs was not created");
                }
            }
        } catch (Exception e) {
            logger.error("Error while creating LogController", e);
        }
    }

    @PostMapping("/add")
    public HttpReply addLogs(@RequestBody String body) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            LogEntry logEntry = mapper.readValue(body, LogEntry.class);
            String jsonEntry = mapper.writeValueAsString(logEntry);

            File file = new File(this.getClass().getClassLoader().getResource("./logs").getFile() + "/" + logEntry.getPtitu() + ".txt");

            if (file.createNewFile()) logger.info("Created file " + file.getPath());

            BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true));
            bw.write(jsonEntry);
            bw.write("\n");
            bw.close();

            return new HttpReply(SUCCESS, logEntry.toString());
        } catch (Exception e) {
            logger.error("Error while adding logs", e);
            return new HttpReply(FAIL, e.getMessage());
        }
    }

    @GetMapping("/accessByPtitu/{ptitu}")
    public HttpReply getLogsById(@PathVariable String ptitu) {
        try {
            List<LogEntry> entries = readLogs(ptitu);
            logger.info("Logs get for ptitU" + ptitu);
            return new HttpReply(SUCCESS, new ObjectMapper().writeValueAsString(entries));
        } catch (Exception e) {
            logger.error("Error while getting logs", e);
            return new HttpReply(FAIL, e.getMessage());
        }
    }

    private List<LogEntry> readLogs(String ptitu) throws IOException {
        File file = new File(this.getClass().getClassLoader().getResource("./logs").getFile() + "/" + ptitu + ".txt");
        ObjectMapper mapper = new ObjectMapper();

        List<String> lines = Files.readAllLines(file.toPath());
        List<LogEntry> entries = new ArrayList<>();

        for (String line : lines) {
            entries.add(mapper.readValue(line, LogEntry.class));
        }

        return entries;
    }

//    @Override
//    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        int leaseAmount = 1;
//        int leaseTime = 3600;
//        TimeUnit timeUnit = TimeUnit.SECONDS;
//
//        Queue queue = QueueFactory.getQueue("generate-logs");
//        List<TaskHandle> tasks = queue.leaseTasks(leaseTime, timeUnit, leaseAmount);
//        HttpReply reply = processTasks(tasks, queue);
//
//        try (PrintWriter writer = resp.getWriter()) {
//            writer.write(mapper.writeValueAsString(reply));
//        }
//    }
//
//    //Method to process and delete tasks
//    private HttpReply processTasks(List<TaskHandle> tasks, Queue queue) throws IOException {
//        for (TaskHandle task : tasks) {
//            String payload = new String(task.getPayload());
//
//            JsonNode jsonNode = mapper.readValue(payload, JsonNode.class);
//            String ptitu = jsonNode.get("ptitu").asText();
//
//            try {
//                long count = readLogs(ptitu).size();
//
//                queue.deleteTask(task);
//
//                return new HttpReply(SUCCESS, Long.toString(count));
//            } catch (Exception e) {
//                e.printStackTrace();
//                return new HttpReply(FAIL, e.getMessage());
//            }
//
//        }
//
//        return new HttpReply(FAIL, "No task to process !");
//    }
}
