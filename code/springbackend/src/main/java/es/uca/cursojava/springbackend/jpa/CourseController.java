package es.uca.cursojava.springbackend.jpa;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.readAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable UUID id) {
        Optional<Course> course = courseService.readOne(id);
        if (course.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(course.get());
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.create(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable UUID id, @RequestBody Course courseDetails) {
        Optional<Course> course = courseService.readOne(id);
        if (course.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        course.get().setCode(courseDetails.getCode());
        course.get().setTitle(courseDetails.getTitle());
        course.get().setCredits(courseDetails.getCredits());

        Course updatedCourse = courseService.update(course.get());
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable UUID id) {
        Optional<Course> course = courseService.readOne(id);
        if (course.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}