package es.uca.cursojava.javamoderno;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Aplicación principal de gestión escolar.
 */
public class Main {

    /**
     * @param args Argumentos de la línea de comandos (no se utilizan en este caso)
     */
    public static void main(String[] args) {
        SchoolManagement app = new SchoolManagement();

        // 0. Cargar datos de estudiantes, asignaturas e inscripciones
        System.out.println("=== Cargando datos ===");
        System.out.println("Cursos: " + app.loadCourses());
        System.out.println("Estudiantes: " + app.loadStudents());
        System.out.println("Inscripciones: " + app.loadEnrollments());
        System.out.println();

        // 1. Iniciar las clases
        System.out.println("=== Iniciando las clases ===");
        app.startCourses();
        System.out.println();

        // 2. Listado de estudiantes ordenados por año de nacimiento
        System.out.println("=== Estudiantes ordenados por año de nacimiento ===");
        List<Student> sortedByGrade = app.getStudentsSortedByYearOfBirthAsc();

        sortedByGrade.forEach(System.out::println);
        System.out.println();

        // 3. Contar estudiantes matriculados en cada curso
        Map<Course, Long> countByCourse = app.countStudentsByCourse();
        System.out.println("=== Número de estudiantes por titulación ===");
        countByCourse.forEach((course, count) ->
                System.out.printf("%s → %d estudiantes%n", course.getTitle(), count)
        );
        System.out.println();

        // 4. Finalizar las clases
        System.out.println("=== Finalizando las clases ===");
        app.finishCourses();
        System.out.println();

        // 5. Verificar si todos han aprobado (nota ≥ 5.0) (allMatch)
        boolean allPassed = app.allStudentsPassed();
        System.out.println("¿Todos los estudiantes han aprobado? → " + (allPassed ? "Sí" : "No"));
        System.out.println();

        // 6. Listado de estudiantes que han suspendido al menos una asignatura.
        System.out.println("=== Estudiantes con suspensos ===");
        List<Student> studentsWithFailures = app.getStudentsWithACourseFailed();
        studentsWithFailures.forEach(s -> System.out.println(s.getFullName()));
        System.out.println();

        // 7. Listado de nombres de estudiantes con matrícula de honor (nota ≥ 9.0).
        System.out.println("=== Estudiantes con matrículas de Honor (nota ≥ 9.0) ===");
        List<String> honoredStudentNames = app.getStudentNamesWithHonors();
        honoredStudentNames.forEach(System.out::println);
        System.out.println();

        // 8. Localizar una determinada matrícula en el almacén de datos
        System.out.println("=== Localización de una matriculación ===");

        // Usando el metodo tradicional,
        // falla debido a  NullPointerException
        SchoolManagement.EnrollmentDTO dto = app.getEnrollmentDTOByStudentIdAndCourseCodeV1("0rrrr", "INF-101");

        System.out.println("Matrícula encontrada: " + dto.studentFullName().toUpperCase());

        // Usando Optional para evitar NullPointerException
        Optional<SchoolManagement.EnrollmentDTO> optionalEnrollmentDTO = app.getEnrollmentDTOByStudentIdAndCourseCode("0qwwerwe", "INF-101");

        optionalEnrollmentDTO.get().courseTitle()


        if(optionalEnrollmentDTO.ifPresent()) {
            SchoolManagement.EnrollmentDTO enrollment = optionalEnrollmentDTO.get();
            System.out.println("Matrícula encontrada: " + enrollment.studentFullName().toUpperCase());
        } else {
            System.out.println("No se encontró matrícula");
        }



        optionalEnrollmentDTO.ifPresentOrElse(
                enrollment -> System.out.println("Matrícula encontrada: " + enrollment.toString().toUpperCase()),
                () -> System.out.println("No se encontró matrícula")
        );

        Optional<BigDecimal> nota = optionalEnrollmentDTO.map(enrollment -> enrollment.grade());
        System.out.println("Nota del estudiante: " + nota.orElse(BigDecimal.ZERO));
        System.out.println();


        // 9. Localizar un estudiante en el almacén de datos y presentar su ficha en pantalla
        System.out.println("=== Localización de un estudiante ===");

        Optional<Student> optStudent = app.getStudentByStudentId("0");

        if (optStudent.isPresent()) {
            String template = """
                    Ficha del estudiante:
                      - Id: %s
                      - Nombre completo: %s
                      - Año de nacimiento: %d
                    """;

            String report = template.formatted(
                    optStudent.get().getStudentId(),
                    optStudent.get().getFullName(),
                    optStudent.get().getYearOfBirth()
            );
            System.out.println(report);
        }


    }

}
