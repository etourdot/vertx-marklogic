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

import java.util.Map;

import static java.util.Objects.*;
import static java.util.Optional.*;

@DataObject(generateConverter = true)
public class TransformOptions {
  private static final String NAME = "name";

  private String name;
  private JsonObject parameters;

  public TransformOptions() {
  }

  public TransformOptions(TransformOptions transformOptions) {
    this();
    requireNonNull(transformOptions);

    name(transformOptions.getName());
    ofNullable(transformOptions.getParameters()).ifPresent(this::parameters);
  }

  public TransformOptions(JsonObject jsonObject) {
    this();
    requireNonNull(jsonObject);

    name(jsonObject.getString(NAME));
    Map<String, Object> params = jsonObject.getMap();
    params.remove(NAME);
    parameters(new JsonObject(params));
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject(parameters.getMap());
    jsonObject.put(NAME, name);
    return jsonObject;
  }

  /**
   * Name
   */
  public String getName() {
    return name;
  }

  public TransformOptions name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Parameters
   */
  public JsonObject getParameters() {
    return parameters;
  }

  public TransformOptions parameters(JsonObject parameters) {
    this.parameters = parameters;
    return this;
  }

  public boolean hasParameters() {
    return ofNullable(parameters).isPresent();
  }
}
