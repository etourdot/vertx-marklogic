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

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpHeaders;
import org.etourdot.vertx.marklogic.Format;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HttpUtils {
  private static final String DOCUMENT_URI_PREFIX = "/documents?uri=";
  private static final Pattern CONTENTTYPE_REGEX_PATTERN = Pattern.compile("([^;]*)[;]?(?:\\s*charset=([^;]*))?[;]?(?:\\s*boundary=([^;]*))?.*",
    Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
  private static final Pattern DISPOSITION_REGEX_PATTERN = Pattern.compile("(?<TYPE>[^;]*)[;]?(?:\\s*filename=[\"]?(?<FILENAME>[^;^\"]*))[;\"]*(?:\\s*category=(?<CATEGORY>[^;]*))[;]?(?:\\s*format=(?<FORMAT>[^;]*)).*",
    Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
  public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
  public static final String VND_MARKLOGIC_DOCUMENT_FORMAT = "vnd.marklogic.document-format";
  public static final String VND_MARKLOGIC_START = "vnd.marklogic.start";
  public static final String VND_MARKLOGIC_PAGELEN = "vnd.marklogic.pageLength";
  public static final String VND_MARKLOGIC_ESTIMATE = "vnd.marklogic.result-estimate";

  public static ContentType extractContentType(MultiMap headers) {
    if (headers.contains(HttpHeaders.CONTENT_TYPE)) {
      Matcher regexMatcher = CONTENTTYPE_REGEX_PATTERN.matcher(headers.get(HttpHeaders.CONTENT_TYPE));
      if (regexMatcher.find()) {
          return new ContentType(regexMatcher.group(1), regexMatcher.group(2), regexMatcher.group(3));
      }
    }
    return null;
  }

  public static ContentDisposition extractContentDisposition(MultiMap headers) {
    if (headers.contains(HEADER_CONTENT_DISPOSITION)) {
      String disposition = headers.get(HEADER_CONTENT_DISPOSITION);
      Matcher regexMatcher = DISPOSITION_REGEX_PATTERN.matcher(disposition);
      if (regexMatcher.find()) {
        return new ContentDisposition(regexMatcher.group("TYPE"),
                 regexMatcher.group("FILENAME"),
                 regexMatcher.group("CATEGORY"),
                 regexMatcher.group("FORMAT"));
      }
    }
    return null;
  }

  public static long extractContentLength(MultiMap headers) {
    if (headers.contains(HttpHeaders.CONTENT_LENGTH)) {
      return Long.parseLong(headers.get(HttpHeaders.CONTENT_LENGTH));
    }
    return 0;
  }

  public static String extractDocumentUri(MultiMap headers) {
    if (headers.contains(HttpHeaders.LOCATION)) {
      String location = headers.get(HttpHeaders.LOCATION);
      return location.substring(location.indexOf(DOCUMENT_URI_PREFIX) + DOCUMENT_URI_PREFIX.length());
    } else if (headers.contains(HEADER_CONTENT_DISPOSITION)) {
      String disposition = headers.get(HEADER_CONTENT_DISPOSITION);
      Matcher regexMatcher = DISPOSITION_REGEX_PATTERN.matcher(disposition);
      if (regexMatcher.find()) {
        return regexMatcher.group(2);
      }
    }
    return null;
  }

  public static Format extractFormat(MultiMap headers) {
    if (headers.contains(VND_MARKLOGIC_DOCUMENT_FORMAT)) {
      return Format.getValue(headers.get(VND_MARKLOGIC_DOCUMENT_FORMAT));
    } else if (headers.contains(HEADER_CONTENT_DISPOSITION)) {
      String disposition = headers.get(HEADER_CONTENT_DISPOSITION);
      Matcher regexMatcher = DISPOSITION_REGEX_PATTERN.matcher(disposition);
      if (regexMatcher.find()) {
        return Format.getValue(regexMatcher.group(4));
      }
    }
    return null;
  }

  public static Pagination extractPagination(MultiMap headers) {
    Pagination pagination = new Pagination();
    if (headers.contains(VND_MARKLOGIC_START)) {
      pagination.setStart(Long.parseLong(headers.get(VND_MARKLOGIC_START)));
    }
    if (headers.contains(VND_MARKLOGIC_PAGELEN)) {
      pagination.setPageLen(Long.parseLong(headers.get(VND_MARKLOGIC_PAGELEN)));
    }
    if (headers.contains(VND_MARKLOGIC_ESTIMATE)) {
      pagination.setEstimate(Long.parseLong(headers.get(VND_MARKLOGIC_ESTIMATE)));
    }
    return pagination;
  }
}
