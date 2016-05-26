/*
 *
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

/**
 * = Vert.x MarkLogic model API
 * :toc: left
 *
 * This classes are convenient to manipulate json objects.
 *
 * == MarkLogicConfig
 *
 * {@link org.etourdot.vertx.marklogic.MarkLogicConfig} manage configuration of MarkLogic connection.
 * A configuration object can be passed to MarkLogicClient, MarkLogicManagement or MarkLogicVerticle.
 *
 * Here a sample of usage with MarkLogicClient as java object:
 * [source, java]
 * ----
 *  MarkLogicConfig config = new MarkLogicConfig()
 *    .host("localhost")
 *    .port(8002)
 *    .user("admin")
 *    .password("admin")
 *    .authentication("digest");
 *  MarkLogicClient client = MarkLogicClient.create(vertx, config);
 * ----
 *
 * This is a sample of usage with MarkLogicVerticle in json format.
 * This object can be passed to verticle in config of deployment options:
 * [source, json]
 * ----
 * {
 *  "host": "localhost",
 *  "port": 8002,
 *  "user": "admin",
 *  "password": "admin",
 *  "authentication": "digest"
 * }
 * ----
 *
 * include::{@link org.etourdot.vertx.marklogic.model.client}[]
 *
 * include::{@link org.etourdot.vertx.marklogic.model.management}[]
 *
 */
@Document(fileName = "model.adoc")
@ModuleGen(name = "marklogic", groupPackage = "org.etourdot.vertx")
package org.etourdot.vertx.marklogic;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;
