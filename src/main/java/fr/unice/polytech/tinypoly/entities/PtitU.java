package fr.unice.polytech.tinypoly.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
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

    @Index
    private String email;

    private long compteur;

    public void addCompteur() {
        compteur++;
    }
}
