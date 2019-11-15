package fr.unice.polytech.tinypoly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class HttpReply {
    private Status status;
    private String message;

    public enum Status {
        SUCCESS,
        FAIL,
        ERROR
    }
}
