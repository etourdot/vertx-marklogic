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

package org.etourdot.vertx.marklogic.http.impl.response;

import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.etourdot.vertx.marklogic.http.MarkLogicResponse;
import org.etourdot.vertx.marklogic.model.client.Document;

import java.util.ArrayList;
import java.util.List;

public class BulkResponse extends DefaultResponse {
  List<Document> documents = new ArrayList<>();

  public BulkResponse(HttpClientResponse response) {
    super(response);
  }

  public BulkResponse(MarkLogicResponse response) { super(response.getResponse()); }

  public List<Document> getDocuments() {
    return documents;
  }

  @Override
  public void process() {
    response.bodyHandler(buffer -> {
      final JsonObject jsonResponse = buffer.toJsonObject();
      if (jsonResponse.containsKey("documents")) {
        JsonArray docs = jsonResponse.getJsonArray("documents");
        docs.stream().forEach(doc -> documents.add(Document.create((JsonObject) doc)));
      }
      this.endHandler.handle(this);
    });
  }
}
