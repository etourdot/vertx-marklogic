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

package org.etourdot.vertx.marklogic.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.etourdot.vertx.marklogic.MarkLogicConfig;
import org.etourdot.vertx.marklogic.http.MarkLogicRequest;
import org.etourdot.vertx.marklogic.http.MarkLogicResponse;
import org.etourdot.vertx.marklogic.http.MarklogicRestService;
import org.etourdot.vertx.marklogic.http.MultiPartRequest;
import org.etourdot.vertx.marklogic.http.MultiPartResponse;
import org.etourdot.vertx.marklogic.http.ResourceNotFoundException;
import org.etourdot.vertx.marklogic.http.impl.DefaultRestService;
import org.etourdot.vertx.marklogic.http.impl.response.SearchResponse;
import org.etourdot.vertx.marklogic.model.client.Document;
import org.etourdot.vertx.marklogic.model.options.DeleteOptions;
import org.etourdot.vertx.marklogic.model.options.SearchOptions;

import static java.util.Objects.*;

class MarkLogicSearchImpl {

  private static final String SEARCH_URL = "/v1/search";
  private static final String QBE_URL = "/v1/qbe";
  private static final String VALUES_URL = "/v1/values";
  private static final String DIRECTORY = "directory";
  private static final String COLLECTION = "collection";
  private static final String TXID = "txid";
  private static final String START = "start";
  private static final String PAGE_LENGTH = "pageLength";
  private static final String VIEW = "view";
  private static final String CATEGORY = "category";
  private static final String STRUCTURED_QUERY = "structuredQuery";
  private static final String QUERY = "query";

  private Logger logger = LoggerFactory.getLogger(MarkLogicSearchImpl.class);

  private final MarklogicRestService restService;

  public MarkLogicSearchImpl(Vertx vertx, MarkLogicConfig config) {
    this.restService = new DefaultRestService.Builder(vertx).marklogicConfig(config).build();
  }

  public MarkLogicSearchImpl(MarklogicRestService restService) {
    this.restService = restService;
  }

  void search(SearchOptions searchOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
    requireNonNull(searchOptions, "searchOptions cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

      MarkLogicRequest marklogicRequest = restService.newMarklogicRequest();
      if (searchOptions.hasQbe()) {
        marklogicRequest.get(QBE_URL);
      } else {
        marklogicRequest.get(SEARCH_URL);
      }
      putSearchOptions(searchOptions, marklogicRequest);
      marklogicRequest.execute(response -> {
        if (HttpResponseStatus.OK.code() == response.statusCode()) {
          response.contentHandler(buffer -> resultHandler.handle(Future.succeededFuture(buffer.toJsonObject())));
          //response.bodyHandler(buffer -> resultHandler.handle(Future.succeededFuture(buffer.toJsonObject())));
        }
      });

  }

  void searchDocuments(SearchOptions searchOptions, Handler<AsyncResult<JsonArray>> resultHandler) {
    requireNonNull(searchOptions, "searchOptions cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    doSeachDocPage(searchOptions, marklogicResponse -> {
      JsonArray documents = new JsonArray();
      if (HttpResponseStatus.OK.code() == marklogicResponse.statusCode()) {
        for (Document document : ((MultiPartResponse) marklogicResponse).getDocuments()) {
          documents.add(document.toJson());
        }
        resultHandler.handle(Future.succeededFuture(documents));
      }
      if (HttpResponseStatus.NOT_FOUND.code() == marklogicResponse.statusCode()) {
        resultHandler.handle(Future.failedFuture(new ResourceNotFoundException(marklogicResponse.statusMessage())));
      }
      resultHandler.handle(Future.succeededFuture());
    });
  }

  void searchBatch(SearchOptions searchOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
    requireNonNull(searchOptions, "searchOptions cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    doSeachDocPage(searchOptions, marklogicResponse -> {
      if (HttpResponseStatus.OK.code() == marklogicResponse.statusCode()) {
        SearchResponse searchResponse = (SearchResponse) marklogicResponse;
        for (Document document : searchResponse.getDocuments()) {
          resultHandler.handle(Future.succeededFuture(document.toJson()));
        }
        if (searchResponse.getStart() + searchResponse.getNbResults() < searchResponse.getEstimate()) {
          searchOptions.start(searchResponse.getStart() + searchResponse.getPageLen());
          searchBatch(searchOptions, resultHandler);
        }
      }
      if (HttpResponseStatus.NOT_FOUND.code() == marklogicResponse.statusCode()) {
        resultHandler.handle(Future.failedFuture(new ResourceNotFoundException(marklogicResponse.statusMessage())));
      }
    });
  }

  void delete(DeleteOptions deleteOptions, Handler<AsyncResult<JsonObject>> resultHandler) {
      requireNonNull(deleteOptions, "deleteOptions cannot be null");
      requireNonNull(resultHandler, "resultHandler cannot be null");

    MarkLogicRequest request = restService.newMarklogicRequest();
    request.delete(SEARCH_URL);
    if (deleteOptions.hasDirectory()) {
      request.addParam(DIRECTORY, deleteOptions.getDirectory());
    }
    if (deleteOptions.hasCollection()) {
      request.addParam(COLLECTION, deleteOptions.getCollection());
    }
    if (deleteOptions.hasTxId()) {
      request.addParam(TXID, deleteOptions.getTxId());
    }
    request.execute(response -> {
      if (HttpResponseStatus.NO_CONTENT.code() == response.statusCode()) {
        resultHandler.handle(Future.succeededFuture());
      } else {
        resultHandler.handle(Future.failedFuture(response.statusMessage()));
      }
    });
  }

  private void doSeachDocPage(SearchOptions searchOptions, Handler<MarkLogicResponse> marklogicResponseHandler) {
    MultiPartRequest marklogicRequest = restService.newMultipartRequest();
    if (searchOptions.hasQbe()) {
      marklogicRequest.get(QBE_URL);
    } else {
      marklogicRequest.get(SEARCH_URL);
    }
    putSearchOptions(searchOptions, marklogicRequest);
    marklogicRequest.execute(response -> {
      SearchResponse searchResponse = new SearchResponse(response);
      searchResponse.endHandler(marklogicResponseHandler);
      searchResponse.process();
    });
  }

  private static void putSearchOptions(SearchOptions options, MarkLogicRequest request) {
    if (options.hasExpression()) {
      request.addParam("q", options.getExpression());
    }
    if (options.hasStart()) {
      request.addParam(START, options.getStart().toString());
    }
    if (options.hasPageLen()) {
      request.addParam(PAGE_LENGTH, options.getPageLen().toString());
    }
    if (options.hasView()) {
      request.addParam(VIEW, options.getView());
    }
    if (options.hasDirectory()) {
      request.addParam(DIRECTORY, options.getDirectory());
    }
    if (options.hasCategories()) {
      options.getCategories().stream().forEach(category -> request.addParam(CATEGORY, (String) category));
    }
    if (options.hasCollections()) {
      options.getCollections().stream().forEach(collection -> request.addParam(COLLECTION, (String) collection));
    }
    if (options.hasStructuredQuery()) {
      request.addParam(STRUCTURED_QUERY, options.getStructuredQuery().encode());
    }
    if (options.hasQbe()) {
      request.addParam(QUERY, options.getQbe().encode());
    }
  }
}
