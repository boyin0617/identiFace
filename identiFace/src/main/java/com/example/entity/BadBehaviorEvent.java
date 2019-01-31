
	package com.example.entity;

	import javax.persistence.Entity;
	import javax.persistence.GeneratedValue;
	import javax.persistence.GenerationType;
	import javax.persistence.Id;
	import javax.persistence.Table;

	@Entity
	@Table(name = "bad_behavior_event")
	public class BadBehaviorEvent {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private int behaviorid;
	 private int blacklistid;
	 private String behaviordate;
	 private String supplement;
	 private String solution;
	public int getBehaviorid() {
		return behaviorid;
	}
	public void setBehaviorid(int behaviorid) {
		this.behaviorid = behaviorid;
	}
	public int getBlacklistid() {
		return blacklistid;
	}
	public void setBlacklistid(int blacklistid) {
		this.blacklistid = blacklistid;
	}
	public String getBehaviordate() {
		return behaviordate;
	}
	public void setBehaviordate(String behaviordate) {
		this.behaviordate = behaviordate;
	}
	public String getSupplement() {
		return supplement;
	}
	public void setSupplement(String supplement) {
		this.supplement = supplement;
	}
	public String getSolution() {
		return solution;
	}
	public void setSolution(String solution) {
		this.solution = solution;
	}

}