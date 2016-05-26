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

import static java.util.Optional.*;

@DataObject(generateConverter = true)
public class ForestsOptions extends NameViewOptions {
  private String level;
  private String replicas;
  private String forestName;
  private String host;
  private String database;

  public ForestsOptions() {
    super();
  }

  public ForestsOptions(ForestsOptions options) {
    super(options);

    ofNullable((options.getLevel())).ifPresent(this::level);
    ofNullable((options.getReplicas())).ifPresent(this::replicas);
    ofNullable((options.getForestName())).ifPresent(this::forestName);
    ofNullable((options.getHost())).ifPresent(this::host);
    ofNullable((options.getDatabase())).ifPresent(this::database);
  }

  public ForestsOptions(JsonObject jsonObject) {
    super(jsonObject);

    ofNullable(jsonObject.getString("level")).ifPresent(this::level);
    ofNullable(jsonObject.getString("replicas")).ifPresent(this::replicas);
    ofNullable(jsonObject.getString("forest-name")).ifPresent(this::forestName);
    ofNullable(jsonObject.getString("host")).ifPresent(this::host);
    ofNullable(jsonObject.getString("database")).ifPresent(this::database);
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    ofNullable(getName()).ifPresent(s -> jsonObject.put("name", s));
    ofNullable(getView()).ifPresent(s -> jsonObject.put("view", s));
    ofNullable(level).ifPresent(s -> jsonObject.put("level", s));
    ofNullable(replicas).ifPresent(s -> jsonObject.put("replicas", s));
    ofNullable(forestName).ifPresent(s -> jsonObject.put("forest-name", s));
    ofNullable(host).ifPresent(s -> jsonObject.put("host", s));
    ofNullable(database).ifPresent(s -> jsonObject.put("database", s));
    return jsonObject;
  }

  /**
   * Level
   */
  public String getLevel() {
    return level;
  }

  public ForestsOptions level(String level) {
    this.level = level;
    return this;
  }

  public Boolean hasLevel() {
    return ofNullable(level).isPresent();
  }

  /**
   * Replicas
   */
  public String getReplicas() {
    return replicas;
  }

  public ForestsOptions replicas(String replicas) {
    this.replicas = replicas;
    return this;
  }

  public Boolean hasReplicas() {
    return ofNullable(replicas).isPresent();
  }

  /**
   * Forest Name
   */
  public String getForestName() {
    return forestName;
  }

  public ForestsOptions forestName(String forestName) {
    this.forestName = forestName;
    return this;
  }

  public boolean hasForestName() {
    return ofNullable(forestName).isPresent();
  }

  /**
   * Host name
   */
  public String getHost() {
    return host;
  }

  public ForestsOptions host(String host) {
    this.host = host;
    return this;
  }

  public boolean hasHost() {
    return ofNullable(host).isPresent();
  }

  /**
   * Database name
   */
  public String getDatabase() {
    return database;
  }

  public ForestsOptions database(String database) {
    this.database = database;
    return this;
  }

  public boolean hasDatabase() {
    return ofNullable(database).isPresent();
  }
}
