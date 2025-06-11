package es.uca.cursojava.springbackend;

import es.uca.cursojava.springbackend.hateoas.Book;
import es.uca.cursojava.springbackend.hateoas.BookController;
import es.uca.cursojava.springbackend.jpa.Course;
import es.uca.cursojava.springbackend.jpa.CourseService;
import es.uca.cursojava.springbackend.mvc.Author;
import es.uca.cursojava.springbackend.mvc.AuthorController;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbackendApplication.class, args);
    }

    @Bean
    CommandLineRunner init(BookController bookController, AuthorController authorController, CourseService courseService) {
        return args -> {

            Faker dataFaker = new Faker();

            // Initialize authors and books

            for (int i = 1; i <= 10; i++) {
                System.out.println("Adding author " + i);
                Author author = authorController.create(new Author(dataFaker.idNumber().valid(), dataFaker.name().firstName(), dataFaker.name().lastName(), dataFaker.internet().emailAddress()));

                for (int j = 1; j <= 5; j++) {
                    System.out.println("Adding book " + j);
                    bookController.create(new Book(dataFaker.code().isbn10(), dataFaker.book().title(), author.getId(), dataFaker.book().genre(), dataFaker.number().numberBetween(1900, 2023)));
                }

            }


            // Initialize courses
            if (courseService.count() > 0) {
                System.out.println("Courses already exist, skipping course creation.");
                return;
            }
            for (int i = 1; i <= 10; i++) {
                System.out.println("Adding course " + i);
                Course course = new Course();
                course.setCode(dataFaker.code().ean8());
                course.setTitle(dataFaker.educator().course());
                course.setCredits(dataFaker.number().numberBetween(1, 10));
                courseService.create(course);
            }


        };


    }
}
