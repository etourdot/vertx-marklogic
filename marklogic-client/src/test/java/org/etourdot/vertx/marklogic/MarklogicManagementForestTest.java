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
import org.etourdot.vertx.marklogic.model.options.ForestsOptions;
import org.junit.Test;

public class MarklogicManagementForestTest extends AbstractTestMarklogicDocument {

  @Test
  public void get_forest_conf_all() throws Exception {
    ForestsOptions forestsOption = new ForestsOptions();
    forestsOption.view("status");
    mlManagement.getForests(forestsOption, onSuccess(s -> {
      assertNotNull(s.getJsonObject("forest-status-list"));
      testComplete();
    }));
    await();
  }

  @Test
  public void get_forest_conf_one() throws Exception {
    ForestsOptions forestsOption = new ForestsOptions();
    forestsOption.name("Documents");
    forestsOption.view("counts");
    mlManagement.getForests(forestsOption, onSuccess(s -> {
      JsonObject counts = s.getJsonObject("forest-counts");
      assertNotNull(counts);
      assertEquals(counts.getString("name"), "Documents");
      testComplete();
    }));
    await();
  }

  @Test
  public void create_and_delete_forest() throws Exception {
    String host = getHost();
    ForestsOptions forestConfig = new ForestsOptions();
    forestConfig.forestName("VertxML-f01");
    forestConfig.host(host);
    mlManagement.createForest(forestConfig, onSuccess(c -> {
      assertNull(c);
      ForestsOptions forestsOption = new ForestsOptions();
      forestsOption.name("VertxML-f01");
      mlManagement.getForests(forestsOption, onSuccess(g -> {
        forestsOption.level("full");
        mlManagement.deleteForest(forestsOption, onSuccess(d -> {
          testComplete();
        }));
      }));
    }));
    await();
  }

  @Test
  public void forest_properties_change() throws Exception {
    ForestsOptions forestsOption = new ForestsOptions();
    forestsOption.name("Documents");
    mlManagement.getForestProps(forestsOption, onSuccess(s -> {
      System.out.println(s);
      testComplete();
    }));
    await();
  }

}
