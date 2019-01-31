package com.example.entity;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "head_datasheet")
public class headsheet {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int imageid;
 

 private String imagename;
 private String imagepath;
 

public int getimageId() {
 return imageid;
}
public void setimageId(int id) {
 this.imageid = id;
}

public String getImageName() {
	return imagename;
}
public void setImageName(String imageName) {
	this.imagename = imageName;
}
public String getImagePath() {
	return imagepath;
}
public void setImagePath(String imagePath) {
	this.imagepath = imagePath;
}
}
