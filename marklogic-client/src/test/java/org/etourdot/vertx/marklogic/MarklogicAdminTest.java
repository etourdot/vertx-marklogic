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

import org.etourdot.vertx.marklogic.model.options.RestApiOptions;
import org.junit.Test;

public class MarklogicAdminTest extends AbstractTestMarklogicDocument {

     /*@Test
    public void make_server() throws Exception {
        String instanceName = "testserveur5";
        JsonObject restApi = new JsonObject();
        restApi.put("name", instanceName);
        restApi.put("database", "Documents");
        restApi.put("modules-database", "Modules");
        restApi.put("port", "8101");
        RestApiOption restApiOption = new RestApiOption();
        restApiOption.restApi(restApi);
        marklogicAdmin.createRESTAppServer(restApiOption.toJson(), onSuccess(s -> {
            RestApiOption restApiOption2 = new RestApiOption();
            restApiOption2.retrieveInstance(instanceName);
            marklogicAdmin.getRESTAppServerConfig(restApiOption2.toJson(), onSuccess(s1 -> {
                assertEquals("Modules", s1.getString("modules-database"));
                RestApiOption restApiOption3 = new RestApiOption();
                restApiOption3.retrieveInstance(s1.getString("name"));
                marklogicAdmin.deleteRESTAppServer(restApiOption3.toJson(), onSuccess(s2 -> {
                    assertEquals("deleted", s2);
                    testComplete();
                }));
                testComplete();
            }));
        }));
        await();
    }*/

  @Test
  public void get_server_conf_all() throws Exception {
    RestApiOptions restApiOption = new RestApiOptions();
    mlManagement.getRESTAppServerConfig(restApiOption, onSuccess(s -> {
      assertTrue(s.getJsonArray("rest-apis").size() > 2);
      testComplete();
    }));
    await();
  }

  @Test
  public void get_server_conf_one() throws Exception {
    RestApiOptions restApiOption = new RestApiOptions();
    restApiOption.retrieveInstance("App-Services");
    mlManagement.getRESTAppServerConfig(restApiOption, onSuccess(s -> testComplete()));
    await();
  }

}
