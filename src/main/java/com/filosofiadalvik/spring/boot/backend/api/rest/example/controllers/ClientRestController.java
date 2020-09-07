package com.filosofiadalvik.spring.boot.backend.api.rest.example.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filosofiadalvik.spring.boot.backend.api.rest.example.models.entity.Client;
import com.filosofiadalvik.spring.boot.backend.api.rest.example.models.services.IClientService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClientRestController {
	
	@Autowired
	IClientService clientService;

	@GetMapping("/clients")
	public List<Client> index(){
		return clientService.findAll();
	}
	
	@GetMapping("/clients/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Client client = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			client = clientService.findById(id);
		}catch(DataAccessException e) {
			response.put("message", "Database error, try again later.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (client == null) {
			response.put("message", "Client with ID: ".concat(id.toString().concat(" was not found.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Client>(client, HttpStatus.OK);
	}
	
	@PostMapping("/clients")
	public ResponseEntity<?> create(@Valid @RequestBody Client client, BindingResult result) {
		Client newClient = null;
		Map<String, Object> response = new HashMap<>();
		
		if (result.hasErrors()) {
			
			/* Legacy way to do this.
			List<String> errors = new ArrayList<>();
			for (FieldError error: result.getFieldErrors(field)) {
				errors.add("The field '" + error.getField() + "' has an error: " + error.getDefaultMessage());
			}*/
			
			//The Java 8 way, functional.
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(error -> "The field '" + error.getField() + "' has an error: " + error.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			newClient = clientService.save(client);
			
		}catch(DataAccessException e) {
			response.put("message", "Database error, try again later.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "Client successfully saved.");
		response.put("client", newClient);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/clients/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Client client, BindingResult result, @PathVariable Long id) {
		Client currentClient = clientService.findById(id);
		Client updatedClient = null;
		Map<String, Object> response = new HashMap<>();
		
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(error -> "The field '" + error.getField() + "' has an error: " + error.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (currentClient == null) {
			response.put("message", "Client with ID: ".concat(id.toString().concat(" was not found.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			currentClient.setName(client.getName());
			currentClient.setLastName(client.getLastName());
			currentClient.setEmail(client.getEmail());
			
			updatedClient = clientService.save(currentClient);
		}catch(DataAccessException e) {
			response.put("message", "Database error, try again later.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "Client successfully updated.");
		response.put("client", updatedClient);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		
	}
	
	@DeleteMapping("/clients/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			clientService.delete(id);
		}catch(DataAccessException e) {
			response.put("message", "Database error, try again later.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "Client successfully deleted");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
