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

package examples.management;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.etourdot.vertx.marklogic.MarkLogicConfig;
import org.etourdot.vertx.marklogic.client.MarkLogicClient;
import org.etourdot.vertx.marklogic.model.client.Document;
import org.etourdot.vertx.marklogic.model.client.Documents;
import org.etourdot.vertx.marklogic.model.client.impl.DocumentsImpl;

public class Examples {

  public void exampleCreate(Vertx vertx, MarkLogicConfig config) {

    MarkLogicClient markLogicClient = MarkLogicClient.create(vertx, config);

  }

  public void exampleSaveDocument(MarkLogicClient markLogicClient) {

    JsonObject documentContent = new JsonObject().put("title", "The Hobbit");

    Document document = Document.create();
    document.uri("/json/documents/exemple1.json")
      .content(documentContent);
    markLogicClient.save(document.toJson(), res -> {
      if (res.succeeded()) {

        String id = res.result().getString(0);
        System.out.println("Saved book with id " + id);

      } else {
        res.cause().printStackTrace();
      }

    });
  }

  public void exampleSaveDocuments(MarkLogicClient markLogicClient) {

    JsonObject documentContent1 = new JsonObject().put("title", "The Hobbit");
    JsonObject documentContent2 = new JsonObject().put("title", "The Two Towers");

    Document document1 = Document.create().uri("/json/documents/exemple1.json").content(documentContent1);
    Document document2 = Document.create().directory("/json/documents/").extension(".json").content(documentContent2);
    Documents documents = new DocumentsImpl().addDocuments(document1, document2);
    markLogicClient.save(documents.toJson(), res -> {
      if (res.succeeded()) {

        String id1 = res.result().getString(0);
        String id2 = res.result().getString(1);
        System.out.println("Saved book with id " + id1);
        System.out.println("Saved book with id " + id2);

      } else {
        res.cause().printStackTrace();
      }

    });
  }
}
