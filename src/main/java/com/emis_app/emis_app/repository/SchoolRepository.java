package com.emis_app.emis_app.repository;

import com.emis_app.emis_app.entity.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long>, JpaSpecificationExecutor<School> {

    // Basic search methods
    Page<School> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<School> findBySchoolTypeIgnoreCase(String schoolType, Pageable pageable);

    Page<School> findByLocationContainingIgnoreCase(String location, Pageable pageable);

    Page<School> findByEnrollmentCapacityBetween(Integer minCapacity, Integer maxCapacity, Pageable pageable);

    // Combined search methods
    @Query("SELECT s FROM School s WHERE " +
            "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%'))) AND " +
            "(:schoolType IS NULL OR LOWER(s.schoolType) = LOWER(CAST(:schoolType AS string))) AND " +
            "(:location IS NULL OR LOWER(s.location) LIKE LOWER(CONCAT('%', CAST(:location AS string), '%'))) AND " +
            "(:minCapacity IS NULL OR s.enrollmentCapacity >= :minCapacity) AND " +
            "(:maxCapacity IS NULL OR s.enrollmentCapacity <= :maxCapacity)")
    Page<School> findBySearchCriteria(
            @Param("name") String name,
            @Param("schoolType") String schoolType,
            @Param("location") String location,
            @Param("minCapacity") Integer minCapacity,
            @Param("maxCapacity") Integer maxCapacity,
            Pageable pageable);




    // Statistical queries
    @Query("SELECT COUNT(l) FROM Learner l WHERE l.school.id = :schoolId")
    Long countLearnersBySchoolId(@Param("schoolId") Long schoolId);

    @Query("SELECT s.schoolType, COUNT(s) FROM School s GROUP BY s.schoolType")
    List<Object[]> countSchoolsByType();

    // Availability check
    @Query("SELECT s FROM School s WHERE s.enrollmentCapacity > " +
            "(SELECT COUNT(l) FROM Learner l WHERE l.school.id = s.id)")
    Page<School> findSchoolsWithAvailableCapacity(Pageable pageable);

    Optional<School> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
