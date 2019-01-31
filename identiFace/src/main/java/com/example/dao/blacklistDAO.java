package com.example.dao;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.CrudRepository;

import com.example.entity.*;

@RepositoryRestResource(collectionResourceRel = "blacklist", path = "blacklist")
public interface blacklistDAO extends CrudRepository<blacklist, Integer> {

}