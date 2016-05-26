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

import static java.util.Objects.*;

@DataObject
public class Host {
  private String name;

  public Host() {
  }

  public Host(JsonObject jsonObject) {
    this();
    requireNonNull(jsonObject);

    this.name = jsonObject.getString("name");
  }

  public Host(Host host) {
    this();
    requireNonNull(host);

    this.name = host.getName();
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("name", name);
    return jsonObject;
  }

  public Host name(JsonObject jsonObject) {
    this.name = jsonObject.getJsonObject("host-default",
      jsonObject.getJsonObject("host-config", jsonObject.getJsonObject("host-counts",
        jsonObject.getJsonObject("host-status")))).getString("name");
    return this;
  }

  /**
   * Host name
   */
  public String getName() {
    return name;
  }

  public Host name(String name) {
    this.name = name;
    return this;
  }
}
