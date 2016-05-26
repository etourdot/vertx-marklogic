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

import io.netty.handler.codec.http.QueryStringEncoder;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.etourdot.vertx.marklogic.Format;
import org.etourdot.vertx.marklogic.http.AuthScheme;
import org.etourdot.vertx.marklogic.http.MarkLogicRequest;
import org.etourdot.vertx.marklogic.http.MarkLogicResponse;
import org.etourdot.vertx.marklogic.http.impl.AuthSchemeFactory;
import org.etourdot.vertx.marklogic.http.impl.response.ErrorResponse;
import org.etourdot.vertx.marklogic.http.utils.Realm;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultMarklogicRequest implements MarkLogicRequest {
  protected final HttpClient httpClient;
  protected Handler<MarkLogicResponse> responseHandler;
  private String uri;
  protected String body;
  protected String contentType;
  protected String authorization;
  protected long contentLength;
  protected HttpMethod method;
  private Map<String, AuthScheme> authSchemes;
  private Handler<HttpClientResponse> errorHandler;
  protected QueryStringEncoder uriEncoder;
  protected Realm realm;

  private Logger logger = LoggerFactory.getLogger(DefaultMarklogicRequest.class);

  public DefaultMarklogicRequest(HttpClient httpClient, Realm realm) {
    this.realm = realm;
    this.httpClient = httpClient;
    this.contentLength = 0;
    this.authSchemes = new HashMap<>();
  }

  private MarkLogicRequest method(String uri, HttpMethod method) {
    this.uri = uri;
    this.uriEncoder = new QueryStringEncoder(uri);
    this.method = method;
    return this;
  }

  @Override
  public MarkLogicRequest post(String uri) {
    return method(uri, HttpMethod.POST);
  }

  @Override
  public MarkLogicRequest get(String uri) {
    return method(uri, HttpMethod.GET);
  }

  @Override
  public MarkLogicRequest delete(String uri) {
    return method(uri, HttpMethod.DELETE);
  }

  @Override
  public MarkLogicRequest put(String uri) {
    return method(uri, HttpMethod.PUT);
  }

  @Override
  public MarkLogicRequest head(String uri) {
    return method(uri, HttpMethod.HEAD);
  }

  @Override
  public MarkLogicRequest withBody(String body, Format format) {
    return withBody(body, format.getDefaultMimetype());
  }

  @Override
  public MarkLogicRequest withBody(String body, String contentType) {
    this.contentType = contentType;
    this.body = body;
    this.contentLength += body.getBytes(Charset.forName("UTF-8")).length;
    return this;
  }

  @Override
  public MarkLogicRequest withBody(JsonObject jsonObject) {
    return withBody(jsonObject.encode(), Format.JSON);
  }

  @Override
  public MarkLogicRequest withResponseHandler(Handler<MarkLogicResponse> handler) {
    this.responseHandler = handler;
    return this;
  }

  @Override
  public MarkLogicRequest withErrorHandler(Handler<HttpClientResponse> handler) {
    this.errorHandler = handler;
    return this;
  }

  @Override
  public MarkLogicRequest withParams(Map<String, String> params) {
    assert uriEncoder != null;

    for (Map.Entry<String, String> param : params.entrySet()) {
      uriEncoder.addParam(param.getKey(), param.getValue());
    }
    return this;
  }

  @Override
  public MarkLogicRequest addParam(String key, String value) {
    assert uriEncoder != null;

    uriEncoder.addParam(key, value);
    return this;
  }

  @Override
  public MarkLogicRequest addParams(String key, List<String> values) {
    assert uriEncoder != null;

    for (String value : values) {
      uriEncoder.addParam(key, value);
    }
    return this;
  }

  @Override
  public void execute() {
    final HttpClientRequest httpClientRequest = httpClient.request(method, uriEncoder.toString());
    httpClientRequest.handler(new AuthHttpHandler(this, responseHandler));
    httpClientRequest.putHeader(HttpHeaders.ACCEPT, Format.JSON.getDefaultMimetype());
    if (authorization == null && AuthScheme.Type.BASIC == realm.getSchemeType()) {
      authorize(uri, AuthSchemeFactory.newScheme(realm));
    }
    if (authorization != null) {
      httpClientRequest.putHeader(HttpHeaders.AUTHORIZATION, authorization);
    }
    if (body != null) {
      httpClientRequest.putHeader(HttpHeaders.CONTENT_LENGTH, Long.toString(contentLength))
        .putHeader(HttpHeaders.CONTENT_TYPE, contentType)
        .write(body);
    }
    httpClientRequest.exceptionHandler(exception -> {
      responseHandler.handle(new ErrorResponse(exception));
    }).end();
  }

  @Override
  public void execute(Handler<MarkLogicResponse> handler) {
    this.responseHandler = handler;
    execute();
  }

  @Override
  public String body() {
    return body;
  }

  @Override
  public String uri() {
    return uri;
  }

  @Override
  public MarkLogicRequest authorize(String uri, AuthScheme authScheme) {
    this.authorization = authScheme.serialize();
    this.authSchemes.put(uri, authScheme);
    return this;
  }

  @Override
  public String method() {
    return method.name();
  }

  public Realm realm() { return realm; }

  public Map<String, AuthScheme> authSchemes() {
    return authSchemes;
  }

}
