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

package org.etourdot.vertx.marklogic.http.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.etourdot.vertx.marklogic.MarkLogicConfig;
import org.etourdot.vertx.marklogic.http.impl.request.DefaultMarklogicRequest;
import org.etourdot.vertx.marklogic.http.impl.request.DefaultMultiPartRequest;
import org.etourdot.vertx.marklogic.http.MarkLogicRequest;
import org.etourdot.vertx.marklogic.http.MarklogicRestService;
import org.etourdot.vertx.marklogic.http.MultiPartRequest;
import org.etourdot.vertx.marklogic.http.impl.response.ErrorResponse;
import org.etourdot.vertx.marklogic.http.utils.Realm;

public final class DefaultRestService implements MarklogicRestService {
  public static final String PING_URL = "/v1/ping";
  private HttpClient httpClient;
  private Realm realm;

  private Logger logger = LoggerFactory.getLogger(DefaultRestService.class);

  private DefaultRestService() {
  }

  private DefaultRestService(Builder builder) {
    realm = new Realm(builder.authentication, builder.user, builder.password);
    HttpClientOptions httpClientOptions = new HttpClientOptions();
    httpClientOptions.setDefaultHost(builder.host);
    httpClientOptions.setDefaultPort(builder.port);
    httpClientOptions.setKeepAlive(builder.keepAlive);
    this.httpClient = builder.vertx.createHttpClient(httpClientOptions);
  }

  @Override
  public void isAvalaible(Handler<AsyncResult<Void>> resultHandler) {
    newMarklogicRequest().head(PING_URL)
      .execute(response -> {
        if (response instanceof ErrorResponse || HttpResponseStatus.UNAUTHORIZED.code() == response.statusCode()) {
          logger.debug("RestService unavailable !!!");
          resultHandler.handle(Future.failedFuture(response.statusMessage()));
        } else {
          logger.debug("RestService available");
          resultHandler.handle(Future.succeededFuture());
        }
      });
  }

  @Override
  public MarkLogicRequest newMarklogicRequest() {
    return new DefaultMarklogicRequest(httpClient, realm);
  }

  @Override
  public MultiPartRequest newMultipartRequest() {
    return new DefaultMultiPartRequest(httpClient, realm);
  }

  public static final class Builder {
    private String host;
    private int port;
    private String user;
    private String password;
    private boolean keepAlive;
    private String authentication;
    private Vertx vertx;

    public Builder(Vertx vertx) {
      this.vertx = vertx;
    }

    public Builder marklogicConfig(MarkLogicConfig config) {
      this.host = config.getHost();
      this.port = config.getPort();
      this.user = config.getUser();
      this.password = config.getPassword();
      this.authentication = config.getAuthentication();
      return this;
    }

    public Builder host(String host) {
      this.host = host;
      return this;
    }

    public Builder port(int port) {
      this.port = port;
      return this;
    }

    public Builder user(String user) {
      this.user = user;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder keepAlive(boolean keepAlive) {
      this.keepAlive = keepAlive;
      return this;
    }

    public MarklogicRestService build() {
      return new DefaultRestService(this);
    }

    public Builder authentication(String authentication) {
      this.authentication = authentication;
      return this;
    }
  }

}
