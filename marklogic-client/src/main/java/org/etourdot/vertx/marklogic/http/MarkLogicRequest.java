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

package org.etourdot.vertx.marklogic.http;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.JsonObject;
import org.etourdot.vertx.marklogic.Format;

import java.util.List;
import java.util.Map;

public interface MarkLogicRequest {
  MarkLogicRequest post(String uri);

  MarkLogicRequest get(String uri);

  MarkLogicRequest delete(String uri);

  MarkLogicRequest put(String uri);

  MarkLogicRequest head(String uri);

  MarkLogicRequest withBody(String body, Format format);

  MarkLogicRequest withBody(String body, String contentType);

  MarkLogicRequest withBody(JsonObject jsonObject);

  MarkLogicRequest withResponseHandler(Handler<MarkLogicResponse> handler);

  MarkLogicRequest withErrorHandler(Handler<HttpClientResponse> handler);

  MarkLogicRequest withParams(Map<String, String> params);

  MarkLogicRequest addParam(String key, String value);

  MarkLogicRequest addParams(String key, List<String> values);

  void execute();

  void execute(Handler<MarkLogicResponse> handler);

  String body();

  String uri();

  MarkLogicRequest authorize(String uri, AuthScheme authScheme);

  String method();
}
