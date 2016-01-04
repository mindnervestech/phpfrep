package com.obs.brs.controller;

import java.util.ArrayList;
import java.util.List;

import com.obs.brs.model.DataEntry;

public class ScoreData {

	public List<DataEntry> dataEntry  =  new  ArrayList<>() ;
	public DataEntry dEntry  = new DataEntry() ;
	public List<DataEntry> getDataEntry() {
		return dataEntry;
	}
	public void setDataEntry(List<DataEntry> dataEntry) {
		this.dataEntry = dataEntry;
	}
	public DataEntry getdEntry() {
		return dEntry;
	}
	public void setdEntry(DataEntry dEntry) {
		this.dEntry = dEntry;
	}
	
}
