package fr.unice.polytech.tinypoly.entities;


import fr.unice.polytech.tinypoly.dto.HttpReply;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogEntry {
    private String ptitu;
    private String author;
    private String accessIP;
    private long timestamp;
    private Type type;

    public enum Type {
        IMAGE,
        PTITU
    }

    @Override
    public String toString() {
        return '{' +
                "\"ptitu\":\"" + ptitu + "\"," +
                "\"type\":" + type +
                '}';
    }
}
