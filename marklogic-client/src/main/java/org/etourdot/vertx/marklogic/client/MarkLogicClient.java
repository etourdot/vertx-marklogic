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

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.etourdot.vertx.marklogic.MarkLogicAvailability;
import org.etourdot.vertx.marklogic.impl.DefaultMarkLogicClient;
import org.etourdot.vertx.marklogic.MarkLogicConfig;
import org.etourdot.vertx.marklogic.model.client.Transformation;
import org.etourdot.vertx.marklogic.model.options.DeleteOptions;
import org.etourdot.vertx.marklogic.model.options.SearchOptions;
import org.etourdot.vertx.marklogic.model.options.TransformOptions;

@VertxGen
public interface MarkLogicClient extends MarkLogicAvailability {

  static MarkLogicClient create(Vertx vertx, MarkLogicConfig config) {
    return new DefaultMarkLogicClient(vertx, config);
  }

  /**
   * Configure transformation on MarkLogic server.
   * TODO: unclear name and options => change this
   *
   * @param transformation Transformation to save to server.
   * @param resultHandler handler will provide "ok" if success.
   */
  void saveTransformation(Transformation transformation, Handler<AsyncResult<String>> resultHandler);

  /**
   * Save one or more documents with params.
   *
   * @param documents {@link org.etourdot.vertx.marklogic.model.client.Document} or {@link org.etourdot.vertx.marklogic.model.client.Documents} to save.
   * @param resultHandler handler will provide array of saved documents uris.
   */
  void save(JsonObject documents, Handler<AsyncResult<JsonArray>> resultHandler);

  /**
   * Save and transform one or more documents with params.
   *
   * @param documents Document or Documents to save.
   * @param transformOptions Transformation options to apply to documents before saving.
   * @param resultHandler handler will provide array of saved documents uris.
   */
  void saveAndTransform(JsonObject documents, TransformOptions transformOptions,
                        Handler<AsyncResult<JsonArray>> resultHandler);

  /**
   * Check if a document exists in database.
   *
   * @param docUri Document's uri to check if exists.
   * @param resultHandler will provide {@link org.etourdot.vertx.marklogic.model.client.Document} skeleton with uri and format or null if document not found.
   */
  void exists(String docUri, Handler<AsyncResult<JsonObject>> resultHandler);

  /**
   * Read a document from database
   *
   * @param docUri uri of document to read.
   * @param resultHandler will provide {@link org.etourdot.vertx.marklogic.model.client.Document} or null if document not found.
   */
  void read(String docUri, Handler<AsyncResult<JsonObject>> resultHandler);

  /**
   * Read only metadata of a document
   *
   * @param docUri uri of document metadata to read.
   * @param resultHandler will provide {@link org.etourdot.vertx.marklogic.model.client.Document} skeleton with uri and format or null if document not found.
   */
  void readMetadata(String docUri, Handler<AsyncResult<JsonObject>> resultHandler);

  /**
   * Read a list of documents from database and return an array of documents.
   * NB: if one document doesnt exists, method return exception.
   *
   * @param docUris array of uri of documents to read.
   * @param resultHandler will provide a {@link org.etourdot.vertx.marklogic.model.client.Document} array.
   */
  void readMany(JsonArray docUris, Handler<AsyncResult<JsonArray>> resultHandler);

  /**
   * Delete a document into database.
   *
   * @param docUri uri of document to delete.
   * @param resultHandler will provide uri of deleted document.
   */
  void delete(String docUri, Handler<AsyncResult<String>> resultHandler);

  /**
   * Search documents in database.
   *
   * @param searchOptions {@link SearchOptions} criteria.
   * @param resultHandler will provide found json snippet.
   */
  void search(SearchOptions searchOptions, Handler<AsyncResult<JsonObject>> resultHandler);

  /**
   * Search documents in database.
   * Return all documents found limit to searchOptions.pagelen.
   *
   * @param searchOptions {@link SearchOptions} criteria.
   * @param resultHandler will provide a {@link org.etourdot.vertx.marklogic.model.client.Document} array.
   */
  void searchDocuments(SearchOptions searchOptions, Handler<AsyncResult<JsonArray>> resultHandler);

  /**
   * Search documents in database in batch mode, meaning each document found will be send to handler
   * without regarding pagelen option.
   *
   * @param searchOptions {@link SearchOptions} options criteria.
   * @param resultHandler will provide individual {@link org.etourdot.vertx.marklogic.model.client.Document}.
   */
  void searchBatch(SearchOptions searchOptions, Handler<AsyncResult<JsonObject>> resultHandler);

  /**
   * Delete documents in database. Documents respond to a search.
   *
   * @param deleteOptions {@link DeleteOptions}
   * @param resultHandler will provide null if success or failure message.
   */
  void deleteDocuments(DeleteOptions deleteOptions, Handler<AsyncResult<JsonObject>> resultHandler);
}
