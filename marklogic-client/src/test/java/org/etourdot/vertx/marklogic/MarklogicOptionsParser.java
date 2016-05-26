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

package org.etourdot.vertx.marklogic;

import com.marklogic.client.DatabaseClientFactory;

import static java.util.Objects.*;
import static java.util.Optional.*;

public class MarklogicOptionsParser {

  private final DatabaseClientFactory.Bean optionBean;

  public MarklogicOptionsParser(MarkLogicConfig config) {
    requireNonNull(config);
    optionBean = new DatabaseClientFactory.Bean();

    optionBean.setHost(config.getHost());
    optionBean.setPort(config.getPort());
    optionBean.setUser(config.getUser());
    optionBean.setPassword(config.getPassword());
    ofNullable(config.getAuthentication()).ifPresent(
      (s) -> optionBean.setAuthentication(DatabaseClientFactory.Authentication.valueOfUncased(s))
    );
  }

  public DatabaseClientFactory.Bean clientBean() {
    return optionBean;
  }
}
