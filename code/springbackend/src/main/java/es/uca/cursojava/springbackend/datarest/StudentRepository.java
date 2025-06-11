package es.uca.cursojava.springbackend.datarest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

// Exponemos el recurso en path /students
@RepositoryRestResource(collectionResourceRel = "students", path = "students")
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Ejemplo de método de búsqueda personalizado:
    List<Student> findByLastNameIgnoreCase(String lastName);
}