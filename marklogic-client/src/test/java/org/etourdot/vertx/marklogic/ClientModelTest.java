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

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import org.etourdot.vertx.marklogic.model.client.Document;
import org.etourdot.vertx.marklogic.model.client.Documents;
import org.etourdot.vertx.marklogic.model.client.impl.DocumentsImpl;
import org.etourdot.vertx.marklogic.model.management.Permission;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class ClientModelTest extends VertxTestBase {

  @Test
  public void documentTest() throws Exception {
    Buffer buf = vertx.fileSystem().readFileBlocking(getClass().getClassLoader()
      .getResource("json/doc1_no_uri.json").getFile());
    Document doc = Document.create(new JsonObject(buf.toString()));
    buf = vertx.fileSystem().readFileBlocking(getClass().getClassLoader()
      .getResource("json/doc1_no_uri_verify.json").getFile());
    JSONAssert.assertEquals(doc.toJson().encode(), buf.toString(), true);
  }

  @Test
  public void documentsTest() throws Exception {
    Buffer buf = vertx.fileSystem().readFileBlocking(getClass().getClassLoader()
      .getResource("json/docs1.json").getFile());
    Documents docs = new DocumentsImpl(new JsonObject(buf.toString()));
    buf = vertx.fileSystem().readFileBlocking(getClass().getClassLoader()
      .getResource("json/docs1_verify.json").getFile());
    JSONAssert.assertEquals(docs.toJson().encode(), buf.toString(), true);
  }

  @Test
  public void permissionTest() throws Exception {
    Buffer buf = vertx.fileSystem().readFileBlocking(getClass().getClassLoader()
      .getResource("json/perm1.json").getFile());
    Permission perm = new Permission(new JsonObject(buf.toString()));
    JSONAssert.assertEquals(perm.toJson().encode(), buf.toString(), true);
  }

  @Test
  public void emptyDocumentTest() {
    Document document = Document.create();
    try {
      document.toJson();
      fail();
    } catch (Exception t) {
      assertEquals("unknown format", t.getMessage());
    }
  }
}
