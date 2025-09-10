package com.emis_app.emis_app.service;


import com.emis_app.emis_app.dto.LearnerDTO;
import com.emis_app.emis_app.dto.PagedResponse;
import com.emis_app.emis_app.entity.Learner;
import com.emis_app.emis_app.entity.School;
import com.emis_app.emis_app.repository.LearnerRepository;
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
public class LearnerService {

    private final LearnerRepository learnerRepository;
    private final SchoolRepository schoolRepository;

    // CRUD Operations
    public LearnerDTO createLearner(LearnerDTO learnerDTO) {
        School school = schoolRepository.findById(learnerDTO.getSchoolId())
                .orElseThrow(() -> new RuntimeException("School not found with id: " + learnerDTO.getSchoolId()));

        // Check enrollment capacity
        Long currentEnrollment = learnerRepository.countBySchoolId(school.getId());
        if (currentEnrollment >= school.getEnrollmentCapacity()) {
            throw new RuntimeException("School has reached its enrollment capacity");
        }

        Learner learner = convertToEntity(learnerDTO, school);
        Learner savedLearner = learnerRepository.save(learner);
        return convertToDTO(savedLearner);
    }

    @Transactional(readOnly = true)
    public Optional<LearnerDTO> getLearnerById(Long id) {
        return learnerRepository.findById(id)
                .map(this::convertToDTO);
    }

    public LearnerDTO updateLearner(Long id, LearnerDTO learnerDTO) {
        Learner existingLearner = learnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Learner not found with id: " + id));

        // Check if school is being changed
        if (!existingLearner.getSchool().getId().equals(learnerDTO.getSchoolId())) {
            School newSchool = schoolRepository.findById(learnerDTO.getSchoolId())
                    .orElseThrow(() -> new RuntimeException("School not found with id: " + learnerDTO.getSchoolId()));

            // Check new school's capacity
            Long currentEnrollment = learnerRepository.countBySchoolId(newSchool.getId());
            if (currentEnrollment >= newSchool.getEnrollmentCapacity()) {
                throw new RuntimeException("New school has reached its enrollment capacity");
            }

            existingLearner.setSchool(newSchool);
        }

        existingLearner.setName(learnerDTO.getName());
        existingLearner.setGender(learnerDTO.getGender());
        existingLearner.setGrade(learnerDTO.getGrade());
        existingLearner.setAcademicYear(learnerDTO.getAcademicYear());

        Learner updatedLearner = learnerRepository.save(existingLearner);
        return convertToDTO(updatedLearner);
    }

    public void deleteLearner(Long id) {
        if (!learnerRepository.existsById(id)) {
            throw new RuntimeException("Learner not found with id: " + id);
        }
        learnerRepository.deleteById(id);
    }

    // Search Operations
    @Transactional(readOnly = true)
    public PagedResponse<LearnerDTO> getAllLearners(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Learner> learnerPage = learnerRepository.findAll(pageable);
        return convertToPagedResponse(learnerPage);
    }

    @Transactional(readOnly = true)
    public PagedResponse<LearnerDTO> searchLearnersByName(String name, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Learner> learnerPage = learnerRepository.findByNameContainingIgnoreCase(name, pageable);
        return convertToPagedResponse(learnerPage);
    }

    @Transactional(readOnly = true)
    public PagedResponse<LearnerDTO> searchLearnersByGender(String gender, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Learner> learnerPage = learnerRepository.findByGenderIgnoreCase(gender, pageable);
        return convertToPagedResponse(learnerPage);
    }

    @Transactional(readOnly = true)
    public PagedResponse<LearnerDTO> searchLearnersByGrade(String grade, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Learner> learnerPage = learnerRepository.findByGradeIgnoreCase(grade, pageable);
        return convertToPagedResponse(learnerPage);
    }

    @Transactional(readOnly = true)
    public PagedResponse<LearnerDTO> searchLearnersByAcademicYear(String academicYear, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Learner> learnerPage = learnerRepository.findByAcademicYear(academicYear, pageable);
        return convertToPagedResponse(learnerPage);
    }

    @Transactional(readOnly = true)
    public PagedResponse<LearnerDTO> searchLearnersBySchool(Long schoolId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Learner> learnerPage = learnerRepository.findBySchoolId(schoolId, pageable);
        return convertToPagedResponse(learnerPage);
    }

    @Transactional(readOnly = true)
    public PagedResponse<LearnerDTO> advancedSearch(String name, String gender, String grade,
                                                    String academicYear, Long schoolId, String schoolName,
                                                    int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Learner> learnerPage = learnerRepository.findBySearchCriteria(
                name, gender, grade, academicYear, schoolId, schoolName, pageable);
        return convertToPagedResponse(learnerPage);
    }

    // Utility methods
    private Learner convertToEntity(LearnerDTO dto, School school) {
        Learner learner = new Learner();
        learner.setName(dto.getName());
        learner.setGender(dto.getGender());
        learner.setGrade(dto.getGrade());
        learner.setAcademicYear(dto.getAcademicYear());
        learner.setSchool(school);
        return learner;
    }

    private LearnerDTO convertToDTO(Learner learner) {
        LearnerDTO dto = new LearnerDTO();
        dto.setId(learner.getId());
        dto.setName(learner.getName());
        dto.setGender(learner.getGender());
        dto.setGrade(learner.getGrade());
        dto.setAcademicYear(learner.getAcademicYear());
        dto.setSchoolId(learner.getSchool().getId());
        dto.setSchoolName(learner.getSchool().getName());
        dto.setCreatedAt(learner.getCreatedAt());
        dto.setUpdatedAt(learner.getUpdatedAt());
        return dto;
    }

    private PagedResponse<LearnerDTO> convertToPagedResponse(Page<Learner> learnerPage) {
        List<LearnerDTO> learners = learnerPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                learners,
                learnerPage.getNumber(),
                learnerPage.getSize(),
                learnerPage.getTotalElements(),
                learnerPage.getTotalPages(),
                learnerPage.isFirst(),
                learnerPage.isLast(),
                learnerPage.hasNext(),
                learnerPage.hasPrevious()
        );
    }
}