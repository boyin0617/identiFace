package com.example.entity;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "address")
public class address {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int addressid;
 

 private String addresscity;
 private String addressdist;
 private String addressdetail;
public String getAddresscity() {
	return addresscity;
}
public void setAddresscity(String addresscity) {
	this.addresscity = addresscity;
}
public String getAddressdist() {
	return addressdist;
}
public void setAddressdist(String addressdist) {
	this.addressdist = addressdist;
}
public String getAddressdetail() {
	return addressdetail;
}
public void setAddressdetail(String addressdetail) {
	this.addressdetail = addressdetail;
}


}
