package com.example.dao;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.CrudRepository;

import com.example.entity.*;

@RepositoryRestResource(collectionResourceRel = "face_datasheet", path = "face_datasheet")
public interface facesheetDAO extends CrudRepository<facesheet, Integer> {

}