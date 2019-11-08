package fr.unice.polytech.log.repositories;

import fr.unice.polytech.log.entities.LogEntry;
import org.springframework.data.repository.CrudRepository;

public interface LogRepo extends CrudRepository<LogEntry, Long> {
    default void add(long id, String message, LogEntry.Level level) {
        save(new LogEntry(id, message, level));
    }
}
