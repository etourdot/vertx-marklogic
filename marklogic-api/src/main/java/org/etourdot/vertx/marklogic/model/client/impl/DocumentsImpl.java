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

package org.etourdot.vertx.marklogic.model.client.impl;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.etourdot.vertx.marklogic.model.client.Document;
import org.etourdot.vertx.marklogic.model.client.Documents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Objects.*;
import static java.util.Optional.*;
import static java.util.stream.Collectors.*;

@DataObject
public class DocumentsImpl implements Documents {
  private static final String DOCUMENTS = "documents";
  private static final String TRANSFORM = "transform";
  private static final String TXID = "txid";
  private static final String FOREST_NAME = "forestName";
  private static final String TEMPORAL_COLLECTION = "temporalCollection";
  private static final String SYSTEM_TIME = "systemTime";
  private static final String CATEGORIES = "categories";

  private final JsonArray documents;
  private String txid;
  private String forestName;
  private String temporalCollection;
  private String systemTime;
  private JsonArray categories;
  private JsonObject transform;

  public DocumentsImpl() {
    this.documents = new JsonArray();
  }

  public DocumentsImpl(DocumentsImpl documents) {
    this();
    requireNonNull(documents);

    documents(documents.getDocuments());
    ofNullable(documents.transform).ifPresent(this::transform);
    ofNullable(documents.forestName).ifPresent(this::forestName);
    ofNullable(documents.temporalCollection).ifPresent(this::temporalCollection);
    ofNullable(documents.systemTime).ifPresent(this::systemTime);
    ofNullable(documents.categories).ifPresent(this::categories);
    ofNullable(documents.txid).ifPresent(this::txid);
  }

  public DocumentsImpl(JsonObject jsonObject) {
    this();
    requireNonNull(jsonObject);
    documents(jsonObject.getJsonArray(DOCUMENTS));
    ofNullable(jsonObject.getJsonObject(TRANSFORM)).ifPresent(this::transform);
    ofNullable(jsonObject.getString(TXID)).ifPresent(this::txid);
    ofNullable(jsonObject.getString(FOREST_NAME)).ifPresent(this::forestName);
    ofNullable(jsonObject.getString(TEMPORAL_COLLECTION)).ifPresent(this::temporalCollection);
    ofNullable(jsonObject.getString(SYSTEM_TIME)).ifPresent(this::systemTime);
    ofNullable(jsonObject.getJsonArray(CATEGORIES)).ifPresent(this::categories);
  }

  @Override
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.put(DOCUMENTS, documents);
    ofNullable(transform).ifPresent(s -> jsonObject.put(TRANSFORM, s));
    ofNullable(txid).ifPresent(s -> jsonObject.put(TXID, s));
    ofNullable(forestName).ifPresent(s -> jsonObject.put(FOREST_NAME, s));
    ofNullable(temporalCollection).ifPresent(s -> jsonObject.put(TEMPORAL_COLLECTION, s));
    ofNullable(systemTime).ifPresent(s -> jsonObject.put(SYSTEM_TIME, s));
    ofNullable(categories).ifPresent(s -> jsonObject.put(CATEGORIES, s));
    return jsonObject;
  }

  /**
   * List of document
   */
  @Override
  public JsonArray getDocuments() {
    return of(documents).get();
  }

  @Override
  public Documents documents(JsonArray documents) {
    boolean isMap = documents.getList().get(0) instanceof Map;
    documents.getList()
      .stream()
      .map(d1 -> Document.create(isMap ? new JsonObject((Map) d1) : (JsonObject) d1))
      .forEach(d2 -> addDocuments((Document) d2));
    return this;
  }

  /**
   * Transform option
   */
  @Override
  public JsonObject getTransform() {
    return ofNullable(transform).get();
  }

  @Override
  public Documents transform(JsonObject transform) {
    this.transform = transform;
    return this;
  }

  @Override
  public boolean hasTransform() {
    return ofNullable(transform).isPresent();
  }

  /**
   * Transaction Id
   */
  @Override
  public String getTxid() {
    return ofNullable(txid).get();
  }

  @Override
  public Documents txid(String txid) {
    this.txid = txid;
    return this;
  }

  @Override
  public boolean hasTxid() {
    return ofNullable(txid).isPresent();
  }

  /**
   * Forest Name
   */
  @Override
  public String getForestName() {
    return ofNullable(forestName).get();
  }

  @Override
  public Documents forestName(String forestName) {
    this.forestName = forestName;
    return this;
  }

  @Override
  public boolean hasForestName() {
    return ofNullable(forestName).isPresent();
  }

  /**
   * Temporal collection
   */
  @Override
  public String getTemporalCollection() {
    return ofNullable(temporalCollection).get();
  }

  @Override
  public Documents temporalCollection(String temporalCollection) {
    this.temporalCollection = temporalCollection;
    return this;
  }

  @Override
  public boolean hasTemporalCollection() {
    return ofNullable(temporalCollection).isPresent();
  }

  /**
   * System time
   */
  @Override
  public String getSystemTime() {
    return ofNullable(systemTime).get();
  }

  @Override
  public Documents systemTime(String systemTime) {
    this.systemTime = systemTime;
    return this;
  }

  @Override
  public boolean hasSystemTime() {
    return ofNullable(systemTime).isPresent();
  }

  /**
   * List of category
   */
  @Override
  public JsonArray getCategories() {
    return ofNullable(categories).get();
  }

  @Override
  public Documents categories(JsonArray categories) {
    this.categories = categories;
    return this;
  }

  @Override
  public boolean hasCategories() {
    return ofNullable(categories).isPresent();
  }

  @Override
  public List<Document> getDocumentsList() {
    return documents.stream()
      .map(d -> Document.create((JsonObject) d))
      .collect(toCollection(ArrayList<Document>::new));
  }

  @Override
  public Documents addDocuments(Document... documents) {
    Arrays.asList(documents).stream().forEach(d -> this.documents.add(d.toJson()));
    return this;
  }
}
