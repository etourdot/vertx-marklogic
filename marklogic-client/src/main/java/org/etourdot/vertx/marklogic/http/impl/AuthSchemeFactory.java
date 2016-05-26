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
import org.etourdot.vertx.marklogic.http.utils.Realm;
import org.etourdot.vertx.marklogic.http.utils.DigestUtils;

public class AuthSchemeFactory {

  public static AuthScheme newScheme(final Realm realm, final AuthScheme.Type schemeType) {
    return createAuthScheme(realm, schemeType);
  }

  public static AuthScheme newScheme(final Realm realm) {
    return createAuthScheme(realm, realm.getSchemeType());
  }

  private static AuthScheme createAuthScheme(Realm realm, AuthScheme.Type schemeType) {
    switch (schemeType) {
      case BASIC:
        return new BasicAuthScheme(realm.getUser(), realm.getPassword());
      case DIGEST:
        return new DigestAuthScheme(realm.getUser(), new String(realm.getPassword().getBytes(DigestUtils.CHARACTER_SET)));
      case KERBEROS:
        break;
    }
    return null;
  }

}
