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
import org.etourdot.vertx.marklogic.Format;
import org.etourdot.vertx.marklogic.MarkLogicConfig;
import org.etourdot.vertx.marklogic.MarkLogicConstants;
import org.etourdot.vertx.marklogic.http.MarkLogicRequest;
import org.etourdot.vertx.marklogic.http.MarklogicRestService;
import org.etourdot.vertx.marklogic.http.MultiPartRequest;
import org.etourdot.vertx.marklogic.http.ResourceNotFoundException;
import org.etourdot.vertx.marklogic.http.impl.DefaultRestService;
import org.etourdot.vertx.marklogic.http.impl.response.BulkResponse;
import org.etourdot.vertx.marklogic.http.impl.response.SearchResponse;
import org.etourdot.vertx.marklogic.model.client.Document;
import org.etourdot.vertx.marklogic.model.client.Documents;
import org.etourdot.vertx.marklogic.model.client.impl.DocumentsImpl;
import org.etourdot.vertx.marklogic.model.management.Permission;
import org.etourdot.vertx.marklogic.model.options.TransformOptions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Objects.*;

class MarkLogicDocumentImpl {

  private static final String DOCUMENTS_URL = "/v1/documents";
  private static final String DOCUMENTS = "documents";
  private static final String FORMAT = "format";
  private static final String CATEGORY = "category";
  private static final String TRANSFORM = "transform";
  private static final String EXTENSION = "extension";
  private static final String DIRECTORY = "directory";
  private static final String QUALITY = "quality";
  private static final String CONTENT = "content";
  private static final String COLLECTION = "collection";

  private Logger logger = LoggerFactory.getLogger(MarkLogicDocumentImpl.class);

  private final MarklogicRestService restService;

  public MarkLogicDocumentImpl(Vertx vertx, MarkLogicConfig config) {
    this.restService = new DefaultRestService.Builder(vertx).marklogicConfig(config).build();
  }

  public MarkLogicDocumentImpl(MarklogicRestService restService) {
    this.restService = restService;
  }

  void save(JsonObject documents, Handler<AsyncResult<JsonArray>> resultHandler) {
    saveAndTransform(documents, null, resultHandler);
  }

  void saveAndTransform(JsonObject documents, TransformOptions transformOptions, Handler<AsyncResult<JsonArray>> resultHandler) {
    requireNonNull(documents, "documents cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    try {
      if (documents.containsKey(DOCUMENTS)) {
        Documents docs = new DocumentsImpl(documents);
        saveDocuments(docs, transformOptions, resultHandler);
      } else {
        final Document doc = Document.create(documents);
        save(doc, transformOptions, resultHandler);
      }
      //Future.succeededFuture();
    } catch (Exception ex) {
      Future.failedFuture(ex);
    }
  }

