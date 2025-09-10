package com.emis_app.emis_app.service;


import com.emis_app.emis_app.dto.PagedResponse;
import com.emis_app.emis_app.dto.SchoolDTO;
import com.emis_app.emis_app.entity.School;
import com.emis_app.emis_app.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SchoolService {

    private final SchoolRepository schoolRepository;

    // CRUD Operations
    public SchoolDTO createSchool(SchoolDTO schoolDTO) {
        if (schoolRepository.existsByNameIgnoreCase(schoolDTO.getName())) {
            throw new RuntimeException("School with name '" + schoolDTO.getName() + "' already exists");
        }

        School school = convertToEntity(schoolDTO);
        School savedSchool = schoolRepository.save(school);
        return convertToDTO(savedSchool);
    }

    @Transactional(readOnly = true)
    public Optional<SchoolDTO> getSchoolById(Long id) {
        return schoolRepository.findById(id)
                .map(this::convertToDTO);
    }

    public SchoolDTO updateSchool(Long id, SchoolDTO schoolDTO) {
        School existingSchool = schoolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("School not found with id: " + id));

        // Check if name is being changed and if new name already exists
        if (!existingSchool.getName().equalsIgnoreCase(schoolDTO.getName()) &&
                schoolRepository.existsByNameIgnoreCase(schoolDTO.getName())) {
            throw new RuntimeException("School with name '" + schoolDTO.getName() + "' already exists");
        }

        existingSchool.setName(schoolDTO.getName());
        existingSchool.setSchoolType(schoolDTO.getSchoolType());
        existingSchool.setLocation(schoolDTO.getLocation());
        existingSchool.setEnrollmentCapacity(schoolDTO.getEnrollmentCapacity());

        School updatedSchool = schoolRepository.save(existingSchool);
        return convertToDTO(updatedSchool);
    }

    public void deleteSchool(Long id) {
        if (!schoolRepository.existsById(id)) {
            throw new RuntimeException("School not found with id: " + id);
        }
        schoolRepository.deleteById(id);
    }

    // Search Operations
    @Transactional(readOnly = true)
    public PagedResponse<SchoolDTO> getAllSchools(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<School> schoolPage = schoolRepository.findAll(pageable);
        return convertToPagedResponse(schoolPage);
    }

    @Transactional(readOnly = true)
    public PagedResponse<SchoolDTO> searchSchoolsByName(String name, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<School> schoolPage = schoolRepository.findByNameContainingIgnoreCase(name, pageable);
        return convertToPagedResponse(schoolPage);
    }

    @Transactional(readOnly = true)
    public PagedResponse<SchoolDTO> searchSchoolsByType(String schoolType, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<School> schoolPage = schoolRepository.findBySchoolTypeIgnoreCase(schoolType, pageable);
        return convertToPagedResponse(schoolPage);
    }

    @Transactional(readOnly = true)
    public PagedResponse<SchoolDTO> searchSchoolsByLocation(String location, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<School> schoolPage = schoolRepository.findByLocationContainingIgnoreCase(location, pageable);
        return convertToPagedResponse(schoolPage);
    }

    @Transactional(readOnly = true)
    public PagedResponse<SchoolDTO> searchSchoolsByCapacityRange(Integer minCapacity, Integer maxCapacity,
                                                                 int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<School> schoolPage = schoolRepository.findByEnrollmentCapacityBetween(minCapacity, maxCapacity, pageable);
        return convertToPagedResponse(schoolPage);
    }

    @Transactional(readOnly = true)
    public PagedResponse<SchoolDTO> advancedSearch(String name, String schoolType, String location,
                                                   Integer minCapacity, Integer maxCapacity,
                                                   int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        System.out.println("DEBUG - name: " + name + ", type: " + (name != null ? name.getClass().getName() : "null"));
        System.out.println("DEBUG - schoolType: " + schoolType + ", type: " + (schoolType != null ? schoolType.getClass().getName() : "null"));
        System.out.println("DEBUG - location: " + location + ", type: " + (location != null ? location.getClass().getName() : "null"));

        Page<School> schoolPage = schoolRepository.findBySearchCriteria(
                name, schoolType, location, minCapacity, maxCapacity, pageable);
        return convertToPagedResponse(schoolPage);
    }

    @Transactional(readOnly = true)
    public PagedResponse<SchoolDTO> getSchoolsWithAvailableCapacity(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<School> schoolPage = schoolRepository.findSchoolsWithAvailableCapacity(pageable);
        return convertToPagedResponse(schoolPage);
    }

    // Utility methods
    private School convertToEntity(SchoolDTO dto) {
        School school = new School();
        school.setName(dto.getName());
        school.setSchoolType(dto.getSchoolType());
        school.setLocation(dto.getLocation());
        school.setEnrollmentCapacity(dto.getEnrollmentCapacity());
        return school;
    }

    private SchoolDTO convertToDTO(School school) {
        SchoolDTO dto = new SchoolDTO();
        dto.setId(school.getId());
        dto.setName(school.getName());
        dto.setSchoolType(school.getSchoolType());
        dto.setLocation(school.getLocation());
        dto.setEnrollmentCapacity(school.getEnrollmentCapacity());
        dto.setLearnersCount(schoolRepository.countLearnersBySchoolId(school.getId()));
        dto.setCreatedAt(school.getCreatedAt());
        dto.setUpdatedAt(school.getUpdatedAt());
        return dto;
    }

    private PagedResponse<SchoolDTO> convertToPagedResponse(Page<School> schoolPage) {
        List<SchoolDTO> schools = schoolPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                schools,
                schoolPage.getNumber(),
                schoolPage.getSize(),
                schoolPage.getTotalElements(),
                schoolPage.getTotalPages(),
                schoolPage.isFirst(),
                schoolPage.isLast(),
                schoolPage.hasNext(),
                schoolPage.hasPrevious()
        );
    }
}

