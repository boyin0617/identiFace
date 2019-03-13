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

 private String imagePath;
 private int age;
 private String gender;
 

 public int getfaceId() {
  return faceid;
}
public void setfaceId(int id) {
 this.faceid = id;
}
public String getimagePath() {
 return imagePath;
}
public void setimagePath(String imagePath) {
 this.imagePath = imagePath;
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
