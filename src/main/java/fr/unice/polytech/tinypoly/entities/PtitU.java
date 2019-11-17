package fr.unice.polytech.tinypoly.entities;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PtitU {

    private long hash;

    private String url;

    private String ptitu;

    private String email;

    public PtitU(String url, String ptitu, String email) {
        this.url = url;
        this.ptitu = ptitu;
        this.email = email;
    }

}
