package com.emis_app.emis_app.repository;

import com.emis_app.emis_app.entity.Learner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearnerRepository extends JpaRepository<Learner, Long>, JpaSpecificationExecutor<Learner> {

    // Basic search methods
    Page<Learner> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Learner> findByGenderIgnoreCase(String gender, Pageable pageable);

    Page<Learner> findByGradeIgnoreCase(String grade, Pageable pageable);

    Page<Learner> findByAcademicYear(String academicYear, Pageable pageable);

    Page<Learner> findBySchoolId(Long schoolId, Pageable pageable);

    // Combined search methods
    @Query("SELECT l FROM Learner l JOIN l.school s WHERE " +
            "(:name IS NULL OR LOWER(l.name) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%'))) AND " +
            "(:gender IS NULL OR LOWER(l.gender) = LOWER(CAST(:gender AS string))) AND " +
            "(:grade IS NULL OR LOWER(l.grade) = LOWER(CAST(:grade AS string))) AND " +
            "(:academicYear IS NULL OR l.academicYear = :academicYear) AND " +
            "(:schoolId IS NULL OR l.school.id = :schoolId) AND " +
            "(:schoolName IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', CAST(:schoolName AS string), '%')))")
    Page<Learner> findBySearchCriteria(
            @Param("name") String name,
            @Param("gender") String gender,
            @Param("grade") String grade,
            @Param("academicYear") String academicYear,
            @Param("schoolId") Long schoolId,
            @Param("schoolName") String schoolName,
            Pageable pageable);


    // Statistical queries
    @Query("SELECT l.gender, COUNT(l) FROM Learner l GROUP BY l.gender")
    List<Object[]> countLearnersByGender();

    @Query("SELECT l.grade, COUNT(l) FROM Learner l GROUP BY l.grade ORDER BY l.grade")
    List<Object[]> countLearnersByGrade();

    @Query("SELECT l.academicYear, COUNT(l) FROM Learner l GROUP BY l.academicYear ORDER BY l.academicYear DESC")
    List<Object[]> countLearnersByAcademicYear();

    @Query("SELECT s.name, COUNT(l) FROM Learner l JOIN l.school s GROUP BY s.name ORDER BY COUNT(l) DESC")
    List<Object[]> countLearnersBySchool();

    // School-specific queries
    Long countBySchoolId(Long schoolId);

    @Query("SELECT COUNT(l) FROM Learner l WHERE l.school.id = :schoolId AND l.gender = :gender")
    Long countBySchoolIdAndGender(@Param("schoolId") Long schoolId, @Param("gender") String gender);

    @Query("SELECT COUNT(l) FROM Learner l WHERE l.school.id = :schoolId AND l.grade = :grade")
    Long countBySchoolIdAndGrade(@Param("schoolId") Long schoolId, @Param("grade") String grade);
}
