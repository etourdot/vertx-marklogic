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

import io.vertx.core.json.JsonObject;
import org.etourdot.vertx.marklogic.model.options.DatabasesOptions;
import org.junit.Test;

public class MarklogicManagementDatabaseTest extends AbstractTestMarklogicDocument {

  @Test
  public void get_database_conf_all() throws Exception {
    DatabasesOptions dbOption = new DatabasesOptions();
    mlManagement.getDatabases(dbOption, onSuccess(s -> {
      assertNotNull(s.getJsonObject("database-default-list"));
      testComplete();
    }));
    await();
  }

  @Test
  public void get_database_conf_one() throws Exception {
    DatabasesOptions dbOption = new DatabasesOptions();
    dbOption.name("Documents");
    mlManagement.getDatabases(dbOption, onSuccess(s -> {
      System.out.println(s);
      JsonObject db = s.getJsonObject("database-default");
      assertNotNull(db);
      assertEquals(db.getString("name"), "Documents");
      testComplete();
    }));
    await();
  }

}
