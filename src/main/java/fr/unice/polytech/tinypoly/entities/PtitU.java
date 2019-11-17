package fr.unice.polytech.tinypoly.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class PtitU {

    @Id
    private long hash;

    private String url;

    private String ptitu;

    private String email;

}
