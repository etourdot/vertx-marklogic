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
import io.vertx.core.json.JsonObject;
import io.vertx.core.parsetools.RecordParser;
import org.etourdot.vertx.marklogic.MarkLogicConstants;
import org.etourdot.vertx.marklogic.http.MultiPartResponse;
import org.etourdot.vertx.marklogic.http.impl.HttpPart;
import org.etourdot.vertx.marklogic.http.utils.ContentType;
import org.etourdot.vertx.marklogic.model.client.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultMultiPartResponse extends DefaultResponse implements MultiPartResponse {

  List<Document> documents;

  public DefaultMultiPartResponse(HttpClientResponse response) {
    super(response);
  }

  @Override
  public void process() {
    extractParts(response, contentType);
  }

  @Override
  public List<Document> getDocuments() {
    return documents;
  }

  private List<Document> transformPartToDocument(Map<String, List<HttpPart>> responseParts) {
    List<Document> documents = new ArrayList<>();
    for (Map.Entry<String, List<HttpPart>> responsePartEntry : responseParts.entrySet()) {
      Document document = Document.create();
      for (HttpPart part : responsePartEntry.getValue()) {
        document.format(part.getFormat());
        document.uri(part.getDocumentUri());
        if (part.isMetadata()) {
          JsonObject metadata = part.getBuffer().toJsonObject();
          document.collections(metadata.getJsonArray(MarkLogicConstants.COLLECTIONS));
          document.permissions(metadata.getJsonArray(MarkLogicConstants.PERMISSIONS));
          document.properties(metadata.getJsonObject(MarkLogicConstants.PROPERTIES));
          document.quality(metadata.getInteger(MarkLogicConstants.QUALITY));
        } else if (part.isContent()) {
          if (document.isJson()) {
            document.content(part.getBuffer().toJsonObject());
          } else {
            document.content(part.getBuffer());
          }
        }
      }
      documents.add(document);
    }
    return documents;
  }

  private void extractParts(HttpClientResponse response, ContentType contentType) {
    final Map<String, List<HttpPart>> parts = new HashMap<>();
    RecordParser parser = RecordParser.newDelimited("--" + contentType.getBoundary(), frame -> {
      if (frame.length() > 0) {
        HttpPart httpPart = new HttpPart(frame);
        if (parts.containsKey(httpPart.getDocumentUri())) {
          List<HttpPart> httpParts = parts.get(httpPart.getDocumentUri());
          List<HttpPart> httpParts2 = new ArrayList<>(httpParts);
          httpParts2.add(httpPart);
          parts.put(httpPart.getDocumentUri(), httpParts2);
        } else {
          parts.put(httpPart.getDocumentUri(), Collections.singletonList(httpPart));
        }
      }
    });

    response.bodyHandler(buffer -> {
      // multipart
      if (contentType.getBoundary() != null) {
        parser.handle(buffer);
      } else {
        HttpPart httpPart = new HttpPart(buffer, response.headers());
        if (httpPart.getDocumentUri() != null) {
          if (parts.containsKey(httpPart.getDocumentUri())) {
            List<HttpPart> httpParts = parts.get(httpPart.getDocumentUri());
            httpParts.add(httpPart);
          } else {
            parts.put(httpPart.getDocumentUri(), Collections.singletonList(httpPart));
          }
        }
      }
      this.documents = transformPartToDocument(parts);
      this.endHandler.handle(this);
    });
  }

}
