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

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import static java.util.Objects.*;
import static java.util.Optional.*;

@DataObject
public class Transformation {
  private static final String NAME = "name";
  private static final String FORMAT = "format";
  private static final String SOURCE = "source";
  private static final String TITLE = "title";
  private static final String DESCRIPTION = "description";
  private static final String PROVIDER = "provider";
  private static final String VERSION = "version";

  public static final String XSLT_MIMETYPE = "application/xslt+xml";
  public static final String XQUERY_MIMETYPE = "application/xquery";
  public static final String JAVASCRIPT_MIMETYPE = "application/javascript";

  private String name;
  private String format;
  private String source;
  private String title;
  private String description;
  private String provider;
  private String version;

  public Transformation() {
  }

  public Transformation(Transformation transformation) {
    this();
    requireNonNull(transformation);

    name(transformation.getName());
    source(transformation.getSource());
    ofNullable(transformation.getDescription()).ifPresent(this::description);
    ofNullable(transformation.getFormat()).ifPresent(this::format);
    ofNullable(transformation.getProvider()).ifPresent(this::provider);
    ofNullable(transformation.getTitle()).ifPresent(this::title);
    ofNullable(transformation.getVersion()).ifPresent(this::version);
  }

  public Transformation(JsonObject jsonObject) {
    this();
    requireNonNull(jsonObject);

    name(jsonObject.getString(NAME));
    source(jsonObject.getString(SOURCE));
    ofNullable(jsonObject.getString(DESCRIPTION)).ifPresent(this::description);
    ofNullable(jsonObject.getString(FORMAT)).ifPresent(this::format);
    ofNullable(jsonObject.getString(PROVIDER)).ifPresent(this::provider);
    ofNullable(jsonObject.getString(TITLE)).ifPresent(this::title);
    ofNullable(jsonObject.getString(VERSION)).ifPresent(this::version);
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.put(NAME, name);
    jsonObject.put(SOURCE, source);
    ofNullable(description).ifPresent(s -> jsonObject.put(DESCRIPTION, s));
    ofNullable(format).ifPresent(s -> jsonObject.put(FORMAT, s));
    ofNullable(provider).ifPresent(s -> jsonObject.put(PROVIDER, s));
    ofNullable(title).ifPresent(s -> jsonObject.put(TITLE, s));
    ofNullable(version).ifPresent(s -> jsonObject.put(VERSION, s));
    return jsonObject;
  }

  /**
   * Transformation name
   */
  public String getName() {
    return name;
  }

  public Transformation name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Transformation format (default: "application/xslt+xml")
   */
  public String getFormat() {
    return ofNullable(format).orElse(XSLT_MIMETYPE);
  }

  public Transformation format(String format) {
    this.format = format;
    return this;
  }

  /**
   * Source
   */
  public String getSource() {
    return source;
  }

  public Transformation source(String source) {
    this.source = source;
    return this;
  }

  /**
   * Title
   */
  public String getTitle() {
    return ofNullable(title).get();
  }

  public Transformation title(String title) {
    this.title = title;
    return this;
  }

  public boolean hasTitle() {
    return ofNullable(title).isPresent();
  }

  /**
   * Description
   */
  public String getDescription() {
    return ofNullable(description).get();
  }

  public Transformation description(String description) {
    this.description = description;
    return this;
  }

  public boolean hasDescription() {
    return ofNullable(description).isPresent();
  }

  /**
   * Provider
   */
  public String getProvider() {
    return ofNullable(provider).get();
  }

  public Transformation provider(String provider) {
    this.provider = provider;
    return this;
  }

  public boolean hasProvider() {
    return ofNullable(provider).isPresent();
  }

  /**
   * Version
   */
  public String getVersion() {
    return ofNullable(version).get();
  }

  public Transformation version(String version) {
    this.version = version;
    return this;
  }

  public boolean hasVersion() {
    return ofNullable(version).isPresent();
  }

  public boolean hasMetas() {
    return hasTitle() || hasDescription() || hasProvider() || hasVersion();
  }
}
