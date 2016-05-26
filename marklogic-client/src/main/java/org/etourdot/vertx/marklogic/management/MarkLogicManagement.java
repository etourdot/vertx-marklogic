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

package org.etourdot.vertx.marklogic.management;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.etourdot.vertx.marklogic.MarkLogicAvailability;
import org.etourdot.vertx.marklogic.impl.DefaultMarkLogicManagement;
import org.etourdot.vertx.marklogic.MarkLogicConfig;
import org.etourdot.vertx.marklogic.model.management.Host;
import org.etourdot.vertx.marklogic.model.management.Privilege;
import org.etourdot.vertx.marklogic.model.management.Role;
import org.etourdot.vertx.marklogic.model.management.User;
import org.etourdot.vertx.marklogic.model.options.DatabasesOptions;
import org.etourdot.vertx.marklogic.model.options.ForestsOptions;
import org.etourdot.vertx.marklogic.model.options.HostsOptions;
import org.etourdot.vertx.marklogic.model.options.PrivilegesOptions;
import org.etourdot.vertx.marklogic.model.options.RestApiOptions;
import org.etourdot.vertx.marklogic.model.options.RolesOptions;
import org.etourdot.vertx.marklogic.model.options.UsersOptions;

@VertxGen
public interface MarkLogicManagement extends MarkLogicAvailability {

  static MarkLogicManagement create(Vertx vertx, MarkLogicConfig config) {
    return new DefaultMarkLogicManagement(vertx, config);
  }

  void createForest(ForestsOptions forestsOption, Handler<AsyncResult<Void>> resultHandler);

  void deleteForest(ForestsOptions forestsOption, Handler<AsyncResult<Void>> resultHandler);

  void getForests(ForestsOptions forestsOption, Handler<AsyncResult<JsonObject>> resultHandler);

  void getForestProps(ForestsOptions forestsOption, Handler<AsyncResult<JsonObject>> resultHandler);

  void setForestProps(ForestsOptions forestsOption, Handler<AsyncResult<Void>> resultHandler);

  void getHosts(HostsOptions hostsOptions, Handler<AsyncResult<JsonObject>> resultHandler);

  void getDefaultHost(Handler<AsyncResult<Host>> resultHandler);

  void getHostProps(HostsOptions hostsOptions, Handler<AsyncResult<JsonObject>> resultHandler);

  void getDatabases(DatabasesOptions databasesOptions, Handler<AsyncResult<JsonObject>> resultHandler);

  void createDatabase(String databaseName, Handler<AsyncResult<String>> resultHandler);

  void databaseOperation(DatabasesOptions databasesOptions, Handler<AsyncResult<String>> resultHandler);

  void getDatabaseProps(DatabasesOptions databasesOptions, Handler<AsyncResult<JsonObject>> resultHandler);

  void setDatabaseProps(DatabasesOptions databasesOptions, Handler<AsyncResult<String>> resultHandler);

  void getRoles(RolesOptions rolesOptions, Handler<AsyncResult<JsonObject>> resultHandler);

  void createRole(Role role, Handler<AsyncResult<Void>> resultHandler);

  void deleteRole(RolesOptions rolesOptions, Handler<AsyncResult<Void>> resultHandler);

  void getUsers(UsersOptions usersOptions, Handler<AsyncResult<JsonObject>> resultHandler);

  void createUser(User user, Handler<AsyncResult<Void>> resultHandler);

  void deleteUser(UsersOptions usersOptions, Handler<AsyncResult<Void>> resultHandler);

  void getPrivileges(PrivilegesOptions privilegesOptions, Handler<AsyncResult<JsonObject>> resultHandler);

  void createPrivilege(Privilege privilege, Handler<AsyncResult<Void>> resultHandler);

  void deletePrivilege(PrivilegesOptions privilegesOptions, Handler<AsyncResult<Void>> resultHandler);

  void createRESTAppServer(RestApiOptions restApiOptions, Handler<AsyncResult<Void>> resultHandler);

  void deleteRESTAppServer(RestApiOptions restApiOptions, Handler<AsyncResult<String>> resultHandler);

  void getRESTAppServerConfig(RestApiOptions restApiOptions, Handler<AsyncResult<JsonObject>> resultHandler);

  void getSecurityRoles(JsonObject config, Handler<AsyncResult<JsonObject>> resultHandler);
}
