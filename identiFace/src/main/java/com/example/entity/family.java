package com.example.entity;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "family")
public class family {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int familyid;
 
 
 private int addressid;
 private int familynumber;
 private String familystyle;
public int getFamilyid() {
	return familyid;
}
public void setFamilyid(int familyid) {
	this.familyid = familyid;
}
public int getAddressid() {
	return addressid;
}
public void setAddressid(int addressid) {
	this.addressid = addressid;
}
public int getFamilynumber() {
	return familynumber;
}
public void setFamilynumber(int familynumber) {
	this.familynumber = familynumber;
}
public String getFamilystyle() {
	return familystyle;
}
public void setFamilystyle(String familystyle) {
	this.familystyle = familystyle;
}





}
