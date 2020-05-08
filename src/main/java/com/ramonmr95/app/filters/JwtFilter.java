package com.ramonmr95.app.filters;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if (requestContext.getMethod().equals("OPTIONS")) {
			return;
		}
		
		String auth = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		Response response = null;

		if (auth == null || !auth.startsWith("Bearer ")) {
			response = Response.status(Status.UNAUTHORIZED)
					.entity("You must authenticate to access the requested resource").build();
			requestContext.abortWith(response);
		} else {
			try {
				String token = auth.substring("Bearer ".length());
				Algorithm algorithm = Algorithm
						.HMAC256("4M2nN3L3UtsJQpe7iQZNiSyPgpV-9Wt4DQ0tmKDqMvS01tQGUQRlKA9TvfkdoEoC");
				JWTVerifier verifier = JWT.require(algorithm).build();
				verifier.verify(token);
			} catch (JWTVerificationException e) {
				response = Response.status(Status.UNAUTHORIZED)
						.entity("You must authenticate to access the requested resource").build();
				requestContext.abortWith(response);
			}
		}

	}

}
