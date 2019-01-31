package com.example.dao;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.CrudRepository;

import com.example.entity.*;

@RepositoryRestResource(collectionResourceRel = "head_datasheet", path = "head_datasheet")
public interface headsheetDAO extends CrudRepository<headsheet, Integer> {

}