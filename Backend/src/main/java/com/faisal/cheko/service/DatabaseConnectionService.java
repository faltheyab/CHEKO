package com.faisal.cheko.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;


@Service
public class DatabaseConnectionService {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionService.class);
    
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final RetryTemplate retryTemplate;
    private final AtomicBoolean isConnected = new AtomicBoolean(false);

    @Autowired
    public DatabaseConnectionService(DataSource dataSource, JdbcTemplate jdbcTemplate, RetryTemplate retryTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        this.retryTemplate = retryTemplate;
        
        // Check initial connection
        checkConnection();
    }


    public boolean isConnected() {
        return isConnected.get();
    }

    @Retryable(value = {SQLException.class, DataAccessException.class},
               maxAttempts = 10)
    public boolean checkConnection() {
        try {
            // Try to get a connection from the data source
            try (Connection connection = dataSource.getConnection()) {
                if (connection.isValid(5)) {  // 5 second timeout
                    if (!isConnected.getAndSet(true)) {
                        log.info("Successfully connected to the database");
                    }
                    return true;
                }
            }
            
            // If we get here, the connection is not valid
            connectionFailed(null);
            return false;
            
        } catch (SQLException | DataAccessException e) {
            connectionFailed(e);
            return false;
        }
    }

    public <T> T executeWithRetry(RetryCallback<T, Exception> operation) throws Exception {
        return retryTemplate.execute(operation);
    }

    @Retryable(value = {SQLException.class, DataAccessException.class},
               maxAttempts = 10)
    public boolean pingDatabase() {
        try {
            // Execute a simple query to check if the database is available
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            
            if (result != null && result == 1) {
                if (!isConnected.getAndSet(true)) {
                    log.info("Successfully connected to the database");
                }
                return true;
            } else {
                connectionFailed(null);
                return false;
            }
        } catch (DataAccessException e) {
            connectionFailed(e);
            return false;
        }
    }

    private void connectionFailed(Exception e) {
        if (isConnected.getAndSet(false)) {
            if (e != null) {
                log.error("Database connection lost: {}", e.getMessage(), e);
            } else {
                log.error("Database connection lost");
            }
        } else {
            if (e != null) {
                log.warn("Failed to connect to database: {}", e.getMessage());
            } else {
                log.warn("Failed to connect to database");
            }
        }
        
        log.info("Will retry connection in 5 minutes...");
    }
}