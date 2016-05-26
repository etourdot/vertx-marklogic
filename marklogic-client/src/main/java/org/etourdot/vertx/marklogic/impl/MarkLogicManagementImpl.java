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

package org.etourdot.vertx.marklogic.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.etourdot.vertx.marklogic.MarkLogicConfig;
import org.etourdot.vertx.marklogic.http.MarkLogicRequest;
import org.etourdot.vertx.marklogic.http.MarklogicRestService;
import org.etourdot.vertx.marklogic.http.impl.DefaultRestService;
import org.etourdot.vertx.marklogic.model.management.Host;
import org.etourdot.vertx.marklogic.model.management.IdNameDesc;
import org.etourdot.vertx.marklogic.model.management.Privilege;
import org.etourdot.vertx.marklogic.model.management.Role;
import org.etourdot.vertx.marklogic.model.management.User;
import org.etourdot.vertx.marklogic.model.options.DatabasesOptions;
import org.etourdot.vertx.marklogic.model.options.ForestsOptions;
import org.etourdot.vertx.marklogic.model.options.HostsOptions;
import org.etourdot.vertx.marklogic.model.options.NameViewOptions;
import org.etourdot.vertx.marklogic.model.options.PrivilegesOptions;
import org.etourdot.vertx.marklogic.model.options.RolesOptions;
import org.etourdot.vertx.marklogic.model.options.UsersOptions;

import static java.util.Objects.*;

class MarkLogicManagementImpl {

  private static final String MANAGE_FORESTS = "/manage/v2/forests";
  private static final String MANAGE_HOSTS = "/manage/v2/hosts";
  private static final String MANAGE_DATABASES = "/manage/v2/databases";
  private static final String MANAGE_ROLES = "/manage/v2/roles";
  private static final String MANAGE_USERS = "/manage/v2/users";
  private static final String MANAGE_PRIVILEGES = "/manage/v2/privileges";
  private static final String LEVEL = "level";
  private static final String REPLICAS = "replicas";
  private static final String VIEW = "view";

  private Logger logger = LoggerFactory.getLogger(MarkLogicDocumentImpl.class);

  private final MarklogicRestService restService;

  public MarkLogicManagementImpl(Vertx vertx, MarkLogicConfig config) {
    this.restService = new DefaultRestService.Builder(vertx).marklogicConfig(config).build();
  }

  public MarkLogicManagementImpl(MarklogicRestService restService) {
    this.restService = restService;
  }

