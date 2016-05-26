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

package org.etourdot.vertx.marklogic.http.utils;

import java.nio.charset.Charset;

public final class ContentType {
  private String mimeType;
  private Charset charset;
  private String boundary;

  public ContentType(String mimeType, String charset, String boundary) {
    this.mimeType = mimeType;
    try {
      this.charset = Charset.forName(charset);
    } catch (Exception e) {
      this.charset = Charset.defaultCharset();
    }
    this.boundary = boundary;
  }

  public String getMimeType() {
    return mimeType;
  }

  public Charset getCharset() {
    return charset;
  }

  public String getBoundary() {
    return boundary;
  }

}