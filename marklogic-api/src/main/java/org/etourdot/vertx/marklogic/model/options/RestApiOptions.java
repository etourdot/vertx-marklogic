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

import static java.util.Objects.*;
import static java.util.Optional.*;

@DataObject(generateConverter = true)
public class RestApiOptions {
  private Boolean removeContent;
  private Boolean removeModules;
  private String retrieveInstance;
  private String retrieveDatabase;
  private JsonArray restApis;

  public RestApiOptions() {
  }

  public RestApiOptions(RestApiOptions options) {
    this();
    requireNonNull(options);

    removeContent(options.isRemoveContent());
    removeModules(options.isRemoveModules());
    ofNullable((options.getRetrieveInstance())).ifPresent(this::retrieveInstance);
    ofNullable(options.getRetrieveDatabase()).ifPresent(this::retrieveDatabase);
    ofNullable(options.getRestApis()).ifPresent(this::restApis);
  }

  public RestApiOptions(JsonObject jsonObject) {
    this();
    requireNonNull(jsonObject);

    ofNullable(jsonObject.getValue("remove-content")).ifPresent(this::removeContent);
    ofNullable(jsonObject.getValue("remove-modules")).ifPresent(this::removeModules);
    ofNullable(jsonObject.getString("retrieve-instance")).ifPresent(this::retrieveInstance);
    ofNullable(jsonObject.getString("retrieve-database")).ifPresent(this::retrieveDatabase);
    ofNullable(jsonObject.getJsonArray("rest-apis")).ifPresent(this::restApis);
    ofNullable(jsonObject.getJsonObject("rest-api")).ifPresent(this::restApi);
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    ofNullable(removeContent).ifPresent(s -> jsonObject.put("remove-content", s));
    ofNullable(removeModules).ifPresent(s -> jsonObject.put("remove-modules", s));
    ofNullable(retrieveInstance).ifPresent(s -> jsonObject.put("retrieve-instance", s));
    ofNullable(retrieveDatabase).ifPresent(s -> jsonObject.put("retrieve-database", s));
    ofNullable(restApis).ifPresent(s -> {
      if (s.size() > 1) {
        jsonObject.put("rest-apis", s);
      } else if (s.size() == 1) {
        jsonObject.put("rest-api", s.getJsonObject(0));
      }
    });
    return jsonObject;
  }

  public Boolean isRemoveContent() {
    return ofNullable(removeContent).orElse(false);
  }

  public void removeContent(Object removeContent) {
    if (removeContent instanceof Boolean) {
      this.removeContent = (Boolean) removeContent;
    } else if (removeContent instanceof String) {
      this.removeContent = Boolean.parseBoolean((String) removeContent);
    }
  }

  public Boolean isRemoveModules() {
    return ofNullable(removeModules).orElse(false);
  }

  public void removeModules(Object removeModules) {
    if (removeModules instanceof Boolean) {
      this.removeModules = (Boolean) removeModules;
    } else if (removeModules instanceof String) {
      this.removeModules = Boolean.parseBoolean((String) removeModules);
    }
  }

  public String getRetrieveInstance() {
    return retrieveInstance;
  }

  public RestApiOptions retrieveInstance(String retrieveInstance) {
    this.retrieveInstance = retrieveInstance;
    return this;
  }

  public Boolean hasRetrieveInstance() {
    return ofNullable(retrieveInstance).isPresent();
  }

  public String getRetrieveDatabase() {
    return retrieveDatabase;
  }

  public RestApiOptions retrieveDatabase(String retrieveDatabase) {
    this.retrieveDatabase = retrieveDatabase;
    return this;
  }

  public Boolean hasRetrieveDatabase() {
    return ofNullable(retrieveDatabase).isPresent();
  }

  public JsonArray getRestApis() {
    return restApis;
  }

  public RestApiOptions restApis(JsonArray restApis) {
    this.restApis = restApis;
    return this;
  }

  public void restApi(JsonObject jsonObject) {
    this.restApis = new JsonArray();
    this.restApis.add(jsonObject);
  }
}
