package com.emis_app.emis_app.controller;


import com.emis_app.emis_app.dto.ApiResponse;
import com.emis_app.emis_app.dto.PagedResponse;
import com.emis_app.emis_app.dto.SchoolDTO;
import com.emis_app.emis_app.service.SchoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/schools")
@RequiredArgsConstructor
@Tag(name = "Schools", description = "School management operations")
public class SchoolController {

    private final SchoolService schoolService;

    // CRUD Operations
    @PostMapping
    @Operation(summary = "Create a new school", description = "Creates a new school with the provided information")
    public ResponseEntity<ApiResponse<SchoolDTO>> createSchool(@Valid @RequestBody SchoolDTO schoolDTO) {
        try {
            SchoolDTO createdSchool = schoolService.createSchool(schoolDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("School created successfully", createdSchool));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get school by ID", description = "Retrieves a school by its unique identifier")
    public ResponseEntity<ApiResponse<SchoolDTO>> getSchoolById(@PathVariable Long id) {
        Optional<SchoolDTO> school = schoolService.getSchoolById(id);
        return school.map(schoolDTO -> ResponseEntity.ok(ApiResponse.success(schoolDTO))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("School not found with id: " + id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update school", description = "Updates an existing school with new information")
    public ResponseEntity<ApiResponse<SchoolDTO>> updateSchool(@PathVariable Long id,
                                                               @Valid @RequestBody SchoolDTO schoolDTO) {
        try {
            SchoolDTO updatedSchool = schoolService.updateSchool(id, schoolDTO);
            return ResponseEntity.ok(ApiResponse.success("School updated successfully", updatedSchool));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete school", description = "Deletes a school by its ID")
    public ResponseEntity<ApiResponse<Void>> deleteSchool(@PathVariable Long id) {
        try {
            schoolService.deleteSchool(id);
            return ResponseEntity.ok(ApiResponse.success("School deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // Search Operations
    @GetMapping
    @Operation(summary = "Get all schools", description = "Retrieves all schools with pagination and sorting")
    public ResponseEntity<ApiResponse<PagedResponse<SchoolDTO>>> getAllSchools(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        PagedResponse<SchoolDTO> schools = schoolService.getAllSchools(page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(schools));
    }

    @GetMapping("/search/name")
    @Operation(summary = "Search schools by name", description = "Searches schools by name with partial matching")
    public ResponseEntity<ApiResponse<PagedResponse<SchoolDTO>>> searchSchoolsByName(
            @Parameter(description = "School name to search") @RequestParam(required = false) String name,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        PagedResponse<SchoolDTO> schools = schoolService.searchSchoolsByName(name, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(schools));
    }

    @GetMapping("/search/type")
    @Operation(summary = "Search schools by type", description = "Searches schools by school type")
    public ResponseEntity<ApiResponse<PagedResponse<SchoolDTO>>> searchSchoolsByType(
            @Parameter(description = "School type") @RequestParam String schoolType,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        PagedResponse<SchoolDTO> schools = schoolService.searchSchoolsByType(schoolType, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(schools));
    }

    @GetMapping("/search/location")
    @Operation(summary = "Search schools by location", description = "Searches schools by location with partial matching")
    public ResponseEntity<ApiResponse<PagedResponse<SchoolDTO>>> searchSchoolsByLocation(
            @Parameter(description = "Location to search") @RequestParam String location,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        PagedResponse<SchoolDTO> schools = schoolService.searchSchoolsByLocation(location, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(schools));
    }

    @GetMapping("/search/capacity")
    @Operation(summary = "Search schools by capacity range", description = "Searches schools within enrollment capacity range")
    public ResponseEntity<ApiResponse<PagedResponse<SchoolDTO>>> searchSchoolsByCapacityRange(
            @Parameter(description = "Minimum capacity") @RequestParam Integer minCapacity,
            @Parameter(description = "Maximum capacity") @RequestParam Integer maxCapacity,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        PagedResponse<SchoolDTO> schools = schoolService.searchSchoolsByCapacityRange(
                minCapacity, maxCapacity, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(schools));
    }

    @GetMapping("/search/advanced")
    @Operation(summary = "Advanced school search", description = "Searches schools using multiple criteria")
    public ResponseEntity<ApiResponse<PagedResponse<SchoolDTO>>> advancedSearch(
            @Parameter(description = "School name") @RequestParam(required = false) String name,
            @Parameter(description = "School type") @RequestParam(required = false) String schoolType,
            @Parameter(description = "Location") @RequestParam(required = false) String location,
            @Parameter(description = "Minimum capacity") @RequestParam(required = false) Integer minCapacity,
            @Parameter(description = "Maximum capacity") @RequestParam(required = false) Integer maxCapacity,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        PagedResponse<SchoolDTO> schools = schoolService.advancedSearch(
                name, schoolType, location, minCapacity, maxCapacity, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(schools));
    }

    @GetMapping("/search/available-capacity")
    @Operation(summary = "Get schools with available capacity", description = "Retrieves schools that have available enrollment capacity")
    public ResponseEntity<ApiResponse<PagedResponse<SchoolDTO>>> getSchoolsWithAvailableCapacity(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        PagedResponse<SchoolDTO> schools = schoolService.getSchoolsWithAvailableCapacity(page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(schools));
    }
}