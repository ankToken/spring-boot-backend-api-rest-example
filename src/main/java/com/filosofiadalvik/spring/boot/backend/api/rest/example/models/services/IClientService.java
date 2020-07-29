package com.filosofiadalvik.spring.boot.backend.api.rest.example.models.services;

import java.util.List;

import com.filosofiadalvik.spring.boot.backend.api.rest.example.models.entity.Client;

public interface IClientService {

	public List <Client> findAll();
	
	public Client findById(Long id);
	
	public Client save(Client client);
	
	public void delete(Long id);
}
