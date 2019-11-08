package fr.unice.polytech.checkid.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    private long id;
}
