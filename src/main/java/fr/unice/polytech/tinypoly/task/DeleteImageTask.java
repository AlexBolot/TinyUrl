package fr.unice.polytech.tinypoly.task;

import com.google.appengine.api.taskqueue.DeferredTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteImageTask implements DeferredTask {

    private static final Logger logger = LoggerFactory.getLogger(DeleteImageTask.class);

    public static final int DELAY_MS = 5000 * 60;

    @Override
    public void run() {
        logger.info("Image Deleted");
    }
}
