package com.generation.lojadegames.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.lojadegames.model.Games;
import com.generation.lojadegames.repository.CategoriaRepository;
import com.generation.lojadegames.repository.GamesRepository;

@RestController
@RequestMapping("/games")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GamesController {
	
	@Autowired
	private GamesRepository gamesRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@GetMapping
	public ResponseEntity<List<Games>> getAll(){
		return ResponseEntity.ok(gamesRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Games> getById(@PathVariable Long id){
		return gamesRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Games>> getByNome(@PathVariable String nome){
		return ResponseEntity.ok(gamesRepository.findAllByNomeContainingIgnoreCase(nome));
	}
	
	@PostMapping
	public ResponseEntity<Games> post(@Valid @RequestBody Games games){
		if(categoriaRepository.existsById(games.getCategoria().getId()))
			return ResponseEntity.status(HttpStatus.CREATED)
				.body(gamesRepository.save(games));
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	@PutMapping
	public ResponseEntity<Games> put(@Valid @RequestBody Games games){
		if(gamesRepository.existsById(games.getId())) {
			
			if(categoriaRepository.existsById(games.getCategoria().getId()))
				return ResponseEntity.status(HttpStatus.OK)
						.body(gamesRepository.save(games));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Games> games = gamesRepository.findById(id);
		
		if(games.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		gamesRepository.deleteById(id);
	}
}
