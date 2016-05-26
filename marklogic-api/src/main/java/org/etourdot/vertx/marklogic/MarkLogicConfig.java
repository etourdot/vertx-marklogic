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

package org.etourdot.vertx.marklogic;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * MarkLogic connection configuration
 */
@DataObject
public class MarkLogicConfig {
  private String host;
  private Integer port;
  private String user;
  private String password;
  private String authentication;

  public MarkLogicConfig() {
  }

  public MarkLogicConfig(MarkLogicConfig marklogicConfig) {
    this.host = marklogicConfig.getHost();
    this.port = marklogicConfig.getPort();
    this.user = marklogicConfig.getUser();
    this.password = marklogicConfig.getPassword();
    this.authentication = marklogicConfig.getAuthentication();
  }

  public MarkLogicConfig(JsonObject jsonObject) {
    this.host = jsonObject.getString("host");
    this.port = jsonObject.getInteger("port");
    this.user = jsonObject.getString("user");
    this.password = jsonObject.getString("password");
    this.authentication = jsonObject.getString("authentication");
  }

  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("host", host);
    jsonObject.put("port", port);
    jsonObject.put("user", user);
    jsonObject.put("password", password);
    jsonObject.put("authentication", authentication);
    return jsonObject;
  }

  /**
   * Host name
   */
  public String getHost() {
    return host;
  }

  public MarkLogicConfig host(String host) {
    this.host = host;
    return this;
  }

  /**
   * Port number
   */
  public int getPort() {
    return port;
  }

  public MarkLogicConfig port(int port) {
    this.port = port;
    return this;
  }

  /**
   * User login
   */
  public String getUser() {
    return user;
  }

  public MarkLogicConfig user(String user) {
    this.user = user;
    return this;
  }

  /**
   * User password
   */
  public String getPassword() {
    return password;
  }

  public MarkLogicConfig password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Authentication (basic, digest)
   */
  public String getAuthentication() {
    return authentication;
  }

  public MarkLogicConfig authentication(String authentication) {
    this.authentication = authentication;
    return this;
  }

}
