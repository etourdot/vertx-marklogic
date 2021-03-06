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

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.etourdot.vertx.marklogic.client.MarkLogicClient;
import org.etourdot.vertx.marklogic.MarkLogicConfig;
import org.etourdot.vertx.marklogic.http.impl.DefaultRestService;
import org.etourdot.vertx.marklogic.http.MarklogicRestService;
import org.etourdot.vertx.marklogic.model.client.Transformation;
import org.etourdot.vertx.marklogic.model.options.DeleteOptions;
import org.etourdot.vertx.marklogic.model.options.SearchOptions;
import org.etourdot.vertx.marklogic.model.options.TransformOptions;

public class DefaultMarkLogicClient implements MarkLogicClient {
  private final MarklogicRestService restService;

  private final MarkLogicConfigImpl markLogicConfig;
  private final MarkLogicDocumentImpl markLogicDocument;
  private final MarkLogicSearchImpl markLogicSearch;

  public DefaultMarkLogicClient(Vertx vertx, MarkLogicConfig config) {
    this.restService = new DefaultRestService.Builder(vertx).marklogicConfig(config).build();

    this.markLogicConfig = new MarkLogicConfigImpl(vertx, restService);
    this.markLogicDocument = new MarkLogicDocumentImpl(restService);
    this.markLogicSearch = new MarkLogicSearchImpl(restService);
  }

  @Override
  public void availability(Handler<AsyncResult<Void>> resultHandler) {
    restService.isAvalaible(resultHandler);
  }

  @Override
  public void saveTransformation(Transformation transformation, Handler<AsyncResult<String>> resultHandler) {
    markLogicConfig.saveTransformation(transformation, resultHandler);
  }

  @Override
  public void save(JsonObject documents, Handler<AsyncResult<JsonArray>> resultHandler) {
    markLogicDocument.save(documents, resultHandler);
  }

  @Override
  public void saveAndTransform(JsonObject documents, TransformOptions transformOptions, Handler<AsyncResult<JsonArray>> resultHandler) {
    markLogicDocument.saveAndTransform(documents, transformOptions, resultHandler);
  }

  @Override
  public void exists(String docUri, Handler<AsyncResult<JsonObject>> resultHandler) {
    markLogicDocument.exists(docUri, resultHandler);
  }

  @Override
  public void read(String docUri, Handler<AsyncResult<JsonObject>> resultHandler) {
    markLogicDocument.read(docUri, resultHandler);
  }

  @Override
  public void readMetadata(String docUri, Handler<AsyncResult<JsonObject>> resultHandler) {
    markLogicDocument.readMetadata(docUri, resultHandler);
  }

  @Override
  public void readMany(JsonArray docUris, Handler<AsyncResult<JsonArray>> resultHandler) {
    markLogicDocument.readMany(docUris, resultHandler);
  }

  @Override
  public void delete(String docUri, Handler<AsyncResult<String>> resultHandler) {
    markLogicDocument.delete(docUri, resultHandler);
  }

  @Override
  public void search(SearchOptions searchOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
    markLogicSearch.search(searchOptions, resultHandler);
  }

  @Override
  public void searchDocuments(SearchOptions searchOptions, Handler<AsyncResult<JsonArray>> resultHandler) {
    markLogicSearch.searchDocuments(searchOptions, resultHandler);
  }

  @Override
  public void searchBatch(SearchOptions searchOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
    markLogicSearch.searchBatch(searchOptions, resultHandler);
  }

  @Override
  public void deleteDocuments(DeleteOptions deleteOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
    markLogicSearch.delete(deleteOptions, resultHandler);
  }
}
