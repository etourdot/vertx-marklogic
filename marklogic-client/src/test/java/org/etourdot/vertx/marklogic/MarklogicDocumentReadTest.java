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

import com.marklogic.client.io.Format;
import io.vertx.core.json.JsonArray;
import org.etourdot.vertx.marklogic.http.ResourceNotFoundException;
import org.etourdot.vertx.marklogic.model.client.Document;
import org.etourdot.vertx.marklogic.model.client.Documents;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.core.Is.*;

public class MarklogicDocumentReadTest extends AbstractTestMarklogicDocument {

  public void setUp() throws Exception {
    super.setUp();
    deleteOnExit = true;
  }

  @Test
  public void read_single_json_document() throws Exception {
    Document document = getDocument("json/doc1_no_uri.json");
    mlClient.save(document.toJson(), onSuccess(ids -> {
      String id = ids.getString(0);
      mlClient.read(id, onSuccess(descriptor -> {
        assertNotNull(descriptor);
        Document doc = Document.create(descriptor);
        assertEquals(id, doc.getUri());
        assertEquals(Format.JSON.name(), doc.getFormat());
        assertThat(new HashSet(doc.getCollections().getList()),
          is(new HashSet(Arrays.asList("coll/c1", "coll/c2"))));
        assertThat(new HashSet(doc.getCategory().getList()),
          is(new HashSet(Arrays.asList("content", "permissions", "collections", "quality"))));
        testComplete();
      }));
    }));
    await();
  }

  @Test
  public void read_multiple_json_documents() throws Exception {
    Documents documents = getDocuments("json/docs1.json");
    saveDocuments(documents);
    String[] uris = new String[]{documents.getDocumentsList().get(0).getUri(),
      documents.getDocumentsList().get(1).getUri()};
    mlClient.readMany(new JsonArray(Arrays.asList(uris)), onSuccess(d -> {
      assertEquals(2, d.size());
      testComplete();
    }));
    await();
  }

  @Test
  public void read_nonexist_document() throws Exception {
    mlClient.read("/test/non_exist.json", onFailure(t -> {
      assertTrue(t instanceof ResourceNotFoundException);
      testComplete();
    }));
    await();
  }

  @Test
  public void read_metadatas() throws Exception {
    Document document = getDocument("json/doc3_uri.json");
    mlClient.save(document.toJson(), onSuccess(ids -> mlClient.readMetadata(document.getUri(), onSuccess(d -> {
      Document result = Document.create(d);
      assertFalse(result.hasContent());
      assertThat(result.getQuality(), is(2));
      testComplete();
    }))));
    await();
  }
}