  void createForest(ForestsOptions forestsOption, Handler<AsyncResult<Void>> resultHandler) {
    requireNonNull(forestsOption, "forestsOption cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    MarkLogicRequest marklogicRequest = restService.newMarklogicRequest();
    marklogicRequest.post(MANAGE_FORESTS)
      .withBody(forestsOption.toJson())
      .execute(response -> {
        if (HttpResponseStatus.CREATED.code() == response.statusCode()) {
          resultHandler.handle(Future.succeededFuture());
        } else {
          response.contentHandler(buffer -> resultHandler.handle(Future.failedFuture(buffer.toJsonObject().encode())));
        }
      });
  }

  void deleteForest(ForestsOptions forestsOption, Handler<AsyncResult<Void>> resultHandler) {
    requireNonNull(forestsOption, "config cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");
    requireNonNull(forestsOption.getName(), "name cannot be null");

    MarkLogicRequest marklogicRequest = restService.newMarklogicRequest();
    String serverHttpString = getUrlWithInstance(MANAGE_FORESTS, forestsOption);
    marklogicRequest.delete(serverHttpString);
    if (forestsOption.hasLevel()) {
      marklogicRequest.addParam(LEVEL, forestsOption.getLevel());
    }
    if (forestsOption.hasReplicas()) {
      marklogicRequest.addParam(REPLICAS, forestsOption.getReplicas());
    }

    marklogicRequest.execute(response -> {
        if (HttpResponseStatus.NO_CONTENT.code() == response.statusCode()) {
          response.contentHandler(buffer -> resultHandler.handle(Future.succeededFuture()));
        } else {
          resultHandler.handle(Future.failedFuture(response.statusMessage()));
        }
      });
  }

  void getForests(ForestsOptions forestsOption, Handler<AsyncResult<JsonObject>> resultHandler) {
    requireNonNull(forestsOption, "forestsOption cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    getNameViewConfig(resultHandler, forestsOption, MANAGE_FORESTS);
  }

  void getForestProps(ForestsOptions forestsOption, Handler<AsyncResult<JsonObject>> resultHandler) {
    requireNonNull(forestsOption, "forestsOption cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");
    requireNonNull(forestsOption.getName(), "forest-name cannot be null");

    getNameViewProperties(resultHandler, forestsOption, MANAGE_FORESTS);
  }

  void setForestProps(ForestsOptions forestsOption, Handler<AsyncResult<Void>> resultHandler) {
    requireNonNull(forestsOption, "forestsOption cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");
    requireNonNull(forestsOption.getName(), "forest-name cannot be null");

    MarkLogicRequest marklogicRequest = restService.newMarklogicRequest();
    String serverHttpString = getUrlWithInstance(MANAGE_FORESTS, forestsOption) + "/properties";
    marklogicRequest.put(serverHttpString)
      .execute(response -> {
        if (HttpResponseStatus.NO_CONTENT.code() == response.statusCode()) {
          response.contentHandler(buffer -> resultHandler.handle(Future.succeededFuture()));
        } else {
          resultHandler.handle(Future.failedFuture(response.statusMessage()));
        }
      });
  }

  void getHosts(HostsOptions hostsOption, Handler<AsyncResult<JsonObject>> resultHandler) {
    requireNonNull(hostsOption, "hostsOption cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    getNameViewConfig(resultHandler, hostsOption, MANAGE_HOSTS);
  }

  void getDefaultHost(Handler<AsyncResult<Host>> resultHandler) {
    HostsOptions hostsOptions = new HostsOptions();
    getHosts(hostsOptions, r -> resultHandler.handle(Future.succeededFuture(new Host().name(r.result()))));
  }

  void getHostProps(HostsOptions hostsOption, Handler<AsyncResult<JsonObject>> resultHandler) {
    requireNonNull(hostsOption, "hostsOption cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    getNameViewProperties(resultHandler, hostsOption, MANAGE_HOSTS);
  }

  void getDatabases(DatabasesOptions databasesOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
    requireNonNull(databasesOptions, "databasesOptions cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    getNameViewConfig(resultHandler, databasesOptions, MANAGE_DATABASES);
  }

  void databaseOperation(DatabasesOptions databasesOptions, Handler<AsyncResult<String>> resultHandler) {
    requireNonNull(databasesOptions, "databaseOption cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    MarkLogicRequest marklogicRequest = restService.newMarklogicRequest();
    String serverHttpString = getUrlWithInstance(MANAGE_DATABASES, databasesOptions);
    marklogicRequest.post(serverHttpString)
      .withBody(databasesOptions.getOperation())
      .execute(response -> {
      if (HttpResponseStatus.ACCEPTED.code() == response.statusCode()) {
        resultHandler.handle(Future.succeededFuture(response.statusMessage()));
      } else {
        resultHandler.handle(Future.failedFuture(response.statusMessage()));
      }
    });
  }

  void createDatabase(String databaseName, Handler<AsyncResult<String>> resultHandler) {
    requireNonNull(databaseName, "databaseName cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    JsonObject operation = new JsonObject().put("database-name", databaseName);
    DatabasesOptions databasesOptions = new DatabasesOptions().operation(operation);
    databaseOperation(databasesOptions, resultHandler);
  }

  void getDatabaseProps(DatabasesOptions databasesOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
    requireNonNull(databasesOptions, "databaseOption cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    getNameViewProperties(resultHandler, databasesOptions, MANAGE_DATABASES);
  }

  void setDatabaseProps(DatabasesOptions databasesOptions, Handler<AsyncResult<String>> resultHandler) {
    requireNonNull(databasesOptions, "databaseOption cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    MarkLogicRequest marklogicRequest = restService.newMarklogicRequest();
    String serverHttpString = getUrlWithInstance(MANAGE_DATABASES, databasesOptions) + "/properties";
    marklogicRequest.put(serverHttpString)
      .withBody(databasesOptions.getOperation())
      .execute(response -> {
        if (HttpResponseStatus.NO_CONTENT.code() == response.statusCode()) {
          response.contentHandler(buffer -> resultHandler.handle(Future.succeededFuture()));
        } else {
          resultHandler.handle(Future.failedFuture(response.statusMessage()));
        }
      });
  }

  void getRoles(RolesOptions rolesOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
    requireNonNull(rolesOptions, "rolesOptions cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    getNameViewConfig(resultHandler, rolesOptions, MANAGE_ROLES);
  }

  void createRole(Role role, Handler<AsyncResult<Void>> resultHandler) {
    requireNonNull(role, "role cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    createNameObject(role, resultHandler, MANAGE_ROLES);
  }

  void deleteRole(RolesOptions rolesOptions, Handler<AsyncResult<Void>> resultHandler) {
    requireNonNull(rolesOptions, "rolesOptions cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    deleteNameObject(rolesOptions, resultHandler, MANAGE_ROLES);
  }

  void getUsers(UsersOptions usersOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
    requireNonNull(usersOptions, "usersOptions cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    getNameViewConfig(resultHandler, usersOptions, MANAGE_USERS);
  }

  void createUser(User user, Handler<AsyncResult<Void>> resultHandler) {
    requireNonNull(user, "user cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    createNameObject(user, resultHandler, MANAGE_ROLES);
  }

  void deleteUser(UsersOptions usersOptions, Handler<AsyncResult<Void>> resultHandler) {
    requireNonNull(usersOptions, "usersOptions cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    deleteNameObject(usersOptions, resultHandler, MANAGE_USERS);
  }

  void getPrivileges(PrivilegesOptions privilegesOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
    requireNonNull(privilegesOptions, "privilegesOptions cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    getNameViewConfig(resultHandler, privilegesOptions, MANAGE_PRIVILEGES);
  }

  void createPrivilege(Privilege privilege, Handler<AsyncResult<Void>> resultHandler) {
    requireNonNull(privilege, "privilege cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    createNameObject(privilege, resultHandler, MANAGE_PRIVILEGES);
  }

  void deletePrivilege(PrivilegesOptions privilegesOptions, Handler<AsyncResult<Void>> resultHandler) {
    requireNonNull(privilegesOptions, "privilegesOptions cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    deleteNameObject(privilegesOptions, resultHandler, MANAGE_PRIVILEGES);
  }

  private void deleteNameObject(NameViewOptions nameOptions, Handler<AsyncResult<Void>> resultHandler, String baseUrl) {
    MarkLogicRequest marklogicRequest = restService.newMarklogicRequest();
    String serverHttpString = getUrlWithInstance(baseUrl, nameOptions);
    marklogicRequest.delete(serverHttpString);
    marklogicRequest.execute(response -> {
      if (HttpResponseStatus.NO_CONTENT.code() == response.statusCode()) {
        response.contentHandler(buffer -> resultHandler.handle(Future.succeededFuture()));
      } else {
        resultHandler.handle(Future.failedFuture(response.statusMessage()));
      }
    });
  }

  private void createNameObject(IdNameDesc idName, Handler<AsyncResult<Void>> resultHandler, String baseUrl) {
    MarkLogicRequest marklogicRequest = restService.newMarklogicRequest();
    marklogicRequest.post(baseUrl)
      .withBody(idName.toJson())
      .execute(response -> {
        if (HttpResponseStatus.CREATED.code() == response.statusCode()) {
          resultHandler.handle(Future.succeededFuture());
        } else {
          response.contentHandler(buffer -> resultHandler.handle(Future.failedFuture(buffer.toJsonObject().encode())));
        }
      });
  }

  private void getNameViewConfig(Handler<AsyncResult<JsonObject>> resultHandler, NameViewOptions option, String baseUrl) {
    MarkLogicRequest marklogicRequest = restService.newMarklogicRequest();
    String serverHttpString = getUrlWithInstance(baseUrl, option);
    marklogicRequest.get(serverHttpString);
    if (option.hasView()) {
      marklogicRequest.addParam(VIEW, option.getView());
    }
    marklogicRequest.execute(response -> {
      if (HttpResponseStatus.OK.code() == response.statusCode()) {
        response.contentHandler(buffer -> resultHandler.handle(Future.succeededFuture(buffer.toJsonObject())));
      } else if (HttpResponseStatus.FOUND.code() == response.statusCode()) {
        // uri in Location header
        resultHandler.handle(Future.succeededFuture());
      } else {
        resultHandler.handle(Future.failedFuture(response.statusMessage()));
      }
    });
  }

  private void getNameViewProperties(Handler<AsyncResult<JsonObject>> resultHandler, NameViewOptions option, String baseUrl) {
    MarkLogicRequest marklogicRequest = restService.newMarklogicRequest();
    String serverHttpString = getUrlWithInstance(baseUrl, option) + "/properties";
    marklogicRequest.get(serverHttpString)
      .execute(response -> {
        if (HttpResponseStatus.OK.code() == response.statusCode()) {
          response.contentHandler(buffer -> resultHandler.handle(Future.succeededFuture(buffer.toJsonObject())));
        } else {
          resultHandler.handle(Future.failedFuture(response.statusMessage()));
        }
      });
  }

  private String getUrlWithInstance(String baseUrl, NameViewOptions option) {
    String urlWithInstance = baseUrl;
    if (option.hasId()) {
      urlWithInstance += "/" + option.getId();
    } else if (option.hasName()) {
      urlWithInstance += "/" + option.getName();
    }
    return urlWithInstance;
  }
}
