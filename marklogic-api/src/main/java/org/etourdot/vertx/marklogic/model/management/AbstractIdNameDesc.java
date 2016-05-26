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
import io.vertx.core.json.JsonObject;

@DataObject
abstract class AbstractIdNameDesc implements IdNameDesc {
  private String id;
  private String name;
  private String description;

  public AbstractIdNameDesc() {
  }

  public AbstractIdNameDesc(IdNameDesc idNameDesc) {
    this.id = idNameDesc.getId();
    this.name = idNameDesc.getName();
    this.description = idNameDesc.getDescription();
  }

  public AbstractIdNameDesc(JsonObject jsonObject) {
    this.id = jsonObject.getString("id");
    this.name = jsonObject.getString("name");
    this.description = jsonObject.getString("description");
  }

  @Override
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("id", id);
    jsonObject.put("name", name);
    jsonObject.put("description", description);
    return jsonObject;
  }

  /**
   * Id
   */
  @Override
  public String getId() {
    return id;
  }

  @Override
  public IdNameDesc id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Name
   */
  @Override
  public String getName() {
    return name;
  }

  @Override
  public IdNameDesc name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Description
   */
  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public IdNameDesc description(String description) {
    this.description = description;
    return this;
  }

}
