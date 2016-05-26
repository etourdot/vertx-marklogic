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

package org.etourdot.vertx.marklogic.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.etourdot.vertx.marklogic.http.impl.DefaultRestService;
import org.etourdot.vertx.marklogic.http.MarkLogicRequest;
import org.etourdot.vertx.marklogic.http.MarklogicRestService;
import org.etourdot.vertx.marklogic.MarkLogicConfig;
import org.etourdot.vertx.marklogic.model.options.RestApiOptions;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

class MarkLogicAdminImpl {

  private static final String REST_APIS = "/v1/rest-apis";
  private static final String INCLUDE = "include";
  private static final String CONTENT = "content";
  private static final String MODULES = "modules";
  private static final String DATABASE = "database";

  private Logger logger = LoggerFactory.getLogger(MarkLogicAdminImpl.class);

  private final MarklogicRestService restService;

  public MarkLogicAdminImpl(Vertx vertx, MarkLogicConfig config) {
    this.restService = new DefaultRestService.Builder(vertx).marklogicConfig(config).build();
  }

  public MarkLogicAdminImpl(MarklogicRestService restService) {
    this.restService = restService;
  }

  void createRESTAppServer(RestApiOptions restApiOption, Handler<AsyncResult<Void>> resultHandler) {
    requireNonNull(restApiOption, "restApiOption cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    MarkLogicRequest marklogicRequest = restService.newMarklogicRequest();
    marklogicRequest.post(REST_APIS)
      .withBody(restApiOption.toJson())
      .execute(response -> {
        if (HttpResponseStatus.CREATED.code() == response.statusCode()) {
          resultHandler.handle(Future.succeededFuture());
        } else {
          response.contentHandler(buffer -> resultHandler.handle(Future.failedFuture(buffer.toJsonObject().encode())));
        }
      });
  }

  void deleteRESTAppServer(RestApiOptions restApiOption, Handler<AsyncResult<String>> resultHandler) {
    requireNonNull(restApiOption, "restApiOption cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    requireNonNull(restApiOption.getRetrieveInstance(), "rettrieve-instance cannot be null");



    String removeServerString = REST_APIS + "/" + restApiOption.getRetrieveInstance();
    MarkLogicRequest marklogicRequest = restService.newMarklogicRequest();
    if (restApiOption.isRemoveContent()) {
      marklogicRequest.addParam(INCLUDE, CONTENT);
    }
    if (restApiOption.isRemoveModules()) {
      marklogicRequest.addParam(INCLUDE, MODULES);
    }
    marklogicRequest.delete(removeServerString)
      .withBody(restApiOption.toJson())
      .execute(response -> {
        if (HttpResponseStatus.ACCEPTED.code() == response.statusCode()) {
          // Todo: is it a convienient return ?
          resultHandler.handle(Future.succeededFuture("deleted"));
        } else {
          resultHandler.handle(Future.failedFuture(response.statusMessage()));
        }
      });
  }

  void getRESTAppServerConfig(RestApiOptions restApiOption, Handler<AsyncResult<JsonObject>> resultHandler) {
    requireNonNull(restApiOption, "restApiOption cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");


    String getServerString = REST_APIS;
    if (restApiOption.hasRetrieveInstance()) {
      getServerString += "/" + restApiOption.getRetrieveInstance();
    }

    MarkLogicRequest marklogicRequest = restService.newMarklogicRequest();
    if (restApiOption.hasRetrieveDatabase()) {
      marklogicRequest.addParam(DATABASE, restApiOption.getRetrieveDatabase());
    }
    marklogicRequest.get(getServerString)
      .execute(response -> {
        if (HttpResponseStatus.OK.code() == response.statusCode()) {
          response.contentHandler(buffer -> resultHandler.handle(Future.succeededFuture(buffer.toJsonObject())));
        } else {
          resultHandler.handle(Future.failedFuture(response.statusMessage()));
        }
      });
  }

  void getSecurityRoles(JsonObject config, Handler<AsyncResult<JsonObject>> resultHandler) {
    requireNonNull(config, "config cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    Map<String, String> params = new HashMap<>();
    MarkLogicRequest marklogicRequest = restService.newMarklogicRequest();
  }

}
