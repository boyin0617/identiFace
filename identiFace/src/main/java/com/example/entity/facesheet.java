package com.example.entity;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "face_datasheet")
public class facesheet {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int faceid;

 private int imageid;
 private String name;
 private int age;
 private String gender;
 

 public int getfaceId() {
  return faceid;
}
public void setfaceId(int id) {
 this.faceid = id;
}
public int getimageId() {
 return imageid;
}
public void setimageId(int id2) {
 this.imageid = id2;
}
public String getName() {
  return name;
 }
public void setName(String name) {
  this.name = name;
 }

public int getAge() {
  return age;
 }
public void setAge(int age) {
  this.age = age;
 }
 
public String getgender() {
 return gender;
}
public void setAddress(String gender) {
 this.gender = gender;
}
}
