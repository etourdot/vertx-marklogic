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

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.query.DeleteQueryDefinition;
import com.marklogic.client.query.QueryManager;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.test.core.TestUtils;
import io.vertx.test.core.VertxTestBase;
import org.etourdot.vertx.marklogic.client.MarkLogicClient;
import org.etourdot.vertx.marklogic.management.MarkLogicManagement;
import org.etourdot.vertx.marklogic.model.client.Document;
import org.etourdot.vertx.marklogic.model.client.Documents;
import org.etourdot.vertx.marklogic.model.client.impl.DocumentsImpl;
import org.etourdot.vertx.marklogic.model.management.Permission;
import org.etourdot.vertx.marklogic.model.options.HostsOptions;

import java.util.concurrent.CountDownLatch;

class AbstractTestMarklogicDocument extends VertxTestBase {

  protected MarkLogicManagement mlManagement;
  protected MarkLogicClient mlClient;
  protected DatabaseClient databaseClient;
  private String directory;

  protected boolean deleteOnExit = false;

  private MarkLogicConfig getClientConfig() {
    JsonObject config = new JsonObject();
    config.put("host", "localhost")
      .put("port", 8003)
      .put("user", "rest-writer")
      .put("password", "x")
      .put("authentication", "digest");
    return new MarkLogicConfig(config);
  }

  private MarkLogicConfig getAdminConfig() {
    JsonObject config = new JsonObject();
    config.put("host", "localhost")
      .put("port", 8002)
      .put("user", "admin")
      .put("password", "admin")
      .put("authentication", "digest")
      .put("keepAlive", false);
    return new MarkLogicConfig(config);
  }

  protected String getDirectory() {
    return this.directory;
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    mlClient = MarkLogicClient.create(vertx, getClientConfig());
    mlManagement = MarkLogicManagement.create(vertx, getAdminConfig());
    this.directory = "/test" + TestUtils.randomAlphaString(20) + "/";
    MarklogicOptionsParser parser = new MarklogicOptionsParser(getClientConfig());
    DatabaseClientFactory.Bean configBean = parser.clientBean();
    databaseClient = configBean.newClient();
  }

  @Override
  public void tearDown() throws Exception {
    if (deleteOnExit) {
      QueryManager queryMgr = databaseClient.newQueryManager();
      DeleteQueryDefinition deleteQuery = queryMgr.newDeleteDefinition();
      deleteQuery.setDirectory(directory);
      queryMgr.delete(deleteQuery);
    }
    databaseClient.release();
    super.tearDown();
  }

  protected Document getXMLDocument(String name) {
    final Buffer buf = vertx.fileSystem().readFileBlocking(getClass().getClassLoader().getResource(name).getFile());
    Document docXML = Document.create();
    docXML.content(buf.toString())
      .uri(getDirectory() + name)
      .contentType("application/xml");
    return docXML;
  }

  protected Document getDocument(String name) {
    final Buffer buf = vertx.fileSystem().readFileBlocking(getClass().getClassLoader().getResource(name).getFile());
    return Document.create(new JsonObject(buf.toString().replaceAll("#DIRNAME#", getDirectory())));
  }

  protected Documents getDocuments(String name) {
    final Buffer buf = vertx.fileSystem().readFileBlocking(getClass().getClassLoader().getResource(name).getFile());
    return new DocumentsImpl(new JsonObject(buf.toString().replaceAll("#DIRNAME#", getDirectory())));
  }

  protected Permission getPermission(String name) {
    final Buffer buf = vertx.fileSystem().readFileBlocking(getClass().getClassLoader().getResource(name).getFile());
    return new Permission(new JsonObject(buf.toString()));
  }

  protected void saveDocuments(Documents documents) throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);
    mlClient.save(documents.toJson(), onSuccess(ids -> {
      latch.countDown();
    }));
    awaitLatch(latch);
  }

  protected String getHost() throws InterruptedException {
    HostsOptions option = new HostsOptions();
    CountDownLatch latch = new CountDownLatch(1);
    final String[] host = new String[1];
    mlManagement.getHosts(option, onSuccess(r -> {
      JsonObject items = r.getJsonObject("host-default-list").getJsonObject("list-items");
      JsonObject item = items.getJsonArray("list-item").getJsonObject(0);
      host[0] = item.getString("nameref");
      latch.countDown();
    }));
    awaitLatch(latch);
    return host[0];
  }
}
