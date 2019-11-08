package fr.unice.polytech.tinypoly.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private long id;
    private String email;

    public Account(long id) {
        this.id = id;
    }

    public Account(String email) {
        this.email = email;
    }
}
