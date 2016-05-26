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
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import static java.util.Objects.*;
import static java.util.Optional.*;

@DataObject(generateConverter = true)
public class SearchOptions {
  private static final String CATEGORIES = "categories";
  private static final String COLLECTIONS = "collections";
  private static final String DIRECTORY = "directory";
  private static final String QBE = "qbe";
  private static final String START = "start";
  private static final String PAGELEN = "pagelen";
  private static final String VIEW = "view";
  private static final String EXPRESSION = "expression";
  private static final String STRUCT_QUERY = "structuredQuery";
  private JsonArray categories;
  private JsonArray collections;
  private String directory;
  private JsonObject qbe;
  private JsonObject structuredQuery;
  private Long start;
  private Long pageLen;
  private String view;
  private String expression;

  public SearchOptions() {
  }

  public SearchOptions(SearchOptions searchOptions) {
    this();
    requireNonNull(searchOptions);

    ofNullable(searchOptions.getCategories()).ifPresent(this::categories);
    ofNullable(searchOptions.getCollections()).ifPresent(this::collections);
    ofNullable(searchOptions.getDirectory()).ifPresent(this::directory);
    ofNullable(searchOptions.getStart()).ifPresent(this::start);
    ofNullable(searchOptions.getPageLen()).ifPresent(this::pageLen);
    ofNullable(searchOptions.getView()).ifPresent(this::view);
    ofNullable(searchOptions.getExpression()).ifPresent(this::expression);
    ofNullable(searchOptions.getQbe()).ifPresent(this::qbe);
    ofNullable(searchOptions.getStructuredQuery()).ifPresent(this::structuredQuery);
  }

  public SearchOptions(JsonObject jsonObject) {
    this();
    requireNonNull(jsonObject);

    ofNullable(jsonObject.getJsonArray(CATEGORIES)).ifPresent(this::categories);
    ofNullable(jsonObject.getJsonArray(COLLECTIONS)).ifPresent(this::collections);
    ofNullable(jsonObject.getString(DIRECTORY)).ifPresent(this::directory);
    ofNullable(jsonObject.getLong(START)).ifPresent(this::start);
    ofNullable(jsonObject.getLong(PAGELEN)).ifPresent(this::pageLen);
    ofNullable(jsonObject.getString(VIEW)).ifPresent(this::view);
    ofNullable(jsonObject.getString(EXPRESSION)).ifPresent(this::expression);
    ofNullable(jsonObject.getJsonObject(QBE)).ifPresent(this::qbe);
    ofNullable(jsonObject.getJsonObject(STRUCT_QUERY)).ifPresent(this::structuredQuery);
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    ofNullable(categories).ifPresent(a -> jsonObject.put(CATEGORIES, a));
    ofNullable(collections).ifPresent(a -> jsonObject.put(COLLECTIONS, a));
    ofNullable(directory).ifPresent(s -> jsonObject.put(DIRECTORY, s));
    ofNullable(start).ifPresent(l -> jsonObject.put(START, l));
    ofNullable(pageLen).ifPresent(l -> jsonObject.put(PAGELEN, l));
    ofNullable(view).ifPresent(s -> jsonObject.put(VIEW, s));
    ofNullable(expression).ifPresent(s -> jsonObject.put(EXPRESSION, s));
    ofNullable(qbe).ifPresent(o -> jsonObject.put(QBE, o));
    ofNullable(structuredQuery).ifPresent(o -> jsonObject.put(STRUCT_QUERY, o));
    return jsonObject;
  }

  /**
   * List of categories
   */
  public JsonArray getCategories() {
    return categories;
  }

  public SearchOptions categories(JsonArray categories) {
    this.categories = categories;
    return this;
  }

  public boolean hasCategories() {
    return ofNullable(categories).isPresent();
  }

  /**
   * List of collections
   */
  public JsonArray getCollections() {
    return collections;
  }

  public SearchOptions collections(JsonArray collections) {
    this.collections = collections;
    return this;
  }

  public boolean hasCollections() {
    return ofNullable(collections).isPresent();
  }

  /**
   * Directory
   */
  public String getDirectory() {
    return directory;
  }

  public SearchOptions directory(String directory) {
    this.directory = directory;
    return this;
  }

  public boolean hasDirectory() {
    return ofNullable(directory).isPresent();
  }

  /**
   * QBE
   */
  public JsonObject getQbe() {
    return qbe;
  }

  public SearchOptions qbe(JsonObject qbe) {
    this.qbe = qbe;
    return this;
  }

  public boolean hasQbe() {
    return ofNullable(qbe).isPresent();
  }

  /**
   * Structures query
   */
  public JsonObject getStructuredQuery() {
    return structuredQuery;
  }

  public SearchOptions structuredQuery(JsonObject structuredQuery) {
    this.structuredQuery = structuredQuery;
    return this;
  }

  public boolean hasStructuredQuery() {
    return ofNullable(structuredQuery).isPresent();
  }

  /**
   * Start position
   */
  public Long getStart() {
    return start;
  }

  public SearchOptions start(Long start) {
    this.start = start;
    return this;
  }

  public boolean hasStart() {
    return ofNullable(start).isPresent();
  }

  /**
   * Page length
   */
  public Long getPageLen() {
    return pageLen;
  }

  public SearchOptions pageLen(Long pageLen) {
    this.pageLen = pageLen;
    return this;
  }

  public boolean hasPageLen() {
    return ofNullable(pageLen).isPresent();
  }

  /**
   * View
   */
  public String getView() {
    return view;
  }

  public SearchOptions view(String view) {
    this.view = view;
    return this;
  }

  public boolean hasView() {
    return ofNullable(view).isPresent();
  }

  /**
   * Search simple expression
   */
  public String getExpression() {
    return expression;
  }

  public SearchOptions expression(String expression) {
    this.expression = expression;
    return this;
  }

  public boolean hasExpression() {
    return ofNullable(expression).isPresent();
  }
}
