package com.filosofiadalvik.spring.boot.backend.api.rest.example.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filosofiadalvik.spring.boot.backend.api.rest.example.models.entity.Client;
import com.filosofiadalvik.spring.boot.backend.api.rest.example.models.services.IClientService;

@RestController
@RequestMapping("/api")
public class ClientRestController {
	
	@Autowired
	IClientService clientService;

	@GetMapping("/clients")
	public List<Client> index(){
		return clientService.findAll();
	}
}
