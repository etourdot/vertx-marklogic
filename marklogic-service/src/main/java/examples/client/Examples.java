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

package examples.client;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

public class Examples {

  public void example0_1_1(Vertx vertx, JsonObject config) {
    JsonObject save = new JsonObject().put("collection", "books")
      .put("document", new JsonObject().put("title", "The Hobbit"));

    vertx.eventBus().send("vertx.io.vertx.ext.marklogic", save,
      new DeliveryOptions().addHeader("action", "save"), saveResult -> {
        if (saveResult.succeeded()) {
          String id = (String) saveResult.result().body();
          System.out.println("Saved book with id " + id);
        } else {
          saveResult.cause().printStackTrace();
        }
      });
  }
}
