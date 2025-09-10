package com.emis_app.emis_app.dto;


import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDTO {

    private Long id;

    @NotBlank(message = "School name is required")
    @Size(max = 200, message = "School name must not exceed 200 characters")
    private String name;

    @JsonProperty("school_type")
    @NotBlank(message = "School type is required")
    @Size(max = 50, message = "School type must not exceed 50 characters")
    private String schoolType;

    @NotBlank(message = "Location is required")
    @Size(max = 300, message = "Location must not exceed 300 characters")
    private String location;

    @JsonProperty("enrollment_capacity")
    @NotNull(message = "Enrollment capacity is required")
    @Min(value = 1, message = "Enrollment capacity must be at least 1")
    private Integer enrollmentCapacity;

    @JsonProperty("learners_count")
    private Long learnersCount;

    private List<LearnerDTO> learners;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}