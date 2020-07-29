package com.filosofiadalvik.spring.boot.backend.api.rest.example.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.filosofiadalvik.spring.boot.backend.api.rest.example.models.entity.Client;

public interface IClientDao extends CrudRepository<Client, Long>{
	
	
}
