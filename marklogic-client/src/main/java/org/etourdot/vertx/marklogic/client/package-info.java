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
 */

/**
 * = Vert.x MarkLogic Client
 *
 * A Vert.x 3 client allowing applications to interact with a MarkLogic cluster for CRUD operations
 * (save, retrieve or delete) on documents and simple or complexe search.
 *
 * *Features*
 *
 * * Completely non-blocking
 * * Supports a majority of the crud options including transactions and multiple document writing.
 * * Support basic and digest connection to the MarkLogic REST API.
 *
 * == Using Vert.x MarkLogic Client
 *
 * To use this project, add the following dependency to the _dependencies_ section of your build descriptor:
 *
 * * Maven (in your `pom.xml`):
 *
 * [source,xml,subs="+attributes"]
 * ----
 * <dependency>
 *   <groupId>${maven.groupId}</groupId>
 *   <artifactId>${maven.artifactId}</artifactId>
 *   <version>${maven.version}</version>
 * </dependency>
 * ----
 *
 * * Gradle (in your `build.gradle` file):
 *
 * [source,groovy,subs="+attributes"]
 * ----
 * compile ${maven.groupId}:${maven.artifactId}:${maven.version}
 * ----
 *
 *
 * == Creating a client
 * [source,java]
 * ----
 * {@link examples.client.Examples#exampleCreate}
 * ----
 *
 * == Using the API
 *
 * The client API is represented by {@link org.etourdot.vertx.marklogic.client.MarkLogicClient}.
 *
 * === Saving documents
 *
 * To save one or more documents you use {@link org.etourdot.vertx.marklogic.client.MarkLogicClient#save}.
 *
 * If the \"documents\" parameter contains more than one document, Marklogic multipart API will be use to save
 * all those documents into a single http request.
 *
 * If document contains an uri and document exists, the save operation will replace found document.
 * If document contains an uri and document doesnt exists or if document contains directory and extension,
 * the save operation will create a document.
 *
 * Here's an example of saving a document and getting the id back
 *
 * [source,$lang]
 * ----
 * {@link examples.client.Examples#exampleSaveDocument}
 * ----
 *
 * Here's an example of saving multi documents and getting the id back
 *
 *  [source,$lang]
 * ----
 * {@link examples.client.Examples#exampleSaveDocuments}
 * ----
 **/

@Document(fileName = "client.adoc")
@ModuleGen(name = "marklogic-client", groupPackage = "org.etourdot.vertx")
package org.etourdot.vertx.marklogic.client;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;
