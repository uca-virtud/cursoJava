package es.uca.cursojava.springbackend.hateoas;

import es.uca.cursojava.springbackend.mvc.AuthorController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookModelAssembler implements RepresentationModelAssembler<Book, EntityModel<Book>> {


    @Override
    public EntityModel<Book> toModel(Book book) {
        WebMvcLinkBuilder self = linkTo(methodOn(BookController.class).getById(book.getId()));
        EntityModel<Book> model = EntityModel.of(book);
        // Agrega links al modelo
        model.add(self.withSelfRel());
        model.add(linkTo(methodOn(AuthorController.class).getById(book.getAuthorId())).withRel("author").withType("GET"));
        model.add(linkTo(methodOn(BookController.class).create(null)).withRel("create").withType("POST"));

        // if usuario tiene permisos para actualiza...
        model.add(linkTo(methodOn(BookController.class).update(book.getId(), null)).withRel("update").withType("PUT"));
        return model;
    }
}
