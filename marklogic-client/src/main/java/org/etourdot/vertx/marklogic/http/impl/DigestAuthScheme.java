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
import org.etourdot.vertx.marklogic.http.utils.DigestUtils;

import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DigestAuthScheme implements AuthScheme {
  private static final String USERNAME = "username";
  private static final String REALM = "realm";
  private static final String NONCE = "nonce";
  private static final String OPAQUE = "opaque";
  private static final String ALGORITHM = "algorithm";
  private static final String QOP = "qop";
  private static final String URI = "uri";
  private static final String CNONCE = "cnonce";
  private static final String NC = "nc";
  private static final String RESPONSE = "response";
  private final Pattern KEY_VALUE_PAIR_PATTERN = Pattern.compile("(\\w+)\\s*=\\s*(\"([^\"]+)\"|(\\w+))\\s*,?\\s*");

  private String realm;
  private String nonce;
  private String opaque;
  private DigestUtils.Algorithm algorithm = DigestUtils.Algorithm.UNSPECIFIED;
  private DigestUtils.QOP qop;
  private volatile int nc;
  private String ncStr;
  private String user;
  private String password;
  private String uri;
  private String cnonce;
  private String response;

  DigestAuthScheme(final String user, final String password) {
    this.user = user;
    this.password = password;
  }

  @Override
  public Type getType() {
    return Type.DIGEST;
  }

  @Override
  public void schemeParts(String[] schemeParts) {
    this.uri = schemeParts[2];
    final Matcher match = KEY_VALUE_PAIR_PATTERN.matcher(schemeParts[0]);
    while (match.find()) {
      final int nbGroups = match.groupCount();
      if (nbGroups != 4) {
        continue;
      }
      final String key = match.group(1);
      final String valNoQuotes = match.group(3);
      final String valQuotes = match.group(4);
      final String val = (valNoQuotes == null) ? valQuotes : valNoQuotes;
      switch (key) {
        case QOP:
          this.qop = DigestUtils.QOP.parse(val);
          break;
        case REALM:
          this.realm = val;
          break;
        case NONCE:
          this.nonce = val;
          break;
        case OPAQUE:
          this.opaque = val;
          break;
        case ALGORITHM:
          algorithm = DigestUtils.Algorithm.parse(val);
          break;
      }
    }

    try {
      final String ha1, ha2;
      if (DigestUtils.Algorithm.MD5_SESS == this.algorithm) {
        ha1 = DigestUtils.md5(DigestUtils.md5(this.user, this.realm, this.password));
      } else {
        ha1 = DigestUtils.md5(this.user, this.realm, this.password);
      }

      ha2 = DigestUtils.md5(schemeParts[1], this.uri);

      if (DigestUtils.QOP.UNSPECIFIED == this.qop) {
        this.response = DigestUtils.md5(ha1, this.nonce, ha2);
      } else {
        this.ncStr = String.format("%08x", incrementCounter());
        this.cnonce = DigestUtils.generateSalt(DigestUtils.CLIENT_NONCE_BYTE_COUNT);
        this.response = DigestUtils.md5(ha1, this.nonce, ncStr, cnonce, this.qop.toString(), ha2);
      }
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  public int incrementCounter() {
    return ++nc;
  }

  @Override
  public String serialize() {
    final StringBuilder sb = new StringBuilder(100);
    sb.append("Digest ");
    DigestUtils.append(sb, USERNAME, this.user);
    DigestUtils.append(sb, REALM, this.realm);
    DigestUtils.append(sb, NONCE, this.nonce);
    DigestUtils.append(sb, OPAQUE, this.opaque);
    DigestUtils.append(sb, ALGORITHM, this.algorithm.toString(), false);
    DigestUtils.append(sb, QOP, this.qop.toString(), false);
    DigestUtils.append(sb, URI, this.uri);
    DigestUtils.append(sb, CNONCE, this.cnonce);
    DigestUtils.append(sb, NC, ncStr, false);
    DigestUtils.append(sb, RESPONSE, this.response);
    return sb.toString();
  }

}
