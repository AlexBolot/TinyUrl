package fr.unice.polytech.log.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEntry implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    private String content;
    private Level level;

    public LogEntry(@JsonProperty("content") String content, @JsonProperty("level") Level level) {
        this.content = content;
        this.level = level;
    }

    public enum Level {
        DEBUG,
        INFO,
        WARNING,
        ERROR
    }
}
