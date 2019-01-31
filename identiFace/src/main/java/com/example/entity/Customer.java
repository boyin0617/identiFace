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
 private int id;

 private String name;
 private String phonenumber;
 private int age;

 public int getId() {
  return id;
}
public void setId(int id) {
 this.id = id;
}

public String getName() {
  return name;
 }
 public void setName(String name) {
  this.name = name;
 }

 public String getPhonenumber() {
  return phonenumber;
 }
 public void setAddress(String phonenumber) {
  this.phonenumber = phonenumber;
 }

 public int getAge() {
  return age;
 }
 public void setAge(int age) {
  this.age = age;
 }
}
