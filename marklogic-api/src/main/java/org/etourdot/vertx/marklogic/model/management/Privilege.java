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
public class Privilege extends AbstractIdNameDesc {
  private String action;
  private String kind;
  private JsonArray roles;

  public Privilege() {
    roles = new JsonArray();
  }

  public Privilege(Privilege privilege) {
    super(privilege);
    this.action = privilege.getAction();
    this.kind = privilege.getKind();
    this.roles = privilege.getRoles();
  }

  public Privilege(JsonObject jsonObject) {
    super(jsonObject);
    name(jsonObject.getString("privilege-name"));
    this.action = jsonObject.getString("action");
    this.kind = jsonObject.getString("kind");
    this.roles = jsonObject.getJsonArray("role");
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("privilege-name", getName());
    jsonObject.put("ation", action);
    jsonObject.put("kind", kind);
    jsonObject.put("role", roles);
    return jsonObject;
  }

  /**
   * Action
   */
  public String getAction() {
    return action;
  }

  public Privilege action(String action) {
    this.action = action;
    return this;
  }

  /**
   * Kind
   */
  public String getKind() {
    return kind;
  }

  public Privilege kind(String kind) {
    this.kind = kind;
    return this;
  }

  /**
   * List of roles
   */
  public JsonArray getRoles() {
    return roles;
  }

  public Privilege roles(JsonArray roles) {
    this.roles = roles;
    return this;
  }
}
