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

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DigestUtils {
  public static final Charset CHARACTER_SET = Charset.forName("iso-8859-1");
  public static final int CLIENT_NONCE_BYTE_COUNT = 4;
  private static SecureRandom randomGenerator;

  static {
    try {
      randomGenerator = SecureRandom.getInstance("SHA1PRNG");
    } catch (final NoSuchAlgorithmException e) {
      System.out.println(e.getLocalizedMessage());
      throw new RuntimeException(e);
    }
  }

  private DigestUtils() {
  }

  public static void append(final StringBuilder sb, final String key, final String value, final boolean useQuote) {
    if (value == null) {
      return;
    }
    if (sb.length() > 0) {
      if (sb.charAt(sb.length() - 1) != ' ') {
        sb.append(',');
      }
    }
    sb.append(key);
    sb.append('=');
    if (useQuote) {
      sb.append('"');
    }
    sb.append(value);
    if (useQuote) {
      sb.append('"');
    }
  }

  public static void append(final StringBuilder sb, final String key, final String value) {
    append(sb, key, value, true);
  }

  public static String md5(final String... tokens) throws NoSuchAlgorithmException {
    String joinTokens = Stream.of(tokens).collect(Collectors.joining(":"));
    final MessageDigest md = MessageDigest.getInstance("MD5");
    md.reset();
    return DatatypeConverter.printHexBinary(md.digest(joinTokens.getBytes(CHARACTER_SET))).toLowerCase();
  }

  public static String generateSalt(int clientNonceByteCount) {
    final byte[] salt = new byte[clientNonceByteCount];
    randomGenerator.nextBytes(salt);
    return DatatypeConverter.printHexBinary(salt).toLowerCase();
  }

  public enum Algorithm {

    UNSPECIFIED(null),
    MD5("MD5"),
    MD5_SESS("MD5-sess");
    private final String md;

    Algorithm(final String md) {
      this.md = md;
    }

    @Override
    public String toString() {
      return md;
    }

    public static Algorithm parse(String val) {
      if (val == null || val.isEmpty()) {
        return Algorithm.UNSPECIFIED;
      }
      val = val.trim();
      if (val.contains(MD5_SESS.md) || val.contains(MD5_SESS.md.toLowerCase())) {
        return MD5_SESS;
      }
      return MD5;
    }
  }

  public enum QOP {

    UNSPECIFIED(null),
    AUTH("auth");

    private final String qop;

    QOP(final String qop) {
      this.qop = qop;
    }

    @Override
    public String toString() {
      return qop;
    }

    public static QOP parse(final String val) {
      if (val == null || val.isEmpty()) {
        return QOP.UNSPECIFIED;
      }
      if (val.contains("auth")) {
        return QOP.AUTH;
      }
      throw new UnsupportedOperationException("DIGEST_FILTER_QOP_UNSUPPORTED");
    }
  }
}
