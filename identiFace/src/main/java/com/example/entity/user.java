package com.example.entity;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class user {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int userid;
 
 
 private int faceid;
 private int familyid;
 private String phone;
 private String email;
 private String birth;
public int getUserid() {
	return userid;
}
public void setUserid(int userid) {
	this.userid = userid;
}
public int getFaceid() {
	return faceid;
}
public void setFaceid(int faceid) {
	this.faceid = faceid;
}
public int getFamilyid() {
	return familyid;
}
public void setFamilyid(int familyid) {
	this.familyid = familyid;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getBirth() {
	return birth;
}
public void setBirth(String birth) {
	this.birth = birth;
}
 


}
