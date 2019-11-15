package fr.unice.polytech.tinypoly.entities;


import fr.unice.polytech.tinypoly.dto.HttpReply;
import fr.unice.polytech.tinypoly.dto.PtitURequest.RequestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogEntry {
    private String ptitu;
    private RequestType type;
    private String author;
    private String accessIP;
    private long timestamp;
    private HttpReply.Status status;

    @Override
    public String toString() {
        return '{' +
                "\"ptitu\":\"" + ptitu + "\"," +
                "\"status\":" + status +
                '}';
    }
}
