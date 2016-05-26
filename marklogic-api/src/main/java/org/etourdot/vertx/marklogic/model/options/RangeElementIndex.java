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

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

@DataObject
public class RangeElementIndex {
  public static final String DEFAULT_COLLATION = "http://io.vertx.ext.marklogic.com/collation/";

  private String scalarType;
  private String namespaceUri;
  private String localname;
  private String collation;
  private Boolean rangeValuePositions;

  public RangeElementIndex() {
  }

  public RangeElementIndex(RangeElementIndex rangeElementIndex) {
    requireNonNull(rangeElementIndex);

    scalarType(rangeElementIndex.getScalarType());
    ofNullable(rangeElementIndex.getNamespaceUri()).ifPresent(this::namespaceUri);
    localname(rangeElementIndex.getLocalname());
    ofNullable(rangeElementIndex.getCollation()).ifPresent(this::collation);
    ofNullable(rangeElementIndex.getRangeValuePositions()).ifPresent(this::rangeValuePositions);
  }

  public RangeElementIndex(JsonObject jsonObject) {
    requireNonNull(jsonObject);

    scalarType(jsonObject.getString("scalar-type"));
    ofNullable(jsonObject.getString("namespace-uri")).ifPresent(this::namespaceUri);
    localname(jsonObject.getString("localname"));
    ofNullable(jsonObject.getString("collation")).ifPresent(this::collation);
    ofNullable(jsonObject.getBoolean("range-value-positions")).ifPresent(this::rangeValuePositions);
  }

  public RangeElementIndex(String scalarType, String namespaceUri, String localname, String collation, Boolean rangeValuePositions) {
    this.scalarType = scalarType;
    this.namespaceUri = namespaceUri;
    this.localname = localname;
    this.collation = collation;
    this.rangeValuePositions = rangeValuePositions;
  }

  public RangeElementIndex(String scalarType, String namespaceUri, String localname, Boolean rangeValuePositions) {
    this.scalarType = scalarType;
    this.namespaceUri = namespaceUri;
    this.localname = localname;
    this.rangeValuePositions = rangeValuePositions;
  }

  public RangeElementIndex(String scalarType, String localname, Boolean rangeValuePositions) {
    this.scalarType = scalarType;
    this.localname = localname;
    this.rangeValuePositions = rangeValuePositions;
  }

  public RangeElementIndex(String scalarType, String namespaceUri, String localname) {
    this.scalarType = scalarType;
    this.namespaceUri = namespaceUri;
    this.localname = localname;
  }

  public RangeElementIndex(String scalarType, String localname) {
    this.scalarType = scalarType;
    this.localname = localname;
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("scalar-type", scalarType);
    jsonObject.put("namespace-uri", ofNullable(namespaceUri).orElse(""));
    jsonObject.put("localname", localname);
    jsonObject.put("collation", ofNullable(collation).orElse(DEFAULT_COLLATION));
    jsonObject.put("range-value-positions", ofNullable(rangeValuePositions).orElse(Boolean.FALSE));
    return jsonObject;
  }

  /**
   * Scalar type
   */
  public String getScalarType() {
    return scalarType;
  }

  public RangeElementIndex scalarType(String scalarType) {
    this.scalarType = scalarType;
    return this;
  }

  /**
   * Namespace uri
   */
  public String getNamespaceUri() {
    return namespaceUri;
  }

  public RangeElementIndex namespaceUri(String namespaceUri) {
    this.namespaceUri = namespaceUri;
    return this;
  }

  /**
   * Localname
   */
  public String getLocalname() {
    return localname;
  }

  public RangeElementIndex localname(String localname) {
    this.localname = localname;
    return this;
  }

  /**
   * Collation
   */
  public String getCollation() {
    return collation;
  }

  public RangeElementIndex collation(String collation) {
    this.collation = collation;
    return this;
  }

  /**
   * Range value positions
   */
  public Boolean getRangeValuePositions() {
    return rangeValuePositions;
  }

  public RangeElementIndex rangeValuePositions(Boolean rangeValuePositions) {
    this.rangeValuePositions = rangeValuePositions;
    return this;
  }
}
