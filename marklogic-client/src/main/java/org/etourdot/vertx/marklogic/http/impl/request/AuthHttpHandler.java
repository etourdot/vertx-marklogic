/*
 * Copyright (C) 2015 - 2016 Emmanuel Tourdot
 *
 * See the NOTICE file distributed with this work for additional information regarding copyright ownership.
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this software.
 * If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.etourdot.vertx.marklogic.http.impl.request;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClientResponse;
import org.etourdot.vertx.marklogic.http.AuthScheme;
import org.etourdot.vertx.marklogic.http.MarkLogicResponse;
import org.etourdot.vertx.marklogic.http.impl.AuthSchemeFactory;
import org.etourdot.vertx.marklogic.http.impl.response.DefaultResponse;

public class AuthHttpHandler implements Handler<HttpClientResponse> {
  private final DefaultMarklogicRequest request;
  private final Handler<MarkLogicResponse> responseHandler;

  public AuthHttpHandler(DefaultMarklogicRequest request, Handler<MarkLogicResponse> responseHandler) {
    this.responseHandler = responseHandler;
    this.request = request;
  }

  @Override
  public void handle(HttpClientResponse httpClientResponse) {
    if (HttpResponseStatus.UNAUTHORIZED.code() == httpClientResponse.statusCode() &&
      !request.authSchemes().containsKey(request.uri())) {
      if (httpClientResponse.headers() != null) {
        final String headerAuthenticate = httpClientResponse.headers().get("WWW-Authenticate");
        if (headerAuthenticate != null) {
          final String[] parts = headerAuthenticate.trim().split("\\s+", 2);
          if (parts.length == 2) {
            final AuthScheme.Type schemeNeeded = AuthScheme.Type.valueOf(parts[0].toUpperCase());
            if (schemeNeeded != null) {
              final AuthScheme authScheme;
              if (schemeNeeded == request.realm().getSchemeType()) {
                authScheme = AuthSchemeFactory.newScheme(request.realm());
              } else {
                authScheme = AuthSchemeFactory.newScheme(request.realm(), schemeNeeded);
              }
              if (AuthScheme.Type.DIGEST == authScheme.getType()) {
                authScheme.schemeParts(new String[]{parts[1], request.method(), request.uri()});
              }
              request.authorize(request.uri(), authScheme).execute();
              return;
            }
          }
        }
      }
    }
    responseHandler.handle(new DefaultResponse(httpClientResponse));
  }

}
