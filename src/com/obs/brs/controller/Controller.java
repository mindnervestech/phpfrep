package com.obs.brs.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.obs.brs.service.IDeService;

@Path("test")
public class Controller {

	 @Autowired
	IDeService deService;
	 
	 @GET
	 public String getHello() {
	     System.out.println(deService.getDataEntry().toString()); 
	     return "done";
	 }
}
