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

package org.etourdot.vertx.marklogic.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.etourdot.vertx.marklogic.MarkLogicConfig;
import io.vertx.serviceproxy.ProxyHelper;

public class MarkLogicClientVerticle extends AbstractVerticle {

  public static final String ML_CLIENT_ADDRESS = "ml.client.address";
  private Logger logger = LoggerFactory.getLogger(MarkLogicClientVerticle.class);

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    logger.debug("Start deploying MarkLogicClientVerticle");
    MarkLogicConfig config = new MarkLogicConfig(config());
    MarkLogicClientService clientService = new MarkLogicClientServiceImpl(MarkLogicClient.create(vertx, config));
    clientService.availability(result -> {
      if (result.succeeded()) {
        // And register them on the event bus against the configured addresses
        logger.debug("Register clientService " + clientService + " on " + ML_CLIENT_ADDRESS);
        ProxyHelper.registerService(MarkLogicClientService.class, vertx, clientService, ML_CLIENT_ADDRESS);
        startFuture.complete();
      } else {
        startFuture.fail("Unable to start MarkLogicClientVerticle: " + result.cause().getMessage());
      }
    });
    logger.debug("End deploying MarkLogicClientVerticle");
  }
}
