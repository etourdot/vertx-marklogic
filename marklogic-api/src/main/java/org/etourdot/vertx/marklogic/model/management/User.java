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
public class User extends AbstractIdNameDesc {
  private String password;
  private JsonArray roles;
  private JsonArray permissions;
  private JsonArray collections;

  public User() {}

  public User(User user) {
    super(user);
    this.password = user.getPassword();
    this.roles = user.getRoles();
    this.permissions = user.getPermissions();
    this.collections = user.getCollections();
  }

  public User(JsonObject jsonObject) {
    super(jsonObject);
    name(jsonObject.getString("user-name"));
    this.password = jsonObject.getString("password");
    this.roles = jsonObject.getJsonArray("role");
    this.permissions = jsonObject.getJsonArray("permission");
    this.collections = jsonObject.getJsonArray("collection");
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("user-name", getName());
    jsonObject.put("description", getDescription());
    jsonObject.put("password", password);
    jsonObject.put("role", roles);
    jsonObject.put("permission", permissions);
    jsonObject.put("collection", collections);
    return jsonObject;
  }

  /**
   * Password
   */
  public String getPassword() {
    return password;
  }

  public User password(String password) {
    this.password = password;
    return this;
  }

  /**
   * List of roles
   */
  public JsonArray getRoles() {
    return roles;
  }

  public User roles(JsonArray roles) {
    this.roles = roles;
    return this;
  }

  /**
   * List of permissions
   */
  public JsonArray getPermissions() {
    return permissions;
  }

  public User permissions(JsonArray permissions) {
    this.permissions = permissions;
    return this;
  }

  /**
   * List of collections
   */
  public JsonArray getCollections() {
    return collections;
  }

  public User collections(JsonArray collections) {
    this.collections = collections;
    return this;
  }
}
