package es.uca.cursojava.javamoderno;

import java.math.BigDecimal;
import java.util.Objects;

public class Enrollment {
    private final int academicYear;
    private Student student;
    private Course course;
    private Status status;
    private BigDecimal grade;

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.status = Status.INACTIVE; // Por defecto, la inscripción es inactiva
        this.academicYear = java.time.LocalDate.now().getYear(); // Año académico actual
        this.grade = BigDecimal.ZERO; // Inicialmente, la nota es cero
    }

    // Getters y setters
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Inscripción: %s en %s, Estado: %s, Fecha: %s, Nota: %s",
                student.getFullName(), course.getTitle(), status, academicYear, grade);
    }


    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Enrollment that)) return false;
        return academicYear == that.academicYear && Objects.equals(student, that.student) && Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(academicYear, student, course);
    }
}
