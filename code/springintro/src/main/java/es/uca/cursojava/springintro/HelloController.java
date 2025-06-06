package es.uca.cursojava.springintro;

import net.datafaker.Faker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {

	@GetMapping("/")
	public String index() {
		return "Saluditos desde la UCA!";
	}

	@GetMapping("/saluditos")
	public List<String> saluditos() {

		List<String> result=new ArrayList<>();
		Faker f= new Faker();

		for(int i=0;i<10;i++) {
			result.add(f.superMario().characters() + " te dice ¡Hola!");
		}

		return result;



	}

}