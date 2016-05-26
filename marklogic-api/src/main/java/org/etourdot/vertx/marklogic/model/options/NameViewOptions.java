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
public class NameViewOptions {
  private String name;
  private String id;
  private String view;

  public NameViewOptions() {
  }

  public NameViewOptions(NameViewOptions options) {
    this();
    requireNonNull(options);

    ofNullable((options.getName())).ifPresent(this::name);
    ofNullable((options.getId())).ifPresent(this::id);
    ofNullable((options.getView())).ifPresent(this::view);
  }

  public NameViewOptions(JsonObject jsonObject) {
    this();
    requireNonNull(jsonObject);

    ofNullable(jsonObject.getString("name")).ifPresent(this::name);
    ofNullable(jsonObject.getString("id")).ifPresent(this::id);
    ofNullable(jsonObject.getString("view")).ifPresent(this::view);
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    ofNullable(name).ifPresent(s -> jsonObject.put("name", s));
    ofNullable(id).ifPresent(s -> jsonObject.put("id", s));
    ofNullable(view).ifPresent(s -> jsonObject.put("view", s));
    return jsonObject;
  }

  /**
   * Name
   */
  public String getName() {
    return name;
  }

  public NameViewOptions name(String name) {
    this.name = name;
    return this;
  }

  public Boolean hasName() {
    return ofNullable(name).isPresent();
  }

  /**
   * Id
   */
  public String getId() {
    return id;
  }

  public NameViewOptions id(String id) {
    this.id = id;
    return this;
  }

  public Boolean hasId() {
    return ofNullable(id).isPresent();
  }

  /**
   * View
   */
  public String getView() {
    return view;
  }

  public NameViewOptions view(String view) {
    this.view = view;
    return this;
  }

  public Boolean hasView() {
    return ofNullable(view).isPresent();
  }
}
