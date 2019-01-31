package com.example.entity;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "blacklist")
public class blacklist {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int blacklistid;
 
 
 private int faceid;
 private String categories;
 private String notice;
public int getBlacklistid() {
	return blacklistid;
}
public void setBlacklistid(int blacklistid) {
	this.blacklistid = blacklistid;
}
public int getFaceid() {
	return faceid;
}
public void setFaceid(int faceid) {
	this.faceid = faceid;
}
public String getCategories() {
	return categories;
}
public void setCategories(String categories) {
	this.categories = categories;
}
public String getNotice() {
	return notice;
}
public void setNotice(String notice) {
	this.notice = notice;
}



}
