package com.example.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "member")
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long member_Id;
	
	private Long face_Id;
	private Long family_Id;
	private String phone;
	private String name;
	private String email;
	private Date birth;
	
	public Long getMemberId() {
		return member_Id;
	}

	public void setMemberId(Long member_Id) {
		this.member_Id = member_Id;
	}
	
	public Long getFaceId() {
		return face_Id;
	}

	public void setFaceId(Long face_Id) {
		this.face_Id = face_Id;
	}

	public Long getFamilyId() {
		return family_Id;
	}

	public void setFamilyId(Long family_Id) {
		this.family_Id = family_Id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

}
