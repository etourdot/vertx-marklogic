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

package org.etourdot.vertx.marklogic.http.impl;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.CaseInsensitiveHeaders;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.parsetools.RecordParser;
import org.etourdot.vertx.marklogic.MarkLogicConstants;
import org.etourdot.vertx.marklogic.http.utils.ContentDisposition;
import org.etourdot.vertx.marklogic.http.utils.ContentType;
import org.etourdot.vertx.marklogic.http.utils.HttpUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HttpPart {
  private Buffer buffer;
  private MultiMap headers;
  private ContentType contentType;
  private ContentDisposition contentDisposition;

  public HttpPart() {
  }

  public HttpPart(Buffer buffer, MultiMap headers) {
    this.buffer = buffer;
    this.headers = headers;
    this.contentType = HttpUtils.extractContentType(headers);
    this.contentDisposition = HttpUtils.extractContentDisposition(headers);
  }

  public HttpPart(Buffer bodyBuffer) {
    this.buffer = Buffer.buffer();
    List<String> headersList = new ArrayList<>();
    // We need to extract headers and content from buffer
    RecordParser parser = RecordParser.newDelimited("\r\n", new Handler<Buffer>() {
      int pos = 0;
      boolean startContent = false;

      @Override
      public void handle(Buffer frame) {
        if (frame.length() == 0) {
          if (pos > 0) {
            startContent = true;
          }
        } else {
          if (!startContent) {
            headersList.add(frame.toString().trim());
          } else {
            buffer.appendBuffer(frame);
          }
        }
        pos++;
      }
    });
    parser.handle(bodyBuffer);
    this.headers = new CaseInsensitiveHeaders();
    for (String header : headersList) {
      int offset = header.indexOf(":");
      this.headers.add(header.substring(0, offset), header.substring(offset + 1).trim());
    }

    this.contentType = HttpUtils.extractContentType(headers);
    this.contentDisposition = HttpUtils.extractContentDisposition(headers);

  }

  public Buffer getBuffer() {
    return buffer;
  }

  public MultiMap getHeaders() {
    return headers;
  }

  public String getMimeType() {
    return (contentType != null) ? contentType.getMimeType() : null;
  }

  public Charset getCharset() {
    return (contentType != null) ? contentType.getCharset() : null;
  }

  public String getDocumentUri() {
    return (contentDisposition != null) ? contentDisposition.getFilename() : null;
  }

  public String getFormat() {
    return (contentDisposition != null) ? contentDisposition.getFormat() : null;
  }

  public String getCategory() {
    return (contentDisposition != null) ? contentDisposition.getCategory() : null;
  }

  public boolean isContent() {
    return MarkLogicConstants.CONTENT.equals(contentDisposition.getCategory());
  }

  public boolean isMetadata() {
    return MarkLogicConstants.METADATA.equals(contentDisposition.getCategory());
  }

  public HttpPart buffer(Buffer buffer) {
    this.buffer = buffer;
    return this;
  }

  public HttpPart headers(MultiMap headers) {
    this.headers = headers;
    return this;
  }

  public Buffer toBuffer() {
    Buffer resultBuffer = Buffer.buffer();
    headers.entries().stream()
      .forEach(entry -> resultBuffer.appendString(entry.getKey()).appendString(": ").appendString(entry.getValue())
        .appendString(System.lineSeparator()));
    if (!headers.contains(HttpHeaders.CONTENT_LENGTH)) {
      resultBuffer.appendString(HttpHeaders.CONTENT_LENGTH.toString()).appendString(": ").appendInt(resultBuffer.length())
        .appendString(System.lineSeparator());
    }
    resultBuffer.appendString(System.lineSeparator());
    resultBuffer.appendBuffer(buffer);
    return resultBuffer;
  }
}
