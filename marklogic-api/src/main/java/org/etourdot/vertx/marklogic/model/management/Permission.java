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

package org.etourdot.vertx.marklogic.model.management;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import static java.util.Objects.*;
import static java.util.Optional.*;

@DataObject
public class Permission {
  private static final String ROLE_NAME = "role-name";
  private static final String CAPABILITIES = "capabilities";

  private String roleName;
  private JsonArray capabilities;

  public Permission() {
  }

  public Permission(Permission permission) {
    requireNonNull(permission);

    ofNullable(permission.getRoleName()).ifPresent(this::roleName);
    ofNullable(permission.getCapabilities()).ifPresent(this::capabilities);
  }

  public Permission(JsonObject jsonObject) {
    requireNonNull(jsonObject);

    ofNullable(jsonObject.getString(ROLE_NAME)).ifPresent(this::roleName);
    ofNullable(jsonObject.getJsonArray(CAPABILITIES)).ifPresent(this::capabilities);
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    ofNullable(roleName).ifPresent(s -> jsonObject.put(ROLE_NAME, s));
    ofNullable(capabilities).ifPresent(s -> jsonObject.put(CAPABILITIES, s));
    return jsonObject;
  }

  /**
   * Role name
   */
  public String getRoleName() {
    return ofNullable(roleName).get();
  }

  public Permission roleName(String roleName) {
    this.roleName = roleName;
    return this;
  }

  public boolean hasRoleName() {
    return ofNullable(roleName).isPresent();
  }

  /**
   * List of capabilities
   */
  public JsonArray getCapabilities() {
    return ofNullable(capabilities).get();
  }

  public Permission capabilities(JsonArray capabilities) {
    this.capabilities = capabilities;
    return this;
  }

  public boolean hasCapabilities() {
    return ofNullable(capabilities).isPresent();
  }

}
