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
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.etourdot.vertx.marklogic.http.impl.DefaultRestService;
import org.etourdot.vertx.marklogic.http.MarkLogicRequest;
import org.etourdot.vertx.marklogic.http.MarklogicRestService;
import org.etourdot.vertx.marklogic.MarkLogicConfig;
import org.etourdot.vertx.marklogic.model.client.Transformation;

import static java.util.Objects.requireNonNull;

class MarkLogicConfigImpl {

  private static final String TRANSFORMS_URL = "/v1/config/transforms";
  private static final String TITLE = "title";
  private static final String PROVIDER = "provider";
  private static final String VERSION = "version";
  private static final String DESCRIPTION = "description";
  private static final String FORMAT = "format";

  private Logger logger = LoggerFactory.getLogger(MarkLogicConfigImpl.class);

  private final FileSystem fileSystem;
  private final MarklogicRestService restService;

  public MarkLogicConfigImpl(Vertx vertx, MarkLogicConfig config) {
    this.fileSystem = vertx.fileSystem();
    this.restService = new DefaultRestService.Builder(vertx).marklogicConfig(config).build();
  }

  public MarkLogicConfigImpl(Vertx vertx, MarklogicRestService restService) {
    this.fileSystem = vertx.fileSystem();
    this.restService = restService;
  }

  void saveTransformation(Transformation transformation, Handler<AsyncResult<String>> resultHandler) {
    requireNonNull(transformation, "config cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    MarkLogicRequest marklogicRequest = restService.newMarklogicRequest();
    marklogicRequest.put(TRANSFORMS_URL + "/" + transformation.getName());
    if (transformation.hasTitle()) {
      marklogicRequest.addParam(TITLE, transformation.getTitle());
    }
    if (transformation.hasProvider()) {
      marklogicRequest.addParam(PROVIDER, transformation.getProvider());
    }
    if (transformation.hasVersion()) {
      marklogicRequest.addParam(VERSION, transformation.getVersion());
    }
    if (transformation.hasDescription()) {
      marklogicRequest.addParam(DESCRIPTION, transformation.getDescription());
    }
    marklogicRequest.addParam(FORMAT, transformation.getFormat());
    final String contentType = ("xslt".equals(transformation.getFormat())) ? "application/xslt+xml" :
      ("xquery".equals(transformation.getFormat())) ? "application/xquery" : "application/vnd.io.vertx.ext.marklogic-javascript";
    final Buffer buffer = fileSystem.readFileBlocking(transformation.getSource());
    marklogicRequest.withBody(buffer.toString(), contentType)
      .execute(response -> {
        if (HttpResponseStatus.NO_CONTENT.code() == response.statusCode()) {
          // Todo: better return ?
          resultHandler.handle(Future.succeededFuture("ok"));
        }
      });
  }
}
