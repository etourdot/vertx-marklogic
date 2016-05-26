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

import io.vertx.codegen.annotations.VertxGen;

@VertxGen
public enum Format {
  BINARY,
  JSON,
  TEXT,
  XML,
  UNKNOWN;

  Format() {
  }

  public String getDefaultMimetype() {
    switch (this) {
      case UNKNOWN:
        return null;
      case BINARY:
        return "application/octet-stream";
      case JSON:
        return "application/json";
      case TEXT:
        return "text/plain";
      case XML:
        return "application/xml";
      default:
        throw new RuntimeException("Unknown format " + this.toString());
    }
  }

  public static Format getFromMimetype(String mimeType) {
    if (mimeType == null) return UNKNOWN;
    else if ("application/xml".equals(mimeType)) return XML;
    else if ("text/xml".equals(mimeType)) return XML;
    else if ("application/json".equals(mimeType)) return JSON;
    else if ("application/octet-stream".equals(mimeType)) return BINARY;
    else if (mimeType.startsWith("text/")) return TEXT;
    else return UNKNOWN;
  }

  public static Format getValue(String name) {
    return valueOf(name.toUpperCase());
  }

  public String getFormat() {
    return this.name().toLowerCase();
  }
}