package com.obs.brs.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Jeevanantham
 *
 */
@Entity
@Table(name="tbl_ocr_text_match_result")
public class OcrTextMatchResult implements Serializable{
	
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	@Column(name="ID" , nullable = false )
	private long Id;
	
	@Column(name="DC_CROPPED_JOBID")
	private long croppedData;
	
	@Column(name="DC_LIVE_JOBID") 
	private long liveData; 
	
	@Column(name="DC_LIVE_JOB_SCORE")
	private float liveJobScore;
	
	public long getId() {
		return Id;
	}

	

	public long getCroppedData() {
		return croppedData;
	}



	public void setCroppedData(long croppedData) {
		this.croppedData = croppedData;
	}



	public long getLiveData() {
		return liveData;
	}



	public void setLiveData(long liveData) {
		this.liveData = liveData;
	}



	public float getLiveJobScore() {
		return liveJobScore;
	}



	public void setLiveJobScore(float liveJobScore) {
		this.liveJobScore = liveJobScore;
	}

	public void setId(long id) {
		Id = id;
	}

	
}
