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

package org.etourdot.vertx.marklogic.model.client.impl;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.etourdot.vertx.marklogic.Format;
import org.etourdot.vertx.marklogic.model.client.Document;

import java.util.Objects;

import static java.util.Objects.*;
import static java.util.Optional.*;

@DataObject
public class DocumentImpl implements Document {
  private static final String URI = "uri";
  private static final String CONTENT = "content";
  private static final String COLLECTIONS = "collections";
  private static final String PERMISSIONS = "permissions";
  private static final String QUALITY = "quality";
  private static final String PROPERTIES = "properties";
  private static final String CONTENT_TYPE = "contentType";
  private static final String MIME_TYPE = "mime-type";
  private static final String EXTENSION = "extension";
  private static final String DIRECTORY = "directory";
  private static final String VERSION = "version";
  private static final String CATEGORY = "category";
  private static final String FORMAT = "format";
  private static final String EXTRACT = "extract";
  private static final String LANG = "lang";
  private static final String REPAIR = "repair";

  private String uri;
  private String contentType;
  private Object content;
  private JsonArray collections;
  private JsonArray permissions;
  private Integer quality;
  private JsonObject properties;

  private String extension;
  private String directory;
  private Long version;
  private String extract;
  private String lang;
  private String repair;
  private String format;
  private JsonArray category;

  public DocumentImpl() {
    this.category = new JsonArray();
  }

  public DocumentImpl(DocumentImpl document) {
    this();
    requireNonNull(document);

    ofNullable(document.uri).ifPresent(this::uri);
    ofNullable(document.content).ifPresent(this::contentObject);
    ofNullable(document.collections).ifPresent(this::collections);
    ofNullable(document.permissions).ifPresent(this::permissions);
    ofNullable(document.quality).ifPresent(this::quality);
    ofNullable(document.properties).ifPresent(this::properties);
    ofNullable(document.extension).ifPresent(this::extension);
    ofNullable(document.directory).ifPresent(this::directory);
    ofNullable(document.version).ifPresent(this::version);
    ofNullable(document.category).ifPresent(this::category);
    ofNullable(document.format).ifPresent(this::format);
    contentType(document.getContentType());
    forceFormatOrContentType();
    extract(document.getExtract());
    lang(document.getLang());
    repair(document.getRepair());
  }

  private void forceFormatOrContentType() {
    if (format == null && contentType == null) {
      throw new NullPointerException("unknown format");
    } else if (contentType != null) {
      format = Format.getFromMimetype(contentType).name();
    } else {
      contentType = Format.getValue(format).getDefaultMimetype();
    }
  }

  public DocumentImpl(JsonObject jsonObject) {
    this();
    requireNonNull(jsonObject);

    ofNullable(jsonObject.getString(URI)).ifPresent(this::uri);
    ofNullable(jsonObject.getValue(CONTENT)).ifPresent(this::contentObject);
    ofNullable(jsonObject.getJsonArray(COLLECTIONS)).ifPresent(this::collections);
    ofNullable(jsonObject.getJsonArray(PERMISSIONS)).ifPresent(this::permissions);
    ofNullable(jsonObject.getInteger(QUALITY)).ifPresent(this::quality);
    ofNullable(jsonObject.getJsonObject(PROPERTIES)).ifPresent(this::properties);
    ofNullable(jsonObject.getString(EXTENSION)).ifPresent(this::extension);
    ofNullable(jsonObject.getString(DIRECTORY)).ifPresent(this::directory);
    ofNullable(jsonObject.getLong(VERSION)).ifPresent(this::version);
    ofNullable(jsonObject.getString(EXTRACT)).ifPresent(this::extract);
    ofNullable(jsonObject.getString(LANG)).ifPresent(this::lang);
    ofNullable(jsonObject.getString(REPAIR)).ifPresent(this::repair);
    ofNullable(jsonObject.getString(FORMAT)).ifPresent(this::format);
    ofNullable(jsonObject.getString(MIME_TYPE)).ifPresent(this::contentType);
    if (!hasContentType()) {
      contentType(jsonObject.getString(CONTENT_TYPE));
    }
    forceFormatOrContentType();
  }

