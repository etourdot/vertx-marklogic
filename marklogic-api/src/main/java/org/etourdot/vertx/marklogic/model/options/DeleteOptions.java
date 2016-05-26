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

package org.etourdot.vertx.marklogic.model.options;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import static java.util.Objects.*;
import static java.util.Optional.*;

@DataObject(generateConverter = true)
public class DeleteOptions {
  private String collection;
  private String directory;
  private String txId;

  public DeleteOptions() {
  }

  public DeleteOptions(DeleteOptions deleteOptions) {
    this();
    requireNonNull(deleteOptions);

    ofNullable(deleteOptions.getCollection()).ifPresent(this::collection);
    ofNullable(deleteOptions.getDirectory()).ifPresent(this::directory);
    ofNullable(deleteOptions.getTxId()).ifPresent(this::txId);
  }

  public DeleteOptions(JsonObject jsonObject) {
    this();
    requireNonNull(jsonObject);

    ofNullable(jsonObject.getString("collection")).ifPresent(this::collection);
    ofNullable(jsonObject.getString("directory")).ifPresent(this::directory);
    ofNullable(jsonObject.getString("txid")).ifPresent(this::txId);
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    ofNullable(collection).ifPresent(s -> jsonObject.put("collection", s));
    ofNullable(directory).ifPresent(s -> jsonObject.put("directory", s));
    ofNullable(txId).ifPresent(s -> jsonObject.put("txid", s));
    return jsonObject;
  }

  /**
   * Collection
   */
  public String getCollection() {
    return ofNullable(collection).get();
  }

  public DeleteOptions collection(String collection) {
    this.collection = collection;
    return this;
  }

  public boolean hasCollection() {
    return ofNullable(collection).isPresent();
  }

  /**
   * Directory
   */
  public String getDirectory() {
    return ofNullable(directory).get();
  }

  public DeleteOptions directory(String directory) {
    this.directory = directory;
    return this;
  }

  public boolean hasDirectory() {
    return ofNullable(directory).isPresent();
  }

  /**
   * Transaction Id
   * @return
   */
  public String getTxId() {
    return txId;
  }

  public DeleteOptions txId(String txId) {
    this.txId = txId;
    return this;
  }

  public boolean hasTxId() {
    return ofNullable(txId).isPresent();
  }
}
