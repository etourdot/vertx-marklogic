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

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientResponse;
import org.etourdot.vertx.marklogic.Format;
import org.etourdot.vertx.marklogic.http.MarkLogicResponse;
import org.etourdot.vertx.marklogic.http.utils.ContentType;
import org.etourdot.vertx.marklogic.http.utils.HttpUtils;

public class DefaultResponse implements MarkLogicResponse {

  final int statusCode;
  final String statusMessage;
  final Format format;
  final String documentUri;
  final long contentLength;
  final ContentType contentType;
  final HttpClientResponse response;
  Handler<MarkLogicResponse> endHandler;

  public DefaultResponse(HttpClientResponse response) {
    this.response = response;
    this.statusCode = response.statusCode();
    this.statusMessage = response.statusMessage();
    MultiMap headers = response.headers();
    this.format = HttpUtils.extractFormat(headers);
    this.documentUri = HttpUtils.extractDocumentUri(headers);
    this.contentLength = HttpUtils.extractContentLength(headers);
    this.contentType = HttpUtils.extractContentType(headers);
  }

  @Override
  public int statusCode() {
    return statusCode;
  }

  @Override
  public String statusMessage() {
    return statusMessage;
  }

  @Override
  public Format getFormat() {
    return format;
  }

  @Override
  public String getDocumentUri() {
    return documentUri;
  }

  @Override
  public long getContentLength() {
    return contentLength;
  }

  @Override
  public ContentType getContentType() {
    return contentType;
  }

  @Override
  public MarkLogicResponse endHandler(Handler<MarkLogicResponse> endHandler) {
    this.endHandler = endHandler;
    return this;
  }

  @Override
  public HttpClientResponse getResponse() {
    return response;
  }

  @Override
  public void contentHandler(Handler<Buffer> contentHandler) {
    response.bodyHandler(buffer -> contentHandler.handle(buffer));
  }
}
