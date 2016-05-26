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

import org.etourdot.vertx.marklogic.http.MarkLogicResponse;

public class ErrorResponse implements MarkLogicResponse {

  private String message;

  public ErrorResponse(Throwable throwable) {
    this.message = throwable.getMessage();
  }

  @Override
  public int statusCode() {
    return -1;
  }

  @Override
  public String statusMessage() {
    return message;
  }

}
