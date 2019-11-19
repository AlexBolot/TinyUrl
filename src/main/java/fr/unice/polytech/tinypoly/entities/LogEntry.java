package fr.unice.polytech.tinypoly.entities;


import fr.unice.polytech.tinypoly.dto.HttpReply;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LogEntry {
    private String ptitu;
    private String author;
    private String accessIP;
    private long timestamp;
    private Type type;
    private HttpReply.Status status;

    public enum Type {
        IMAGE,
        PTITU
    }
}
