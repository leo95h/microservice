package br.com.bookservice.controller;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bookservice.model.Book;
import br.com.bookservice.proxy.CambioProxy;
import br.com.bookservice.repository.BookRepository;

@RestController
@RequestMapping("/book-service")
public class BookController {

	@Autowired
	private Environment environment;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private CambioProxy cambioProxy;
	
//  
//  Estrutura usando a interface Feign
//
	@GetMapping(value = "/{id}/{currency}")
	public Optional<Book> findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {
		
		var book = bookRepository.findById(id);
		
		if (book == null) {
			throw new RuntimeException("Book not found");
		}
		
		HashMap<String, String> params = new HashMap<>();
		
		params.put("amount", book.get().getPrice().toString());
		params.put("from", "USD");
		params.put("to", currency);
		
		var cambio = cambioProxy.getCambio(book.get().getPrice(), "USD", currency);
		
		var port = environment.getProperty("local.server.port");
		book.get().setEnvironment(port);
		book.get().setCurrency(currency);
		book.get().setPrice(cambio.getConvertedValue());
				
		return book;
	}
	
//  
//  Estrutura sem usar a interface Feign
//
//	@GetMapping(value = "/{id}/{currency}")
//	public Optional<Book> findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {
//		
//		var book = bookRepository.findById(id);
//		
//		if (book == null) {
//			throw new RuntimeException("Book not found");
//		}
//		
//		HashMap<String, String> params = new HashMap<>();
//		
//		params.put("amount", book.get().getPrice().toString());
//		params.put("from", "USD");
//		params.put("to", currency);
//		
//		var response = new RestTemplate().getForEntity("http://localhost:8000/cambio-service/{amount}/{from}/{to}", Cambio.class, params);
//		var cambio = response.getBody();
//		
//		var port = environment.getProperty("local.server.port");
//		book.get().setEnvironment(port);
//		book.get().setCurrency(currency);
//		book.get().setPrice(cambio.getConvertedValue());
//		
//		return book;
//	}
	
}
