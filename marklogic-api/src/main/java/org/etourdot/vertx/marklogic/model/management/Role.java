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

@DataObject
public class Role extends AbstractIdNameDesc {
  private JsonArray roles;
  private JsonArray privileges;
  private JsonArray permissions;
  private JsonArray collections;

  public Role() {
    roles = new JsonArray();
    privileges = new JsonArray();
  }

  public Role(Role role) {
    super(role);
    this.roles = role.getRoles();
    this.privileges = role.getPrivileges();
    this.permissions = role.getPermissions();
    this.collections = role.getCollections();
  }

  public Role(JsonObject jsonObject) {
    super(jsonObject);
    name(jsonObject.getString("role-name"));
    this.roles = jsonObject.getJsonArray("role");
    this.privileges = jsonObject.getJsonArray("privilege");
    this.permissions = jsonObject.getJsonArray("permission");
    this.collections = jsonObject.getJsonArray("collection");
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("role-name", getName());
    jsonObject.put("description", getDescription());
    jsonObject.put("role", roles);
    jsonObject.put("privilege", privileges);
    jsonObject.put("permission", permissions);
    jsonObject.put("collection", collections);
    return jsonObject;
  }

  /**
   * List of roles
   */
  public JsonArray getRoles() {
    return roles;
  }

  public Role roles(JsonArray roles) {
    this.roles = roles;
    return this;
  }

  /**
   * List of privileges
   */
  public JsonArray getPrivileges() {
    return privileges;
  }

  public Role privileges(JsonArray privileges) {
    this.privileges = privileges;
    return this;
  }

  /**
   * List of permissions
   */
  public JsonArray getPermissions() {
    return permissions;
  }

  public Role permissions(JsonArray permissions) {
    this.permissions = permissions;
    return this;
  }

  /**
   * List of collections
   */
  public JsonArray getCollections() {
    return collections;
  }

  public Role collections(JsonArray collections) {
    this.collections = collections;
    return this;
  }
}
