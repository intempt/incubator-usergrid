/*******************************************************************************
 * Copyright (c) 2010, 2011 Ed Anuff and Usergrid, all rights reserved.
 * http://www.usergrid.com
 * 
 * This file is part of Usergrid Stack.
 * 
 * Usergrid Stack is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * Usergrid Stack is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License along
 * with Usergrid Stack. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Additional permission under GNU AGPL version 3 section 7
 * 
 * Linking Usergrid Stack statically or dynamically with other modules is making
 * a combined work based on Usergrid Stack. Thus, the terms and conditions of the
 * GNU General Public License cover the whole combination.
 * 
 * In addition, as a special exception, the copyright holders of Usergrid Stack
 * give you permission to combine Usergrid Stack with free software programs or
 * libraries that are released under the GNU LGPL and with independent modules
 * that communicate with Usergrid Stack solely through:
 * 
 *   - Classes implementing the org.usergrid.services.Service interface
 *   - Apache Shiro Realms and Filters
 *   - Servlet Filters and JAX-RS/Jersey Filters
 * 
 * You may copy and distribute such a system following the terms of the GNU AGPL
 * for Usergrid Stack and the licenses of the other code concerned, provided that
 ******************************************************************************/
package org.usergrid.rest.security.shiro;

import static org.usergrid.rest.exceptions.AuthErrorInfo.BAD_ACCESS_TOKEN_ERROR;
import static org.usergrid.rest.exceptions.AuthErrorInfo.EXPIRED_ACCESS_TOKEN_ERROR;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.shiro.authc.AuthenticationException;
import org.usergrid.management.exceptions.BadAccessTokenException;
import org.usergrid.management.exceptions.ExpiredTokenException;
import org.usergrid.rest.ApiResponse;

@Provider
public class ShiroAuthenticationExceptionMapper implements
		ExceptionMapper<AuthenticationException> {

	@Override
	public Response toResponse(AuthenticationException e) {
		if (e.getCause() != null) {
			return constructResponse(e.getCause());
		}
		return constructResponse(e);
	}

	public Response constructResponse(Throwable e) {
		String type = null;
		String message = e.getMessage();
		ApiResponse response = new ApiResponse();
		if (e instanceof ExpiredTokenException) {
			type = EXPIRED_ACCESS_TOKEN_ERROR.getType();
		} else if (e instanceof BadAccessTokenException) {
			type = BAD_ACCESS_TOKEN_ERROR.getType();
		}
		response.withError(type, message, e);
		return Response.status(Status.UNAUTHORIZED).type("application/json")
				.entity(response).build();
	}

}
