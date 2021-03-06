package com.example.dao;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.CrudRepository;

import com.example.entity.*;

@RepositoryRestResource(collectionResourceRel = "address", path = "address")
public interface addressDAO extends CrudRepository<address, Integer> {

}