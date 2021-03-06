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
package org.entando.entando.apsadmin.portal.guifragment.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.guifragment.GuiFragmentUtilizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.BaseActionHelper;
import com.agiletec.apsadmin.user.group.helper.GroupActionHelper;

public class GuiFragmentActionHelper extends BaseActionHelper implements IGuiFragmentActionHelper {

	private static final Logger _logger = LoggerFactory.getLogger(GroupActionHelper.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<Object>> getReferencingObjects(GuiFragment fragment, HttpServletRequest request) throws ApsSystemException {
		Map<String, List<Object>> references = new HashMap<String, List<Object>>();
    	try {
    		String[] defNames = ApsWebApplicationUtils.getWebApplicationContext(request).getBeanNamesForType(GuiFragmentUtilizer.class);
			for (int i=0; i < defNames.length; i++) {
				Object service = null;
				try {
					service = ApsWebApplicationUtils.getWebApplicationContext(request).getBean(defNames[i]);
				} catch (Throwable t) {
					_logger.error("error in hasReferencingObjects", t);
					service = null;
				}
				if (service != null) {
					GuiFragmentUtilizer guiFragUtilizer = (GuiFragmentUtilizer) service;
					List<Object> utilizers = guiFragUtilizer.getGuiFragmentUtilizers(fragment.getCode());
					if (utilizers != null && !utilizers.isEmpty()) {
						//FIXME UNIFORMARE E STUDIARE CHIAVE IDONEA
						references.put(guiFragUtilizer.getName()+"Utilizers", utilizers);
					}
				}
			}
    	} catch (Throwable t) {
    		throw new ApsSystemException("Error in getReferencingObjects", t);
    	}
    	return references;
	}
}
