package com.emis_app.emis_app.controller;

import com.emis_app.emis_app.dto.ApiResponse;
import com.emis_app.emis_app.dto.LearnerDTO;
import com.emis_app.emis_app.dto.PagedResponse;
import com.emis_app.emis_app.service.LearnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/learners")
@RequiredArgsConstructor
@Tag(name = "Learners", description = "Learner management operations")
public class LearnerController {

    private final LearnerService learnerService;

    // CRUD Operations
    @PostMapping
    @Operation(summary = "Create a new learner", description = "Creates a new learner with the provided information")
    public ResponseEntity<ApiResponse<LearnerDTO>> createLearner(@Valid @RequestBody LearnerDTO learnerDTO) {
        try {
            LearnerDTO createdLearner = learnerService.createLearner(learnerDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Learner created successfully", createdLearner));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get learner by ID", description = "Retrieves a learner by their unique identifier")
    public ResponseEntity<ApiResponse<LearnerDTO>> getLearnerById(@PathVariable Long id) {
        Optional<LearnerDTO> learner = learnerService.getLearnerById(id);
        return learner.map(learnerDTO -> ResponseEntity.ok(ApiResponse.success(learnerDTO))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Learner not found with id: " + id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update learner", description = "Updates an existing learner with new information")
    public ResponseEntity<ApiResponse<LearnerDTO>> updateLearner(@PathVariable Long id,
                                                                 @Valid @RequestBody LearnerDTO learnerDTO) {
        try {
            LearnerDTO updatedLearner = learnerService.updateLearner(id, learnerDTO);
            return ResponseEntity.ok(ApiResponse.success("Learner updated successfully", updatedLearner));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete learner", description = "Deletes a learner by their ID")
    public ResponseEntity<ApiResponse<Void>> deleteLearner(@PathVariable Long id) {
        try {
            learnerService.deleteLearner(id);
            return ResponseEntity.ok(ApiResponse.success("Learner deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // Search Operations
    @GetMapping
    @Operation(summary = "Get all learners", description = "Retrieves all learners with pagination and sorting")
    public ResponseEntity<ApiResponse<PagedResponse<LearnerDTO>>> getAllLearners(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        PagedResponse<LearnerDTO> learners = learnerService.getAllLearners(page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(learners));
    }

    @GetMapping("/search/name")
    @Operation(summary = "Search learners by name", description = "Searches learners by name with partial matching")
    public ResponseEntity<ApiResponse<PagedResponse<LearnerDTO>>> searchLearnersByName(
            @Parameter(description = "Learner name to search") @RequestParam(required = false) String name,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        PagedResponse<LearnerDTO> learners = learnerService.searchLearnersByName(name, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(learners));
    }

    @GetMapping("/search/gender")
    @Operation(summary = "Search learners by gender", description = "Searches learners by gender")
    public ResponseEntity<ApiResponse<PagedResponse<LearnerDTO>>> searchLearnersByGender(
            @Parameter(description = "Gender (Male, Female, Other)") @RequestParam(required = false) String gender,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        PagedResponse<LearnerDTO> learners = learnerService.searchLearnersByGender(gender, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(learners));
    }

    @GetMapping("/search/grade")
    @Operation(summary = "Search learners by grade", description = "Searches learners by grade level")
    public ResponseEntity<ApiResponse<PagedResponse<LearnerDTO>>> searchLearnersByGrade(
            @Parameter(description = "Grade level") @RequestParam String grade,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        PagedResponse<LearnerDTO> learners = learnerService.searchLearnersByGrade(grade, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(learners));
    }

    @GetMapping("/search/academic-year")
    @Operation(summary = "Search learners by academic year", description = "Searches learners by academic year")
    public ResponseEntity<ApiResponse<PagedResponse<LearnerDTO>>> searchLearnersByAcademicYear(
            @Parameter(description = "Academic year (YYYY-YYYY format)") @RequestParam String academicYear,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        PagedResponse<LearnerDTO> learners = learnerService.searchLearnersByAcademicYear(academicYear, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(learners));
    }

    @GetMapping("/search/school")
    @Operation(summary = "Search learners by school", description = "Searches learners by school ID")
    public ResponseEntity<ApiResponse<PagedResponse<LearnerDTO>>> searchLearnersBySchool(
            @Parameter(description = "School ID") @RequestParam Long schoolId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        PagedResponse<LearnerDTO> learners = learnerService.searchLearnersBySchool(schoolId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(learners));
    }

    @GetMapping("/search/advanced")
    @Operation(summary = "Advanced learner search", description = "Searches learners using multiple criteria")
    public ResponseEntity<ApiResponse<PagedResponse<LearnerDTO>>> advancedSearch(
            @Parameter(description = "Learner name") @RequestParam(required = false) String name,
            @Parameter(description = "Gender") @RequestParam(required = false) String gender,
            @Parameter(description = "Grade") @RequestParam(required = false) String grade,
            @Parameter(description = "Academic year") @RequestParam(required = false) String academicYear,
            @Parameter(description = "School ID") @RequestParam(required = false) Long schoolId,
            @Parameter(description = "School name") @RequestParam(required = false) String schoolName,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        PagedResponse<LearnerDTO> learners = learnerService.advancedSearch(
                name, gender, grade, academicYear, schoolId, schoolName, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(learners));
    }
}