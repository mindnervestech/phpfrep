package com.obs.brs.controller;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class RestApplication extends ResourceConfig {

	public RestApplication() {
		register(RequestContextFilter.class);
		register(Controller.class);
	}
}
