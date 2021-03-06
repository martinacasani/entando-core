/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.apsadmin.tags.util;

import java.io.Writer;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.components.Component;

import com.agiletec.apsadmin.tags.ParamMapTag;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * Component class for tag {@link ParamMapTag} used to parameterize other tags with a map of parameters.
 * @author E.Santoboni
 */
public class ParamMap extends Component {
	
	public ParamMap(ValueStack stack) {
		super(stack);
	}
	
	@Override
	public boolean end(Writer writer, String body) {
		Log log = LogFactory.getLog(ParamMap.class);
		Component component = this.findAncestor(Component.class);
		if (null == this.getMap()) {
			log.info("Attribute map is mandatory.");
			return super.end(writer, body);
		}
		Object object = this.findValue(this.getMap());
		if (null == object) {
			log.debug("Map not found in ValueStack");
			return super.end(writer, body);
		}
		if (!(object instanceof Map)) {
			log.warn("Error in JSP. Attribute map must evaluate to java.util.Map. Found type: " + object.getClass().getName());
			return super.end(writer, body);
		}
		component.addAllParameters((Map) object);
		return super.end(writer, body);
	}
	
	protected String getMap() {
		return _map;
	}
	public void setMap(String map) {
		this._map = map;
	}
	
	private String _map;
	
}