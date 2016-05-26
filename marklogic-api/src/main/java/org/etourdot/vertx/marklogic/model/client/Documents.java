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

package org.etourdot.vertx.marklogic.model.client;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.etourdot.vertx.marklogic.model.client.impl.DocumentsImpl;

import java.util.List;

@VertxGen
public interface Documents {
  static Documents create() {
    return new DocumentsImpl();
  }

  static Documents create(JsonObject jsonObject) {
    return new DocumentsImpl(jsonObject);
  }

  /**
   * Convert document to {@link JsonObject}.
   *
   * @return json representation.
   */
  JsonObject toJson();

  /**
   * Get documents array
   *
   * @return array of {@link Document}
   */
  JsonArray getDocuments();

  /**
   * Set documents
   *
   * @param documents array of {@link Document}
   * @return {@link Documents}
   */
  @Fluent
  Documents documents(JsonArray documents);

  /**
   * Get transform criteria.
   *
   * @return
   */
  JsonObject getTransform();

  /**
   * Set transform criteria.
   *
   * @param transform
   * @return
   */
  @Fluent
  Documents transform(JsonObject transform);

  /**
   * Check if there is transform criteria.
   *
   * @return true or false.
   */
  boolean hasTransform();

  /**
   * Get transaction id
   *
   * @return transaction id.
   */
  String getTxid();

  /**
   * Set transaction id.
   *
   * @param txid
   * @return {@link Documents}
   */
  @Fluent
  Documents txid(String txid);

  boolean hasTxid();

  /**
   * Get forest name.
   *
   * @return forest name.
   */
  String getForestName();

  /**
   * Set forest name.
   *
   * @param forestName
   * @return {@link Documents}
   */
  @Fluent
  Documents forestName(String forestName);

  boolean hasForestName();

  String getTemporalCollection();

  @Fluent
  Documents temporalCollection(String temporalCollection);

  boolean hasTemporalCollection();

  String getSystemTime();

  @Fluent
  Documents systemTime(String systemTime);

  boolean hasSystemTime();

  JsonArray getCategories();

  @Fluent
  Documents categories(JsonArray categories);

  boolean hasCategories();

  @GenIgnore
  List<Document> getDocumentsList();

  @GenIgnore
  Documents addDocuments(Document... documents);
}
