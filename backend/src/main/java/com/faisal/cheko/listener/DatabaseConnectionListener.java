package com.faisal.cheko.listener;

import com.faisal.cheko.service.DatabaseConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Listener for database connection events.
 * Monitors the database connection and attempts to reconnect if it's down.
 */
@Component
public class DatabaseConnectionListener {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionListener.class);
    
    private final DatabaseConnectionService databaseConnectionService;

    @Autowired
    public DatabaseConnectionListener(DatabaseConnectionService databaseConnectionService) {
        this.databaseConnectionService = databaseConnectionService;
    }

    /**
     * Initialize the database connection when the application starts.
     * This will trigger the first connection attempt.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Application is ready, checking database connection...");
        try {
            if (!databaseConnectionService.isConnected()) {
                log.info("Database is disconnected, will attempt reconnection on schedule");
            }
        } catch (Exception e) {
            log.error("Error checking database connection: {}", e.getMessage(), e);
            log.info("Application will continue running and retry database connection later");
        }
    }


    @Scheduled(fixedRate = 300000)  // 5 minutes
    public void checkDatabaseConnection() {
        if (!databaseConnectionService.isConnected()) {
            log.info("Database is disconnected, attempting to reconnect...");
            databaseConnectionService.pingDatabase();
        }
    }

    @Scheduled(fixedRate = 300000)  // 5 minutes
    public void logConnectionStatus() {
        if (databaseConnectionService.isConnected()) {
            log.debug("Database connection is healthy");
        } else {
            log.warn("Database connection is currently down, automatic retry is in progress");
        }
    }
}