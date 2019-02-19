package com.example.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long faceid;

 private String name;
 private String gender;
 private int age;

 public Long getFaceId() {
  return faceid;
}
public void setFaceId(Long faceid) {
 this.faceid = faceid;
}

public String getName() {
  return name;
 }
 public void setName(String name) {
  this.name = name;
 }

 public String getGender() {
  return gender;
 }
 public void setGender(String gender) {
  this.gender = gender;
 }

 public int getAge() {
  return age;
 }
 public void setAge(int age) {
  this.age = age;
 }
}
