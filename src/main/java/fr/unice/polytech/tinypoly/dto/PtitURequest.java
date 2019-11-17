package fr.unice.polytech.tinypoly.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PtitURequest {

    private String email;

    private String url;

}
