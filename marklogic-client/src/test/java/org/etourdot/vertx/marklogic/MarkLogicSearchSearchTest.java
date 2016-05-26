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

import org.etourdot.vertx.marklogic.model.client.Document;
import org.etourdot.vertx.marklogic.model.client.Documents;
import org.etourdot.vertx.marklogic.model.options.SearchOptions;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class MarkLogicSearchSearchTest extends AbstractTestMarklogicDocument {

  public void setUp() throws Exception {
    super.setUp();
    deleteOnExit = true;
  }

  @Test
  public void search_simple_json() throws Exception {
    Documents documents = getDocuments("json/docs1.json");
    saveDocuments(documents);
    SearchOptions searchOptions = new SearchOptions();
    searchOptions.expression("neighborhoods");
    mlClient.search(searchOptions, onSuccess(hdl1 -> {
      assertEquals(2, hdl1.getInteger("total").intValue());
      searchOptions.expression("neighborhoods AND correlations");
      mlClient.search(searchOptions, onSuccess(hdl2 -> {
        assertEquals(1, hdl2.getInteger("total").intValue());
        assertEquals(getDirectory() + "docs1_2.json", hdl2.getJsonArray("results").getJsonObject(0).getString("uri"));
        testComplete();
      }));
    }));
    await();
  }

  @Test
  public void search_documents() throws Exception {
    Documents documents = getDocuments("json/docs_batch.json");
    saveDocuments(documents);
    SearchOptions searchOptions = new SearchOptions();
    searchOptions.pageLen(5L);
    searchOptions.directory(getDirectory());
    mlClient.searchDocuments(searchOptions, onSuccess(sbl -> {
      assertEquals(5, sbl.size());
      testComplete();
    }));
    await();
  }

  @Test
  public void search_batch() throws Exception {
    Documents documents = getDocuments("json/docs_batch.json");
    saveDocuments(documents);
    SearchOptions searchOptions = new SearchOptions();
    searchOptions.directory(getDirectory());
    CountDownLatch latch = new CountDownLatch(documents.getDocumentsList().size());
    mlClient.searchBatch(searchOptions, onSuccess(doc -> {
      Document document = Document.create(doc);
      assertNotNull(document);
      latch.countDown();
    }));
    awaitLatch(latch);
  }
}
