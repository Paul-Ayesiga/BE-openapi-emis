package com.emis_app.emis_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "learners")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Learner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Learner name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Column(nullable = false, length = 10)
    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female, or Other")
    private String gender;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "Grade is required")
    @Size(max = 20, message = "Grade must not exceed 20 characters")
    private String grade;

    @Column(name = "academic_year", nullable = false, length = 20)
    @NotBlank(message = "Academic year is required")
    @Pattern(regexp = "^\\d{4}-\\d{4}$", message = "Academic year must be in format YYYY-YYYY")
    private String academicYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    @JsonBackReference
    private School school;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}