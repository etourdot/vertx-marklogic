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

import io.vertx.core.json.JsonArray;
import org.etourdot.vertx.marklogic.model.client.impl.DocumentsImpl;
import org.etourdot.vertx.marklogic.model.options.DeleteOptions;
import org.etourdot.vertx.marklogic.model.client.Document;
import org.etourdot.vertx.marklogic.model.client.Documents;
import org.junit.Test;

import java.util.Arrays;

public class MarkLogicSearchDeleteTest extends AbstractTestMarklogicDocument {

  public void setUp() throws Exception {
    super.setUp();
    deleteOnExit = true;
  }

  @Test
  public void delete_dir() {
    Document doc1 = getDocument("json/doc2_uri.json");
    Document doc2 = getDocument("json/doc3_uri.json");
    Documents documents = new DocumentsImpl();
    documents.addDocuments(doc1, doc2);
    JsonArray uris = new JsonArray(Arrays.asList(doc1.getUri(), doc2.getUri()));
    mlClient.save(documents.toJson(), onSuccess(ids -> {
      DeleteOptions toDeleteOptions = new DeleteOptions();
      toDeleteOptions.directory(getDirectory());
      mlClient.deleteDocuments(toDeleteOptions, onSuccess(t -> {
        mlClient.readMany(uris, onSuccess(reads -> {
          assertEquals(0, reads.size());
          testComplete();
        }));
      }));
    }));
    await();
  }

  @Test
  public void delete_collection() {
    Document doc1 = getDocument("json/doc2_uri.json");
    Document doc2 = getDocument("json/doc3_uri.json");
    Documents documents = new DocumentsImpl();
    documents.addDocuments(doc1, doc2);
    mlClient.save(documents.toJson(), onSuccess(ids -> {
      DeleteOptions toDelete = new DeleteOptions();
      toDelete.collection("coll/c2");
      mlClient.deleteDocuments(toDelete, onSuccess(d1 -> {
        mlClient.exists(doc1.getUri(), onSuccess(d2 -> {
          assertNotNull(d2);
          mlClient.exists(doc2.getUri(), onSuccess(d3 -> {
            assertNull(d3);
            testComplete();
          }));
        }));
      }));
    }));
    await();
  }

  @Test
  public void delete_all() {
    Document doc1 = getDocument("json/doc2_uri.json");
    Document doc2 = getDocument("json/doc3_uri.json");
    Documents documents = new DocumentsImpl();
    documents.addDocuments(doc1, doc2);
    mlClient.save(documents.toJson(), onSuccess(ids -> {
      DeleteOptions toDelete = new DeleteOptions();
      mlClient.deleteDocuments(toDelete, onSuccess(t -> {
        mlClient.exists(doc1.getUri(), onSuccess(d1 -> {
          assertNull(d1);
          mlClient.exists(doc2.getUri(), onSuccess(d2 -> {
            assertNull(d2);
            testComplete();
          }));
        }));
      }));
    }));
    await();
  }

  @Test
  public void delete_bad_dir() {
    Document doc1 = getDocument("json/doc2_uri.json");
    Document doc2 = getDocument("json/doc3_uri.json");
    Documents documents = new DocumentsImpl();
    documents.addDocuments(doc1, doc2);
    JsonArray uris = new JsonArray(Arrays.asList(doc1.getUri(), doc2.getUri()));
    mlClient.save(documents.toJson(), onSuccess(ids -> {
      DeleteOptions toDelete = new DeleteOptions();
      toDelete.directory("/an inexistant directory /");
      mlClient.deleteDocuments(toDelete, onSuccess(t -> {
        mlClient.readMany(uris, onSuccess(reads -> {
          assertEquals(2, reads.size());
          testComplete();
        }));
      }));
    }));
    await();
  }
}
