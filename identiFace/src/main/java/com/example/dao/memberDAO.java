package com.example.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.entity.Member;

@RepositoryRestResource(collectionResourceRel = "member", path = "member")
public interface memberDAO extends CrudRepository<Member, Long> {

}