  void exists(String docUri, Handler<AsyncResult<JsonObject>> resultHandler) {
    requireNonNull(docUri, "docUri cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    MarkLogicRequest marklogicRequest = restService.newMarklogicRequest();
    marklogicRequest.head(DOCUMENTS_URL)
      .addParam(MarkLogicConstants.URI, docUri)
      .execute(response -> {
        if (HttpResponseStatus.OK.code() == response.statusCode()) {
          Document document = Document.create();
          document.uri(docUri);
          Format format = response.getFormat();
          if (format != null) {
            document.format(format.getFormat());
          }
          resultHandler.handle(Future.succeededFuture(document.toJson()));
        } else {
          resultHandler.handle(Future.succeededFuture());
        }
      });
  }

  void read(String docUri, Handler<AsyncResult<JsonObject>> resultHandler) {
    requireNonNull(docUri, "docUri cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    readDocument(docUri, Arrays.asList(MarkLogicConstants.CONTENT, MarkLogicConstants.METADATA), resultHandler);
  }

  void readMetadata(String docId, Handler<AsyncResult<JsonObject>> resultHandler) {
    requireNonNull(docId, "docId cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    readDocument(docId, Collections.singletonList(MarkLogicConstants.METADATA), resultHandler);
  }


  void readMany(JsonArray docIds, Handler<AsyncResult<JsonArray>> resultHandler) {
    requireNonNull(resultHandler, "resultHandler cannot be null");

    readDocuments(docIds, Arrays.asList(MarkLogicConstants.CONTENT, MarkLogicConstants.METADATA), resultHandler);
  }

  void delete(String docId, Handler<AsyncResult<String>> resultHandler) {
    requireNonNull(docId, "docId cannot be null");
    requireNonNull(resultHandler, "resultHandler cannot be null");

    MarkLogicRequest marklogicRequest = restService.newMarklogicRequest();
    marklogicRequest.delete(DOCUMENTS_URL)
      .addParam(MarkLogicConstants.URI, docId)
      .execute(response -> {
        if (HttpResponseStatus.NO_CONTENT.code() == response.statusCode()) {
          resultHandler.handle(Future.succeededFuture(docId));
        }
      });
  }

  void readDocument(String docId, List<String> categories, Handler<AsyncResult<JsonObject>> resultHandler) {
    JsonArray docIds = new JsonArray(Collections.singletonList(docId));
    readDocuments(docIds, categories, (result) -> {
      if (result.failed()) {
        resultHandler.handle(Future.failedFuture(result.cause()));
      } else {
        resultHandler.handle(Future.succeededFuture(result.result().getJsonObject(0)));
      }
    });
  }

  void readDocuments(JsonArray docIds, List<String> categories, Handler<AsyncResult<JsonArray>> resultHandler) {
    MultiPartRequest marklogicRequest = restService.newMultipartRequest();
    marklogicRequest.get(DOCUMENTS_URL)
      .addParam(FORMAT, "json")
      .addParams(CATEGORY, categories);
    docIds.stream().forEach(docId -> marklogicRequest.addParam(MarkLogicConstants.URI, (String) docId));
    marklogicRequest.execute(response -> {
      SearchResponse searchResponse = new SearchResponse(response);
      searchResponse.endHandler(marklogicResponse -> {
        if (HttpResponseStatus.OK.code() == marklogicResponse.statusCode()) {
          JsonArray documents = new JsonArray();
          for (Document document : ((SearchResponse) marklogicResponse).getDocuments()) {
            documents.add(document.toJson());
          }
          resultHandler.handle(Future.succeededFuture(documents));
        }
        if (HttpResponseStatus.NOT_FOUND.code() == marklogicResponse.statusCode()) {
          resultHandler.handle(Future.failedFuture(new ResourceNotFoundException(marklogicResponse.statusMessage())));
        }
        resultHandler.handle(Future.succeededFuture());
      });
      searchResponse.process();
    });
  }

  void saveDocuments(Documents documents, TransformOptions transformOptions, Handler<AsyncResult<JsonArray>> resultHandler) {
    logger.debug("Start saveDocuments " + this);
    MultiPartRequest marklogicRequest = restService.newMultipartRequest();
    marklogicRequest.post(DOCUMENTS_URL);
    documents.getDocumentsList().stream().forEach(marklogicRequest::addDocument);
    if (transformOptions != null) {
      marklogicRequest.addParam(TRANSFORM, transformOptions.getName());
      if (transformOptions.hasParameters()) {
        for (Map.Entry<String, Object> param : transformOptions.getParameters().getMap().entrySet()) {
          marklogicRequest.addParam("trans:" + param.getKey(), (String) param.getValue());
        }
      }
    }
    logger.debug("saveDocuments request:" + marklogicRequest);
    marklogicRequest.execute(response -> {
      BulkResponse bulkResponse = new BulkResponse(response);
      bulkResponse.endHandler(marklogicResponse -> {
        logger.debug("saveDocuments request endhandler:" + marklogicRequest);
        if (HttpResponseStatus.OK.code() == marklogicResponse.statusCode()) {
          JsonArray resultDocs = new JsonArray();
          for (Document document : ((BulkResponse) marklogicResponse).getDocuments()) {
            resultDocs.add(document.getUri());
          }
          resultHandler.handle(Future.succeededFuture(resultDocs));
        }
      });
      bulkResponse.process();
    });
  }

  private void save(Document document, TransformOptions transformOptions, Handler<AsyncResult<JsonArray>> resultHandler) {
    final Format format = Format.valueOf(document.getFormat());
    MarkLogicRequest marklogicRequest = restService.newMarklogicRequest();
    if (document.hasUri()) {
      marklogicRequest.put(DOCUMENTS_URL)
        .addParam(MarkLogicConstants.URI, document.getUri())
        .withResponseHandler(response -> {
          if (HttpResponseStatus.CREATED.code() == response.statusCode()) {
            String uri = response.getDocumentUri();
            if (uri != null) {
              resultHandler.handle(Future.succeededFuture(new JsonArray().add(uri)));
            }
          } else if (HttpResponseStatus.NO_CONTENT.code() == response.statusCode()) {
            resultHandler.handle(Future.succeededFuture(new JsonArray().add(document.getUri())));
          }
        });
    } else if (document.isNewDocument()) {
      marklogicRequest.post(DOCUMENTS_URL)
        .addParam(EXTENSION, document.getExtension())
        .addParam(DIRECTORY, document.getDirectory())
        .withResponseHandler(response -> {
          if (HttpResponseStatus.CREATED.code() == response.statusCode()) {
            String uri = response.getDocumentUri();
            if (uri != null) {
              resultHandler.handle(Future.succeededFuture(new JsonArray().add(uri)));
            }
          }
        });
    }
    if (document.hasQuality()) {
      marklogicRequest.addParam(QUALITY, Integer.toString(document.getQuality()));
    }
    if (document.hasProperties()) {
      for (Map.Entry<String, Object> property : document.getProperties().getMap().entrySet()) {
        marklogicRequest.addParam("prop:" + property.getKey(), (String) property.getValue());
      }
    }
    if (document.hasPermissions()) {
      document.getPermissions().stream().forEach(perm -> {
        Permission permission = new Permission((JsonObject) perm);
        permission.getCapabilities().stream().forEach(capability -> marklogicRequest.addParam("perm:" + permission.getRoleName(),
          (String) capability));
      });
    }
    marklogicRequest.addParam(CATEGORY, CONTENT);
    if (document.hasCollections()) {
      document.getCollections().stream().forEach(coll -> marklogicRequest.addParam(COLLECTION, (String) coll));
    }
    marklogicRequest.addParam(FORMAT, format.getFormat());
    if (Format.JSON == format) {
      marklogicRequest.withBody(((JsonObject) document.getContent()));
    } else if (Format.XML == format) {
      marklogicRequest.withBody((String) document.getContent(), Format.XML);
    } else if (Format.TEXT == format) {
      marklogicRequest.withBody((String) document.getContent(), Format.TEXT);
    } else if (Format.BINARY == format) {
      marklogicRequest.withBody((String) document.getContent(), Format.TEXT);
    }
    if (transformOptions != null) {
      marklogicRequest.addParam(TRANSFORM, transformOptions.getName());
      if (transformOptions.hasParameters()) {
        for (Map.Entry<String, Object> param : transformOptions.getParameters().getMap().entrySet()) {
          marklogicRequest.addParam("trans:" + param.getKey(), (String) param.getValue());
        }
      }
    }
    marklogicRequest.execute();
  }

}

