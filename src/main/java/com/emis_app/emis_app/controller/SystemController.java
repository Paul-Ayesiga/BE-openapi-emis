package com.emis_app.emis_app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/system")
@Tag(name = "System", description = "System health and information endpoints")
public class SystemController {

    @GetMapping("/health")
    @Operation(
            summary = "System health check",
            description = "Returns the current health status of the application"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "System is healthy"),
            @ApiResponse(responseCode = "500", description = "System is experiencing issues")
    })
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "EMIS Application is running successfully");
        response.put("timestamp", LocalDateTime.now());
        response.put("version", "1.0.0");
        response.put("java_version", System.getProperty("java.version"));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    @Operation(summary = "Application information")
    public ResponseEntity<Map<String, Object>> getApplicationInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("application", "Education Management Information System");
        info.put("version", "1.0.0");
        info.put("description", "API for managing schools and learners");
        info.put("spring_boot_version", "3.5.3");
        info.put("java_version", "21");
        info.put("build_time", LocalDateTime.now());
        return ResponseEntity.ok(info);
    }
}