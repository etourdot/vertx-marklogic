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

package org.etourdot.vertx.marklogic.http.impl.request;

import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.CaseInsensitiveHeaders;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import org.etourdot.vertx.marklogic.Format;
import org.etourdot.vertx.marklogic.http.MultiPartRequest;
import org.etourdot.vertx.marklogic.http.impl.HttpPart;
import org.etourdot.vertx.marklogic.http.utils.HttpUtils;
import org.etourdot.vertx.marklogic.http.utils.Realm;
import org.etourdot.vertx.marklogic.model.client.Document;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DefaultMultiPartRequest extends DefaultMarklogicRequest implements MultiPartRequest {
  private static final String DEFAULT_BOUNDARY = "ML_BOUNDARY_";
  private String boundary;
  private final List<HttpPart> parts;

  public DefaultMultiPartRequest(HttpClient httpClient, Realm realm) {
    super(httpClient, realm);
    parts = new ArrayList<>();
    boundary = DEFAULT_BOUNDARY + Instant.now().toEpochMilli();
  }

  @Override
  public void boundary(String boundary) {
    this.boundary = boundary;
  }

  @Override
  public void execute() {
    final HttpClientRequest httpClientRequest = httpClient.request(method, uriEncoder.toString());
    httpClientRequest.handler(new AuthHttpHandler(this, responseHandler));
    if (HttpMethod.POST == method) {
      httpClientRequest.putHeader(HttpHeaders.ACCEPT, Format.JSON.getDefaultMimetype());
      httpClientRequest.putHeader(HttpHeaders.CONTENT_TYPE, "multipart/mixed; boundary=" + boundary);
    } else {
      httpClientRequest.putHeader(HttpHeaders.ACCEPT, "multipart/mixed; boundary=" + boundary);
      httpClientRequest.putHeader(HttpHeaders.CONTENT_TYPE, Format.JSON.getDefaultMimetype());
    }
    if (authorization != null) {
      httpClientRequest.putHeader(HttpHeaders.AUTHORIZATION, authorization);
    }
    if (body != null) {
      httpClientRequest.putHeader(HttpHeaders.CONTENT_LENGTH, Long.toString(contentLength)).write(body);
    } else if (!parts.isEmpty()) {
      long computeLength = 0;
      String boundaryString = "--" + boundary;
      Buffer buffer = Buffer.buffer();
      for (HttpPart part : parts) {
        Buffer partBuffer = part.toBuffer();
        buffer.appendString(boundaryString).appendString(System.lineSeparator()).appendBuffer(partBuffer).appendString(System.lineSeparator());
        computeLength += boundaryString.length() + partBuffer.length() + 2 * System.lineSeparator().length();
      }
      buffer.appendString(boundaryString).appendString("--").appendString(System.lineSeparator());
      computeLength += boundaryString.length() + 2 + System.lineSeparator().length();
      httpClientRequest.putHeader(HttpHeaders.CONTENT_LENGTH, Long.toString(computeLength)).write(buffer);
    }
    httpClientRequest.end();
  }

  @Override
  public MultiPartRequest addDocument(Document document) {
    parts.addAll(constructMultipartHttpPart(document));
    return this;
  }

  private static List<HttpPart> constructMultipartHttpPart(Document document) {
    List<HttpPart> partList = new ArrayList<>();
    try {
      if (document.hasMetadata()) {
        Buffer metadata = Buffer.buffer(document.getMetadata().encode().getBytes("UTF-8"));
        partList.add(constructHttpPart(document, metadata, null));
      }
      if (document.hasContent()) {
        final Format format = Format.valueOf(document.getFormat());
        Buffer body = Buffer.buffer();
        final String mimeType = format.getDefaultMimetype();
        if (Format.JSON == format) {
          body.appendString(((JsonObject) document.getContent()).encode(), "UTF-8");
        } else if (Format.XML == format || Format.TEXT == format || Format.BINARY == format) {
          body.appendString((String) document.getContent(), "UTF-8");
        }
        partList.add(constructHttpPart(document, body, mimeType));
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return partList;
  }

  private static HttpPart constructHttpPart(Document document, Buffer buffer, String mimeType) {
    HttpPart contentHttpPart = new HttpPart();
    MultiMap headers = new CaseInsensitiveHeaders();
    if (mimeType != null) {
      headers.add(HttpHeaders.CONTENT_TYPE, mimeType);
    } else {
      headers.add(HttpHeaders.CONTENT_TYPE, Format.JSON.getDefaultMimetype());
    }
    headers.add(HttpHeaders.CONTENT_LENGTH, Integer.toString(buffer.length()));
    headers.add(HttpUtils.HEADER_CONTENT_DISPOSITION, computeContentDisposition(document, mimeType != null));
    contentHttpPart.headers(headers).buffer(buffer);
    return contentHttpPart;
  }

  private static String computeContentDisposition(Document document, boolean isContent) {
    final StringBuilder dispoBuilder = new StringBuilder();
    if (document.hasUri()) {
      dispoBuilder.append("attachment;");
      dispoBuilder.append("filename=\"")
        .append(document.getUri()).append("\";");
    } else if (document.isNewDocument()){
      dispoBuilder.append("inline;");
      if (isContent) {
        dispoBuilder.append("extension=").append(document.getExtension()).append(";");
        dispoBuilder.append("directory=").append(document.getDirectory()).append(";");
      }
    }
    dispoBuilder.append("category=");
    if (isContent) {
      dispoBuilder.append("content");
    } else {
      dispoBuilder.append("metadata");
    }
    dispoBuilder.append(";");
    dispoBuilder.append("format=");
    if (isContent) {
      dispoBuilder.append(document.getFormat().toLowerCase());
    } else {
      dispoBuilder.append("json");
    }
    return dispoBuilder.toString();
  }
}
