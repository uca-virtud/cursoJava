package es.uca.cursojava.springbackend.jpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    List<Course> findByTitleContainingIgnoreCase(String title);

    List<Course> findByCreditsGreaterThan(int credits, Sort sort);

}