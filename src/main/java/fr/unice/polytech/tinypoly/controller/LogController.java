//package fr.unice.polytech.tinypoly;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.appengine.api.taskqueue.Queue;
//import com.google.appengine.api.taskqueue.QueueFactory;
//import com.google.appengine.api.taskqueue.TaskHandle;
//import fr.unice.polytech.tinypoly.dto.HttpReply;
//import fr.unice.polytech.tinypoly.entities.LogEntry;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.PostConstruct;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.nio.file.Files;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//import static fr.unice.polytech.tinypoly.dto.HttpReply.Status.FAIL;
//import static fr.unice.polytech.tinypoly.dto.HttpReply.Status.SUCCESS;
//
//@RestController
//@RequestMapping(path = "/logs", produces = "application/json")
//@WebServlet(
//        name = "TaskPull",
//        description = "TaskQueues: Process some queues",
//        urlPatterns = "/taskqueues/queue"
//)
//public class LogController extends HttpServlet {
//
//    @PostConstruct
//    private void postConstruct() throws IOException {
//        File directory = new File(this.getClass().getClassLoader().getResource(".").getFile() + "/logs");
//
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//    }
//
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    @PostMapping("/add")
//    public HttpReply addLogs(@RequestBody String body) {
//        try {
//            LogEntry logEntry = mapper.readValue(body, LogEntry.class);
//            String jsonEntry = mapper.writeValueAsString(logEntry);
//
//            System.out.println("logs/" + logEntry.getPtitu() + ".txt");
//
//            File file = new File(this.getClass().getClassLoader().getResource("./logs").getFile() + "/" + logEntry.getPtitu() + ".txt");
//            if (file.createNewFile()) {
//                System.out.println("File is created!");
//            } else {
//                System.out.println("File already exists.");
//            }
//
//            BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true));
//            bw.write(jsonEntry);
//            bw.write("\n");
//            bw.close();
//
//            return new HttpReply(SUCCESS, logEntry.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new HttpReply(FAIL, e.getMessage());
//        }
//    }
//
//    @GetMapping("/accessByPtitu/{ptitu}")
//    public HttpReply getLogsById(@PathVariable String ptitu) {
//        try {
//            long count = readLogs(ptitu).size();
//            return new HttpReply(SUCCESS, Long.toString(count));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new HttpReply(FAIL, e.getMessage());
//        }
//    }
//
//    private List<LogEntry> readLogs(String ptitu) throws IOException {
//        File file = new File(this.getClass().getClassLoader().getResource("./logs").getFile() + "/" + ptitu + ".txt");
//
//        List<String> lines = Files.readAllLines(file.toPath());
//        List<LogEntry> entries = new ArrayList<>();
//
//        for (String line : lines) {
//            entries.add(mapper.readValue(line, LogEntry.class));
//        }
//
//        return entries;
//    }
//
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
//}
