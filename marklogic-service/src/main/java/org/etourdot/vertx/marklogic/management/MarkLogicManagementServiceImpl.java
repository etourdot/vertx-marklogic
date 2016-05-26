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

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
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

public class MarkLogicManagementServiceImpl implements MarkLogicManagementService {

  private final MarkLogicManagement markLogicManagement;

  public MarkLogicManagementServiceImpl(MarkLogicManagement markLogicManagement) {
    this.markLogicManagement = markLogicManagement;
  }

  @Override
  public void availability(Handler<AsyncResult<Void>> resultHandler) {
    markLogicManagement.availability(resultHandler);
  }

  @Override
  public void createForest(ForestsOptions forestsOption, Handler<AsyncResult<Void>> resultHandler) {
    markLogicManagement.createForest(forestsOption, resultHandler);
  }

  @Override
  public void deleteForest(ForestsOptions forestsOption, Handler<AsyncResult<Void>> resultHandler) {
    markLogicManagement.deleteForest(forestsOption, resultHandler);
  }

  @Override
  public void getForests(ForestsOptions forestsOption, Handler<AsyncResult<JsonObject>> resultHandler) {
    markLogicManagement.getForests(forestsOption, resultHandler);
  }

  @Override
  public void getForestProps(ForestsOptions forestsOption, Handler<AsyncResult<JsonObject>> resultHandler) {
    markLogicManagement.getForestProps(forestsOption, resultHandler);
  }

  @Override
  public void setForestProps(ForestsOptions forestsOption, Handler<AsyncResult<Void>> resultHandler) {
    markLogicManagement.setForestProps(forestsOption, resultHandler);
  }

  @Override
  public void getHosts(HostsOptions hostsOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
    markLogicManagement.getHosts(hostsOptions, resultHandler);
  }

  @Override
  public void getDefaultHost(Handler<AsyncResult<Host>> resultHandler) {
    markLogicManagement.getDefaultHost(resultHandler);
  }

  @Override
  public void getHostProps(HostsOptions hostsOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
    markLogicManagement.getHostProps(hostsOptions, resultHandler);
  }

  @Override
  public void getDatabases(DatabasesOptions databasesOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
    markLogicManagement.getDatabases(databasesOptions, resultHandler);
  }

  @Override
  public void createDatabase(String databaseName, Handler<AsyncResult<String>> resultHandler) {
    markLogicManagement.createDatabase(databaseName, resultHandler);
  }

  @Override
  public void databaseOperation(DatabasesOptions databasesOptions, Handler<AsyncResult<String>> resultHandler) {
    markLogicManagement.databaseOperation(databasesOptions, resultHandler);
  }

  @Override
  public void getDatabaseProps(DatabasesOptions databasesOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
    markLogicManagement.getDatabaseProps(databasesOptions, resultHandler);
  }

  @Override
  public void setDatabaseProps(DatabasesOptions databasesOptions, Handler<AsyncResult<String>> resultHandler) {
    markLogicManagement.setDatabaseProps(databasesOptions, resultHandler);
  }

  @Override
  public void getRoles(RolesOptions rolesOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
    markLogicManagement.getRoles(rolesOptions, resultHandler);
  }

  @Override
  public void createRole(Role role, Handler<AsyncResult<Void>> resultHandler) {
    markLogicManagement.createRole(role, resultHandler);
  }

  @Override
  public void deleteRole(RolesOptions rolesOptions, Handler<AsyncResult<Void>> resultHandler) {
    markLogicManagement.deleteRole(rolesOptions, resultHandler);
  }

  @Override
  public void getUsers(UsersOptions usersOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
    markLogicManagement.getUsers(usersOptions, resultHandler);
  }

  @Override
  public void createUser(User user, Handler<AsyncResult<Void>> resultHandler) {
    markLogicManagement.createUser(user, resultHandler);
  }

  @Override
  public void deleteUser(UsersOptions usersOptions, Handler<AsyncResult<Void>> resultHandler) {
    markLogicManagement.deleteUser(usersOptions, resultHandler);
  }

  @Override
  public void getPrivileges(PrivilegesOptions privilegesOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
    markLogicManagement.getPrivileges(privilegesOptions, resultHandler);
  }

  @Override
  public void createPrivilege(Privilege privilege, Handler<AsyncResult<Void>> resultHandler) {
    markLogicManagement.createPrivilege(privilege, resultHandler);
  }

  @Override
  public void deletePrivilege(PrivilegesOptions privilegesOptions, Handler<AsyncResult<Void>> resultHandler) {
    markLogicManagement.deletePrivilege(privilegesOptions, resultHandler);
  }

  @Override
  public void createRESTAppServer(RestApiOptions restApiOptions, Handler<AsyncResult<Void>> resultHandler) {
    markLogicManagement.createRESTAppServer(restApiOptions, resultHandler);
  }

  @Override
  public void deleteRESTAppServer(RestApiOptions restApiOptions, Handler<AsyncResult<String>> resultHandler) {
    markLogicManagement.deleteRESTAppServer(restApiOptions, resultHandler);
  }

  @Override
  public void getRESTAppServerConfig(RestApiOptions restApiOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
    markLogicManagement.getRESTAppServerConfig(restApiOptions, resultHandler);
  }

  @Override
  public void getSecurityRoles(JsonObject config, Handler<AsyncResult<JsonObject>> resultHandler) {
    markLogicManagement.getSecurityRoles(config, resultHandler);
  }
}
