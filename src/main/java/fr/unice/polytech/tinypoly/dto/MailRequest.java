package fr.unice.polytech.tinypoly.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class MailRequest {

    private String destination;

    private String msg;

    private String subject;

}
