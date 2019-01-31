package com.example.dao;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.CrudRepository;

import com.example.entity.*;

@RepositoryRestResource(collectionResourceRel = "Customer", path = "Customer")
public interface CustomerDAO extends CrudRepository<Customer, Integer> {

}