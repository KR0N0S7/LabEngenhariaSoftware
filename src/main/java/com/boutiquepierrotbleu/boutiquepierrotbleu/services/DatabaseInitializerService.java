package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

@Service
public class DatabaseInitializerService {

    // @Autowired
    // private JdbcTemplate jdbcTemplate;

    // @PostConstruct
    // public void initDatabase() {
    //     // Check if data is already present in the database
    //     String checkDataQuery = "SELECT COUNT(*) FROM your_table WHERE column1 = 'value1'";
    //     Integer count = jdbcTemplate.queryForObject(checkDataQuery, Integer.class);

    //     // If data is not present, execute the SQL script
    //     if (count == 0) {
    //         executeSqlScript();
    //     }
    // }

    // private void executeSqlScript() {
    //     // Path to your SQL script file
    //     String scriptFilePath = "classpath:data.sql";
        
    //     try {
    //         // Read the script file content
    //         Resource resource = new ClassPathResource(scriptFilePath);
    //         String scriptContent = new String(Files.readAllBytes(resource.getFile().toPath()));

    //         // Execute the script content
    //         jdbcTemplate.execute(scriptContent);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         // Handle the exception (you might want to log the error)
    //     }
    // }
}

