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
import org.etourdot.vertx.marklogic.model.client.Document;
import org.junit.Test;

public class MarklogicDocumentExistsTest extends AbstractTestMarklogicDocument {

  public void setUp() throws Exception {
    super.setUp();
    deleteOnExit = true;
  }

  @Test
  public void exists_json_document() throws Exception {
    Document document = getDocument("json/doc1_no_uri.json");
    mlClient.save(document.toJson(), onSuccess(ids -> {
      String id = ids.getString(0);
      mlClient.exists(id, onSuccess(descriptor -> {
        assertNotNull(descriptor);
        Document doc = Document.create(descriptor);
        assertEquals(Format.JSON.name(), doc.getFormat());
        assertEquals(id, doc.getUri());
        testComplete();
      }));
    }));
    await();
  }

  @Test
  public void non_exists_document() throws Exception {
    mlClient.exists("/test/non_exist.json", onSuccess(descriptor -> {
      assertNull(descriptor);
      testComplete();
    }));
    await();
  }

}
