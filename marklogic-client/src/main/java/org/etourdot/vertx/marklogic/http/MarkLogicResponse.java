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

package org.etourdot.vertx.marklogic.http;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientResponse;
import org.etourdot.vertx.marklogic.Format;
import org.etourdot.vertx.marklogic.http.utils.ContentType;

public interface MarkLogicResponse {

  int statusCode();

  String statusMessage();

  default void process() {}

  default MarkLogicResponse endHandler(Handler<MarkLogicResponse> endHandler) { return this; }

  default Format getFormat() {
    return null;
  }

  default String getDocumentUri() {
    return null;
  }

  default long getContentLength() {
    return 0L;
  }

  default ContentType getContentType() {
    return null;
  }

  default HttpClientResponse getResponse() {
    return null;
  }

  default void contentHandler(Handler<Buffer> contentHandler) {}
}
