package com.example.dao;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.CrudRepository;

import com.example.entity.*;

@RepositoryRestResource(collectionResourceRel = "BadBehaviorEvent", path = "BadBehaviorEvent")
public interface BadBehaviorEventDAO extends CrudRepository<BadBehaviorEvent, Integer> {

}