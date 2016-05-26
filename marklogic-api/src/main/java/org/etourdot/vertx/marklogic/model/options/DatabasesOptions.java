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
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import static java.util.Optional.ofNullable;

@DataObject(generateConverter = true)
public class DatabasesOptions extends NameViewOptions {
  private JsonObject operation;

  public DatabasesOptions() {
    super();
  }

  public DatabasesOptions(DatabasesOptions options) {
    super(options);

    ofNullable((options.getOperation())).ifPresent(this::operation);
  }

  public DatabasesOptions(JsonObject jsonObject) {
    super(jsonObject);

    ofNullable(jsonObject.getJsonObject("operation")).ifPresent(this::operation);
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    ofNullable(getName()).ifPresent(s -> jsonObject.put("name", s));
    ofNullable(getView()).ifPresent(s -> jsonObject.put("view", s));
    ofNullable(operation).ifPresent(o -> jsonObject.put("operation", o));
    return jsonObject;
  }

  /**
   * Operation details
   */
  public JsonObject getOperation() {
    return operation;
  }

  /**
   * Operation on database
   * @param operation
   * @return
   */
  public DatabasesOptions operation(JsonObject operation) {
    this.operation = operation;
    return this;
  }

  public boolean hasOperation() {
    return ofNullable(operation).isPresent();
  }

  public DatabasesOptions addOption(String name, Object value) {
    if (operation == null) {
      operation = new JsonObject();
    }
    operation.put(name, value);
    return this;
  }

  /**
   * Range element indexes
   */
  public DatabasesOptions addRangeElementIndex(RangeElementIndex index) {
    if (operation == null) {
      operation = new JsonObject();
    }
    JsonArray indexes = operation.getJsonArray("range-element-indexes");
    if (indexes == null) {
      indexes = new JsonArray();
    }
    indexes.add(index.toJson());
    return this;
  }

  /**
   * Range attribute indexes
   */
  public DatabasesOptions addRangeAttributeIndex(RangeAttributeIndex index) {
    if (operation == null) {
      operation = new JsonObject();
    }
    JsonArray indexes = operation.getJsonArray("range-element-attribute-indexes");
    if (indexes == null) {
      indexes = new JsonArray();
    }
    indexes.add(index.toJson());
    return this;
  }
}