  @Override
  public JsonObject toJson() {
    JsonObject jsonObject = new JsonObject();
    ofNullable(uri).ifPresent(s -> jsonObject.put(URI, s));
    ofNullable(content).ifPresent(s -> jsonObject.put(CONTENT, s));
    ofNullable(collections).ifPresent(s -> jsonObject.put(COLLECTIONS, s));
    ofNullable(permissions).ifPresent(s -> jsonObject.put(PERMISSIONS, s));
    ofNullable(quality).ifPresent(s -> jsonObject.put(QUALITY, s));
    ofNullable(properties).ifPresent(s -> jsonObject.put(PROPERTIES, s));
    ofNullable(extension).ifPresent(s -> jsonObject.put(EXTENSION, s));
    ofNullable(directory).ifPresent(s -> jsonObject.put(DIRECTORY, s));
    forceFormatOrContentType();
    jsonObject.put(CONTENT_TYPE, getContentType());
    jsonObject.put(FORMAT, getFormat());
    return jsonObject;
  }

  /**
   * Uri of document
   * cf. <a href="https://docs.marklogic.com/REST/PUT/v1/documents">Documents</a>
   */
  @Override
  public String getUri() {
    return ofNullable(uri).get();
  }

  @Override
  public Document uri(String uri) {
    this.uri = uri;
    return this;
  }

  @Override
  public boolean hasUri() {
    return ofNullable(uri).isPresent();
  }

  /**
   * Content of document (json, xml, text or binary)
   */
  @Override
  public Object getContent() {
    return ofNullable(content).get();
  }

  private Document contentObject(Object content) {
    this.content = content;
    if (content != null) {
      this.category.add("content");
    }
    return this;
  }

  @Override
  public Document content(String content) {
    return contentObject(content);
  }

  @Override
  public Document content(Buffer content) {
    return contentObject(content.getBytes());
  }

  @Override
  public Document content(JsonObject content) {
    return contentObject(content);
  }

  @Override
  public boolean hasContent() {
    return ofNullable(content).isPresent();
  }

  /**
   * Collections of document
   * cf. <a href="https://docs.marklogic.com/REST/PUT/v1/documents">Documents</a>
   */
  @Override
  public JsonArray getCollections() {
    return ofNullable(collections).get();
  }

  @Override
  public Document collections(JsonArray collections) {
    this.collections = collections;
    if (collections != null) {
      this.category.add("collections");
    }
    return this;
  }

  @Override
  public boolean hasCollections() {
    return ofNullable(collections).isPresent();
  }

  /**
   * Permissions
   * cf. <a href="https://docs.marklogic.com/REST/PUT/v1/documents">Documents</a>
   */
  @Override
  public JsonArray getPermissions() {
    return ofNullable(permissions).get();
  }

  @Override
  public Document permissions(JsonArray permissions) {
    this.permissions = permissions;
    /*permissions.getList().stream().collect(toMap(Map.Entry::getKey, ))
    Map<String, Object> permMap = permissions.getMap();
    for (Map.Entry entry : permMap.entrySet()) {
      this.permissions.put((String) entry.getKey(), new JsonArray(((List<String>) entry.getValue())));
    }*/
    if (permissions != null) {
      this.category.add("permissions");
    }
    return this;
  }

  @Override
  public boolean hasPermissions() {
    return ofNullable(permissions).isPresent();
  }

  /**
   * Quality
   */
  @Override
  public Integer getQuality() {
    return ofNullable(quality).get();
  }

  @Override
  public Document quality(Integer quality) {
    this.quality = quality;
    if (quality != null) {
      this.category.add("quality");
    }
    return this;
  }

  @Override
  public boolean hasQuality() {
    return ofNullable(quality).isPresent();
  }

  /**
   * Content-type
   */
  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public Document contentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

  @Override
  public boolean hasContentType() { return ofNullable(contentType).isPresent(); }

  /**
   * Extension
   */
  @Override
  public String getExtension() {
    return ofNullable(extension).get();
  }

  @Override
  public Document extension(String extension) {
    this.extension = extension;
    return this;
  }

  @Override
  public boolean hasExtension() {
    return ofNullable(extension).isPresent();
  }

  /**
   * Directory
   */
  @Override
  public String getDirectory() {
    return ofNullable(directory).get();
  }

  @Override
  public Document directory(String directory) {
    this.directory = directory;
    return this;
  }

