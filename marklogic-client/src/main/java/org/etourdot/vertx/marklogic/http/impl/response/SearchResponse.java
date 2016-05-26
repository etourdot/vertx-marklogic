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
import org.etourdot.vertx.marklogic.http.MarkLogicResponse;
import org.etourdot.vertx.marklogic.http.utils.Pagination;

import static org.etourdot.vertx.marklogic.http.utils.HttpUtils.extractPagination;

public class SearchResponse extends DefaultMultiPartResponse {

  private Pagination pagination;

  public SearchResponse(HttpClientResponse response) {
    super(response);
    this.pagination = extractPagination(response.headers());
  }

  public SearchResponse(MarkLogicResponse response) {
    super(response.getResponse());
    this.pagination = extractPagination(response.getResponse().headers());
  }

  public Long getStart() {
    if (pagination != null) {
      return pagination.getStart();
    }
    return 0L;
  }

  public Long getPageLen() {
    if (pagination != null) {
      return pagination.getPageLen();
    }
    return 0L;
  }

  public Long getEstimate() {
    if (pagination != null) {
      return pagination.getEstimate();
    }
    return 0L;
  }

  public Long getNbResults() {
    if (documents != null) {
      return (long) documents.size();
    }
    return 0L;
  }
}
