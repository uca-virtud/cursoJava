package es.uca.cursojava.javamoderno;

import net.datafaker.Faker;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Clase de gestión de una universidad.
 */
public class SchoolManagement {
    private final List<Student> students;
    private final List<Course> courses;
    private final List<Enrollment> enrollments;


    private final Random random = new Random();
    private final Faker faker = new Faker();


    /**
     * Constructor de la clase SchoolManagement.
     */
    public SchoolManagement() {
        this.students = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.enrollments = new ArrayList<>();
    }


    /**
     * Cargar cursos.
     *
     * @return Número de cursos cargados
     */
    public int loadCourses() {
        // Añadir al menos 3
        courses.add(new Course("INF-101", "Programación I", 6, "Informática"));
        courses.add(new Course("MAT-201", "Álgebra", 5, "Matemáticas"));
        courses.add(new Course("FIS-301", "Física", 5, "Física"));
        return courses.size();
    }

    /**
     * Cargar estudiantes.
     *
     * @return Número de estudiantes cargados
     */
    public int loadStudents() {

        students.add(new Student("0", "Estudiante #0", 1980));

        // Supplier para crear estudiante genérico
        Supplier<Student> randomStudentSupplier = () -> {
            String id = String.valueOf(1 + random.nextInt(10000));
            return new Student(id, faker.name().fullName(), 1980 + random.nextInt(30));
        };

        students.add(randomStudentSupplier.get());
        students.add(randomStudentSupplier.get());
        students.add(randomStudentSupplier.get());
        students.add(randomStudentSupplier.get());
        students.add(randomStudentSupplier.get());
        return students.size();


    }


    /**
     * Cargar inscripciones.
     *
     * @return Número de inscripciones cargadas
     */
    public int loadEnrollments() {
        // Cada estudiante se inscribe en todos los cursos

        for (Student student : students) {
            for (Course course : courses) {
                enrollments.add(new Enrollment(student, course));
            }
        }
        return enrollments.size();
    }

    /**
     * Listado de estudiantes ordenados por nota descendente.
     */
    public List<Student> getStudentsSortedByYearOfBirthAsc() {
        Comparator<Student> byYearOfBirthDescending = (e1, e2) ->
                Integer.compare(e1.getYearOfBirth(), e2.getYearOfBirth());


        return students.stream()
                .sorted(byYearOfBirthDescending)
                .collect(Collectors.toList());
    }


    /**
     * Comienza la impartición de cursos.
     */
    public void startCourses() {
        Consumer<Enrollment> startEnrollmentConsumer = enrollment -> {
            // Simula el inicio de la inscripción
            enrollment.setStatus(Status.ACTIVE);
            System.out.println("Iniciando " + enrollment);
        };

        enrollments.forEach(startEnrollmentConsumer);
    }


    /**
     * Contar número de estudiantes de cada curso.
     */
    public Map<Course, Long> countStudentsByCourse() {
        return enrollments.stream()
                .collect(Collectors.groupingBy(Enrollment::getCourse, Collectors.counting()));
    }


    /**
     * Finaliza la impartición de cursos.
     */
    public void finishCourses() {
        Consumer<Enrollment> finishEnrollmentConsumer = enrollment -> {
            // Simula el final de la inscripción
            enrollment.setStatus(Status.COMPLETED);

            // Aquí podrías asignar una nota aleatoria entre 0 y 10
            double grade = random.nextInt(11);
            enrollment.setGrade(BigDecimal.valueOf(grade));
            System.out.println("Finalizando " + enrollment);
        };

        enrollments.forEach(finishEnrollmentConsumer);
    }


    /**
     * Verificar si los estudiantes han aprobado (nota ≥ 5.0).
     */
    public boolean allStudentsPassed() {
        Predicate<Enrollment> isPassed = e -> e.getGrade().compareTo(BigDecimal.valueOf(5.0)) > 0;
        return enrollments.stream().allMatch(isPassed);
    }

    /**
     * Listado de estudiantes que han suspendido al menos una asignatura.
     */
    public List<Student> getStudentsWithACourseFailed() {
        Predicate<Enrollment> hasFailedPredicate = e -> e.getGrade().compareTo(BigDecimal.valueOf(5.0)) < 0;
        Function<Enrollment, Student> enrollmentToStudentFunction = e -> e.getStudent();

        return enrollments.stream()
                .filter(hasFailedPredicate)
                .map(enrollmentToStudentFunction)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Listado de nombres de estudiantes con matrícula de honor (nota ≥ 9.0).
     */
    public List<String> getStudentNamesWithHonors() {

        return enrollments.stream()
                .filter(e -> e.getGrade().compareTo(BigDecimal.valueOf(9.0)) >= 0)
                .map(Enrollment::getStudent)
                .distinct()
                .map(Student::getFullName)
                .collect(Collectors.toList());
    }

    /**
     * @param studentId  Identificador del estudiante
     * @param courseCode Código del curso
     * @return Un EnrollmentDTO si se encuentra la inscripción, o vacío si no existe.
     */
    public EnrollmentDTO getEnrollmentDTOByStudentIdAndCourseCodeV1(String studentId, String courseCode) {
        for (Enrollment e : enrollments) {
            if (e.getStudent().getStudentId().equals(studentId) && e.getCourse().getCourseCode().equals(courseCode)) {
                return new EnrollmentDTO(
                        e.getStudent().getStudentId(),
                        e.getStudent().getFullName(),
                        e.getCourse().getCourseCode(),
                        e.getCourse().getTitle(),
                        e.getGrade()
                );
            }
        }

        return null; // Si no se encuentra, se devuelve null


    }

    /**
     * @param studentId  Identificador del estudiante
     * @param courseCode Código del curso
     * @return Un Optional que contiene un EnrollmentDTO si se encuentra la inscripción, o vacío si no existe.
     */
    public Optional<EnrollmentDTO> getEnrollmentDTOByStudentIdAndCourseCode(String studentId, String courseCode) {
        return enrollments.stream()
                .filter(e -> e.getStudent().getStudentId().equals(studentId) && e.getCourse().getCourseCode().equals(courseCode))
                .findFirst()
                .map(e -> new EnrollmentDTO(
                        e.getStudent().getStudentId(),
                        e.getStudent().getFullName(),
                        e.getCourse().getCourseCode(),
                        e.getCourse().getTitle(),
                        e.getGrade()
                ));
    }

    /**
     * @param studentId
     * @return
     */
    public Optional<Student> getStudentByStudentId(String studentId) {
        return students.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst();
    }

    public static record EnrollmentDTO(String studentId, String studentFullName, String courseCode, String courseTitle,
                                       BigDecimal grade) {
    }


}