  @Override
  public boolean hasDirectory() {
    return ofNullable(directory).isPresent();
  }

  /**
   * Version
   */
  @Override
  public Long getVersion() {
    return ofNullable(version).orElse(0L);
  }

  @Override
  public Document version(Long version) {
    this.version = version;
    return this;
  }

  @Override
  public boolean hasVersion() {
    return ofNullable(version).isPresent();
  }

  /**
   * Format
   */
  @Override
  public String getFormat() {
    return format;
  }

  @Override
  public Document format(String format) {
    this.format = format;
    return this;
  }

  /**
   * Extract
   */
  @Override
  public String getExtract() {
    return extract;
  }

  @Override
  public Document extract(String extract) {
    this.extract = extract;
    return this;
  }

  /**
   * Lang
   */
  @Override
  public String getLang() {
    return lang;
  }

  @Override
  public Document lang(String lang) {
    this.lang = lang;
    return this;
  }

   /**
   * Repair
   */
  @Override
  public String getRepair() {
    return repair;
  }

  @Override
  public Document repair(String repair) {
    this.repair = repair;
    return this;
  }

   /**
   * Properties
   */
  @Override
  public JsonObject getProperties() {
    return ofNullable(this.properties).get();
  }

  @Override
  public Document properties(JsonObject properties) {
    this.properties = properties;
    return this;
  }

  @Override
  public boolean hasProperties() {
    return ofNullable(properties).isPresent();
  }

  @Override
  @GenIgnore
  public void addProperty(String name, Object value) {
    if (hasProperties()) {
      this.properties.put(name, value);
    } else {
      properties(new JsonObject().put(name, value));
    }
  }

  /**
   * Category
   */
  @Override
  public JsonArray getCategory() {
    return category;
  }

  @Override
  public Document category(JsonArray category) {
    this.category = category;
    return this;
  }

  @Override
  public boolean hasCategory() {
    return ofNullable(category).isPresent();
  }

  @Override
  @GenIgnore
  public void addCategory(String category) {
    this.category.add(category);
  }

  /**
   * Metadata
   */
  @Override
  public JsonObject getMetadata() {
    JsonObject metadata = new JsonObject();
    ofNullable(collections).ifPresent(s -> metadata.put(COLLECTIONS, s));
    ofNullable(permissions).ifPresent(s -> metadata.put(PERMISSIONS, s));
    ofNullable(properties).ifPresent(s -> metadata.put(PROPERTIES, s));
    ofNullable(quality).ifPresent(s -> metadata.put(QUALITY, s));
    return metadata;
  }

  @Override
  public boolean hasMetadata() {
    return hasCollections() || hasQuality() || hasPermissions() || hasProperties();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DocumentImpl document = (DocumentImpl) o;
    return Objects.equals(uri, document.uri) &&
      Objects.equals(contentType, document.contentType) &&
      Objects.equals(content, document.content) &&
      Objects.equals(collections, document.collections) &&
      Objects.equals(permissions, document.permissions) &&
      Objects.equals(quality, document.quality) &&
      Objects.equals(properties, document.properties) &&
      Objects.equals(extension, document.extension) &&
      Objects.equals(directory, document.directory) &&
      Objects.equals(version, document.version) &&
      Objects.equals(extract, document.extract) &&
      Objects.equals(lang, document.lang) &&
      Objects.equals(repair, document.repair) &&
      Objects.equals(format, document.format);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uri, contentType, content, collections, permissions, quality, properties,
      extension, directory, version, extract, lang, repair, format);
  }

  @Override
  @GenIgnore
  public boolean isNewDocument() {
    return !hasUri() && hasExtension() && hasDirectory();
  }

  @Override
  @GenIgnore
  public boolean isTemplateDocument() {
    return !hasUri() && !hasDirectory();
  }

  @Override
  @GenIgnore
  public boolean isJson() {
    return Format.JSON == Format.getValue(format);
  }

  @Override
  @GenIgnore
  public boolean isXML() {
    return Format.XML == Format.getValue(format);
  }

  @Override
  @GenIgnore
  public boolean isText() {
    return Format.TEXT == Format.getValue(format);
  }

  @Override
  @GenIgnore
  public boolean isBinary() {
    return Format.BINARY == Format.getValue(format);
  }

}
