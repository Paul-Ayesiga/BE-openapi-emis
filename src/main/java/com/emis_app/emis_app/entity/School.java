package com.emis_app.emis_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "schools")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "School name is required")
    @Size(max = 200, message = "School name must not exceed 200 characters")
    private String name;

    @Column(name = "school_type", nullable = false, length = 50)
    @NotBlank(message = "School type is required")
    @Size(max = 50, message = "School type must not exceed 50 characters")
    private String schoolType;

    @Column(nullable = false, length = 300)
    @NotBlank(message = "Location is required")
    @Size(max = 300, message = "Location must not exceed 300 characters")
    private String location;

    @Column(name = "enrollment_capacity", nullable = false)
    @NotNull(message = "Enrollment capacity is required")
    @Min(value = 1, message = "Enrollment capacity must be at least 1")
    private Integer enrollmentCapacity;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Learner> learners;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
