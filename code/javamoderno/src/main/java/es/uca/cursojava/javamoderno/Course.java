package es.uca.cursojava.javamoderno;

import java.util.Objects;

public class Course {
    private String courseCode;
    private String title;
    private int credits;
    private String department;

    public Course(String courseCode, String title, int credits, String department) {
        this.courseCode = courseCode;
        this.title = title;
        this.credits = credits;
        this.department = department;
    }

    // Getters y setters
    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return String.format("%s – %s (%d créditos, Departamento: %s)",
                courseCode, title, credits, department);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return Objects.equals(courseCode, course.courseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode);
    }
}
