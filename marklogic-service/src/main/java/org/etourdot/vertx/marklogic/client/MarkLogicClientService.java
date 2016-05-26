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

package org.etourdot.vertx.marklogic.client;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ProxyHelper;
import org.etourdot.vertx.marklogic.model.client.Transformation;
import org.etourdot.vertx.marklogic.model.options.DeleteOptions;
import org.etourdot.vertx.marklogic.model.options.SearchOptions;
import org.etourdot.vertx.marklogic.model.options.TransformOptions;

@ProxyGen
public interface MarkLogicClientService extends MarkLogicClient {

  static MarkLogicClientService createEventBusProxy(Vertx vertx, String address) {
    return ProxyHelper.createProxy(MarkLogicClientService.class, vertx, address);
  }

  @Override
  void availability(Handler<AsyncResult<Void>> resultHandler);

  @Override
  void saveTransformation(Transformation transformation, Handler<AsyncResult<String>> resultHandler);

  @Override
  void save(JsonObject documents, Handler<AsyncResult<JsonArray>> resultHandler);

  @Override
  void saveAndTransform(JsonObject documents, TransformOptions transformOptions,
                        Handler<AsyncResult<JsonArray>> resultHandler);

  @Override
  void exists(String docUri, Handler<AsyncResult<JsonObject>> resultHandler);

  @Override
  void read(String docUri, Handler<AsyncResult<JsonObject>> resultHandler);

  @Override
  void readMetadata(String docUri, Handler<AsyncResult<JsonObject>> resultHandler);

  @Override
  void readMany(JsonArray docUris, Handler<AsyncResult<JsonArray>> resultHandler);

  @Override
  void delete(String docUri, Handler<AsyncResult<String>> resultHandler);

  @Override
  void search(SearchOptions searchOptions, Handler<AsyncResult<JsonObject>> resultHandler);

  @Override
  void searchDocuments(SearchOptions searchOptions, Handler<AsyncResult<JsonArray>> resultHandler);

  @Override
  void searchBatch(SearchOptions searchOptions, Handler<AsyncResult<JsonObject>> resultHandler);

  @Override
  void deleteDocuments(DeleteOptions deleteOptions, Handler<AsyncResult<JsonObject>> resultHandler);

}
