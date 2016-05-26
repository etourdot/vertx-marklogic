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

import org.etourdot.vertx.marklogic.http.AuthScheme;

import java.util.Base64;

public class BasicAuthScheme implements AuthScheme {
  private final String basicAuth;

  public BasicAuthScheme(final String user, final String password) {
    String authInfo = user + ":" + password;
    this.basicAuth = Base64.getEncoder().encodeToString(authInfo.getBytes());
  }
  @Override
  public Type getType() {
    return Type.BASIC;
  }

  @Override
  public String serialize() {
    return "Basic " + basicAuth;
  }

}
