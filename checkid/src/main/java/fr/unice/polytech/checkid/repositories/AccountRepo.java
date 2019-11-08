package fr.unice.polytech.checkid.repositories;

import fr.unice.polytech.checkid.entities.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepo extends CrudRepository<Account, Long> {
    default void add(long id) {
        save(new Account(id));
    }
}
