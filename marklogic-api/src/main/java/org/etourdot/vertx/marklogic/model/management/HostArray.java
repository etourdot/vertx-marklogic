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

@DataObject
public class HostArray {
  private JsonArray hosts;

  public HostArray() {
  }

  public HostArray(JsonObject jsonObject) {
    this();
    requireNonNull(jsonObject);

    this.hosts = jsonObject.getJsonObject("host-default-list")
      .getJsonObject("list-items")
      .getJsonArray("list-item");
  }

  public HostArray(HostArray hostArray) {
    this();
    requireNonNull(hostArray);

    this.hosts = hostArray.getHosts();
  }

  /**
   * List of host
   */
  public JsonArray getHosts() {
    return hosts;
  }

  /**
   * Id
   */
  public String getId(int pos) {
    return hosts.getJsonObject(pos).getString("idref");
  }

  /**
   * Name
   */
  public String getName(int pos) {
    return hosts.getJsonObject(pos).getString("nameref");
  }
}
