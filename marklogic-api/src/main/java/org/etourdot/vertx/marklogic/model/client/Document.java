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

package org.etourdot.vertx.marklogic.model.client;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.etourdot.vertx.marklogic.model.client.impl.DocumentImpl;

/**
 * Document class
 */
@VertxGen
public interface Document {
  static Document create() {
    return new DocumentImpl();
  }

  static Document create(JsonObject jsonObject) {
    return new DocumentImpl(jsonObject);
  }

  /**
   * Convert document to {@link JsonObject}.
   *
   * @return json representation.
   */
  JsonObject toJson();

  /**
   * Return document's uri.
   *
   * @return uri
   */
  String getUri();

  /**
   * Set document uri.
   *
   * @param uri
   */
  @Fluent
  Document uri(String uri);

  /**
   * Check if document has an uri.
   *
   * @return true if document has an uri.
   */
  boolean hasUri();

  @GenIgnore
  Object getContent();

  /**
   * Set document content in {@link String} format.
   *
   * @param content
   * @return {@link Document}
   */
  @Fluent
  Document content(String content);

  /**
   * Set document content in {@link Buffer} format for binary purpose.
   *
   * @param content
   * @return {@link Document}
   */
  @Fluent
  Document content(Buffer content);

  /**
   * Set document content in {@link JsonObject} format
   *
   * @param content
   * @return {@link Document}
   */
  @Fluent
  Document content(JsonObject content);

  /**
   * Check if document has a content.
   *
   * @return true if document ha a content.
   */
  boolean hasContent();

  /**
   * Get document's collections.
   *
   * @return an array of collections (strings).
   */
  JsonArray getCollections();

  /**
   * Set document's collections.
   *
   * @param collections array of collections (string)
   * @return {@link Document}
   */
  @Fluent
  Document collections(JsonArray collections);

  /**
   * Check if document has collections.
   *
   * @return true if document has collections.
   */
  boolean hasCollections();

  /**
   * Get document's permissions.
   *
   * @return an array of {@link org.etourdot.vertx.marklogic.model.management.Permission}.
   */
  JsonArray getPermissions();

  /**
   * Set document's permissions.
   *
   * @param permissions array of {@link org.etourdot.vertx.marklogic.model.management.Permission}.
   * @return {@link Document}
   */
  @Fluent
  Document permissions(JsonArray permissions);

  /**
   * Check if document has permissions.
   *
   * @return true if document has permissions.
   */
  boolean hasPermissions();

  /**
   * Get document's quality.
   *
   * @return quality number.
   */
  Integer getQuality();

  /**
   * Set document's quality.
   *
   * @param quality
   * @return {@link Document}
   */
  @Fluent
  Document quality(Integer quality);

  /**
   * Check if document has quality.
   *
   * @return true if document has quality.
   */
  boolean hasQuality();

  /**
   * Get document's content type.
   *
   * @return content typee
   */
  String getContentType();

  /**
   * Set document's content type.
   *
   * @param contentType
   * @return {@link Document}
   */
  @Fluent
  Document contentType(String contentType);

  /**
   * Check if document has a content type.
   *
   * @return true if document has a content type.
   */
  boolean hasContentType();

  /**
   * Get document's extension.
   *
   * @return extension.
   */
  String getExtension();

  /**
   * Set documents's extension.
   *
   * @param extension
   * @return {@link Document}
   */
  @Fluent
  Document extension(String extension);

  /**
   * Check if document has an extension.
   *
   * @return true if document has an extension.
   */
  boolean hasExtension();

  /**
   * Get document's directory.
   *
   * @return
   */
  String getDirectory();

  /**
   * Set document's directory
   *
   * @param directory
   * @return {@link Document}
   */
  @Fluent
  Document directory(String directory);

  /**
   * Check if document has a directory.
   *
   * @return true if document has a directory.
   */
  boolean hasDirectory();

  /**
   * Get document's version.
   *
   * @return version number
   */
  Long getVersion();

  /**
   * Set document's version.
   *
   * @param version
   * @return {@link Document}
   */
  @Fluent
  Document version(Long version);

  /**
   * Check if document has a version.
   *
   * @return true if document has a version.
   */
  boolean hasVersion();

  /**
   * Get document's format.
   * @return format
   */
  String getFormat();

  /**
   * Set document's format.
   *
   * @param format
   * @return {@link Document}
   */
  @Fluent
  Document format(String format);

  /**
   * Get document's extract.
   *
   * @return extract.
   */
  String getExtract();

  /**
   * Set document's extract.
   *
   * @param extract
   * @return {@link Document}
   */
  @Fluent
  Document extract(String extract);

  /**
   * Get document's lang.
   *
   * @return lang
   */
  String getLang();

  /**
   * Set document's lang.
   *
   * @param lang
   * @return {@link Document}
   */
  @Fluent
  Document lang(String lang);

  /**
   * Get document's repair.
   *
   * @return {@link Document}
   */
  String getRepair();

  /**
   * Set document's repair.
   *
   * @param repair
   * @return {@link Document}
   */
  @Fluent
  Document repair(String repair);

  /**
   * Get document's properties.
   *
   * @return properties.
   */
  JsonObject getProperties();

  /**
   * Set document's properties.
   *
   * @param properties
   * @return {@link Document}
   */
  @Fluent
  Document properties(JsonObject properties);

  /**
   * Check if document has properties.
   * @return true if document has properties.
   */
  boolean hasProperties();

  /**
   * Add a property.
   *
   * @param name of property.
   * @param value of property.
   */
  @GenIgnore
  void addProperty(String name, Object value);

  /**
   * Get document's categories.
   *
   * @return {@link JsonArray} of categories (string)
   */
  JsonArray getCategory();

  /**
   * Set document's categories.
   *
   * @param category
   * @return {@link Document}
   */
  @Fluent
  Document category(JsonArray category);

  /**
   * Check if document has categories.
   * @return true if document has categories.
   */
  boolean hasCategory();

  /**
   * Add a category.
   *
   * @param category
   */
  @GenIgnore
  void addCategory(String category);

  /**
   * Get document's metadata.
   *
   * @return metadata
   */
  JsonObject getMetadata();

  /**
   * Check if document has metadata.
   * @return true if document has metadata.
   */
  boolean hasMetadata();

  @GenIgnore
  boolean isNewDocument();

  @GenIgnore
  boolean isTemplateDocument();

  /**
   * Check if document is a json document.
   * @return true if document is a json document.
   */
  @GenIgnore
  boolean isJson();

  /**
   * Check if document is a xml document.
   * @return true if document is a xml document.
   */
  @GenIgnore
  boolean isXML();

  /**
   * Check if document is a text document.
   * @return true if document is a text document.
   */
  @GenIgnore
  boolean isText();

  /**
   * Check if document is a binary document.
   * @return true if document is a binary document.
   */
  @GenIgnore
  boolean isBinary();
}
