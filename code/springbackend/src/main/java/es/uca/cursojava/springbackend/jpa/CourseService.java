package es.uca.cursojava.springbackend.jpa;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course create(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> readAll() {
        return courseRepository.findAll();
    }

    public Optional<Course> readOne(UUID id) {

        return courseRepository.findById(id);
    }


    public Course update(Course course) {
        return courseRepository.save(course);
    }


    public void delete(UUID id) {
        courseRepository.deleteById(id);
    }

    public int count() {
        return (int) courseRepository.count();
    }
}