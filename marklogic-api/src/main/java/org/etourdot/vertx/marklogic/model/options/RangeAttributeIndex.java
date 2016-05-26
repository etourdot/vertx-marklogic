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

@DataObject
public class RangeAttributeIndex extends RangeElementIndex {
  private String attributeNamespaceUri;
  private String attributeLocalname;

  public RangeAttributeIndex() {
  }

  public RangeAttributeIndex(RangeAttributeIndex rangeAttributeIndex) {
    requireNonNull(rangeAttributeIndex);

    scalarType(rangeAttributeIndex.getScalarType());
    ofNullable(rangeAttributeIndex.getNamespaceUri()).ifPresent(this::namespaceUri);
    localname(rangeAttributeIndex.getLocalname());
    ofNullable(rangeAttributeIndex.getAttributeNamespaceUri()).ifPresent(this::attributeNamespaceUri);
    attributeLocalname(rangeAttributeIndex.getAttributeLocalname());
    ofNullable(rangeAttributeIndex.getCollation()).ifPresent(this::collation);
    ofNullable(rangeAttributeIndex.getRangeValuePositions()).ifPresent(this::rangeValuePositions);
  }

  public RangeAttributeIndex(JsonObject jsonObject) {
    requireNonNull(jsonObject);

    scalarType(jsonObject.getString("scalar-type"));
    ofNullable(jsonObject.getString("parent-namespace-uri")).ifPresent(this::namespaceUri);
    localname(jsonObject.getString("parent-localname"));
    ofNullable(jsonObject.getString("collation")).ifPresent(this::collation);
    ofNullable(jsonObject.getBoolean("range-value-positions")).ifPresent(this::rangeValuePositions);
  }

  public RangeAttributeIndex(String scalarType, String namespaceUri, String localname, String attributeNamespaceUri,
                             String attributeLocalname, String collation, Boolean rangeValuePositions) {
    super(scalarType, namespaceUri, localname, collation, rangeValuePositions);
    this.attributeNamespaceUri = attributeNamespaceUri;
    this.attributeLocalname = attributeLocalname;
  }

  public RangeAttributeIndex(String scalarType, String namespaceUri, String localname, String attributeNamespaceUri,
                             String attributeLocalname, Boolean rangeValuePositions) {
    super(scalarType, namespaceUri, localname, rangeValuePositions);
    this.attributeNamespaceUri = attributeNamespaceUri;
    this.attributeLocalname = attributeLocalname;
  }

  public RangeAttributeIndex(String scalarType, String localname, String attributeLocalname, Boolean rangeValuePositions) {
    super(scalarType, localname, rangeValuePositions);
    this.attributeLocalname = attributeLocalname;
  }

  public RangeAttributeIndex(String scalarType, String namespaceUri, String localname, String attributeNamespaceUri,
                             String attributeLocalname) {
    super(scalarType, namespaceUri, localname);
    this.attributeNamespaceUri = attributeNamespaceUri;
    this.attributeLocalname = attributeLocalname;
  }

  public RangeAttributeIndex(String scalarType, String localname, String attributeLocalname) {
    super(scalarType, localname);
    this.attributeLocalname = attributeLocalname;
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("scalar-type", getScalarType());
    jsonObject.put("parent-namespace-uri", ofNullable(getNamespaceUri()).orElse(""));
    jsonObject.put("parent-localname", getLocalname());
    jsonObject.put("namespace-uri", ofNullable(attributeNamespaceUri).orElse(""));
    jsonObject.put("localname", attributeLocalname);
    jsonObject.put("collation", ofNullable(getCollation()).orElse(DEFAULT_COLLATION));
    jsonObject.put("range-value-positions", ofNullable(getRangeValuePositions()).orElse(Boolean.FALSE));
    return jsonObject;
  }

  /**
   * Attribute namespace uri
   */
  public String getAttributeNamespaceUri() {
    return attributeNamespaceUri;
  }

  public RangeElementIndex attributeNamespaceUri(String attributeNamespaceUri) {
    this.attributeNamespaceUri = attributeNamespaceUri;
    return this;
  }

  /**
   * Attribute localname
   */
  public String getAttributeLocalname() {
    return attributeLocalname;
  }

  public RangeElementIndex attributeLocalname(String attributeLocalname) {
    this.attributeLocalname = attributeLocalname;
    return this;
  }


}
