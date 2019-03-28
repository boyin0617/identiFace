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
	private String memberId;
	
	private Long member_faceId;
	private Long member_familyId;
	private String member_name;
	private String phone;
	private String email;
	private Date birth;
	
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	public Long getFaceId() {
		return member_faceId;
	}

	public void setFaceId(Long member_faceId) {
		this.member_faceId = member_faceId;
	}

	public Long getFamilyId() {
		return member_familyId;
	}

	public void setFamilyId(Long member_familyId) {
		this.member_familyId = member_familyId;
	}

	public String getName() {
		return member_name;
	}

	public void setName(String member_name) {
		this.member_name = member_name;
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

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

}
