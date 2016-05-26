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
 * == MarkLogic Client model API
 *
 * === Document
 *
 * {@link org.etourdot.vertx.marklogic.model.client.Document} is class used to manipulate MarkLogic Document (of course).
 * It encapsulate datas and metadatas of document including format (or content-type), uri (or directory and extension),
 * properties ...
 *
 * Here is an example of a json format document:
 * [source, json]
 * ----
 * {
 *   "content" : {
 *     "foo" : "bar",
 *     "num" : 123,
 *     "big" : true,
 *     "other" : {
 *       "quux" : "flib",
 *       "myarr" : [ "blah", true, 312 ]
 *     }
 *   },
 *   "collections" : [ "coll/c1", "coll/c2" ],
 *   "quality" : 2,
 *   "properties" : {
 *     "aProp1" : "aValue1",
 *     "aProp2" : "aValue2"
 *   },
 *   "uri" : "/jsondocs/doc_uri.json",
 *   "contentType" : "application/json",
 *   "permissions" : [{"role-name":"app-user", "capabilities": ["update"]}]
 * }
 * ----
 *
 * Here is another example of a xml format document:
 * [source, json]
 * ----
 * {
 *   "content" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<sources><country>France</country><id>538e2bd5e4b0c61eded74330</id><lat>48.865877</lat><lng>2.36374</lng></sources>",
 *   "collections" : [ "coll/c1", "coll/c2" ],
 *   "quality" : 2,
 *   "properties" : {
 *     "aProp1" : "aValue1",
 *     "aProp2" : "aValue2"
 *   },
 *   "uri" : "/xmldocs/doc_uri.xml",
 *   "contentType" : "application/xml",
 *   "permissions" : [{"role-name":"app-user", "capabilities": ["update"]}]
 * }
 * ----
 *
 * Here is an exemple of text format document:
 * [source, json]
 * ----
 * {"uri":"/testHVBJHBYKAGAVRTCSNKHF/doc1.txt","content":"a simple text with chinese: 箷 笓粊 痑祣筇 軿鉯頏 磏 峬峿 蔏蔍蓪 忷扴汥","contentType":"text/plain"}
 * ----
 *
 * And finally an example of binary format document:
 * [source, json]
 * ----
 * {"uri":"/testXBWPWCSWWUQIJGCVAGDV/img2.gif","content":"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","contentType":"application/octet-stream"}
 * ----
 *
 * === Documents
 *
 * {@link org.etourdot.vertx.marklogic.model.client.Documents} is a convenient class to manipulate multiple documents.
 *
 * Usage sample:
 * [source, java]
 * ----
 * Document document1 = getDocument("json/doc1_no_uri.json");
 * Document document2 = getDocument("json/doc2_uri.json");
 * Document docBin = getBinDocument();
 * Document docXML = getXMLDocument();
 * Documents documents = Documents.create();
 * documents.addDocuments(document1, docBin, docXML, document2);
 * mlClient.save(documents.toJson(), onSuccess(ids -> {}));
 * ----
 *
 * === Transformation
 *
 * {@link org.etourdot.vertx.marklogic.model.client.Transformation} represents a transformation to save to MarkLogic.
 *
 * Here is an example of transformation object:
 * [source, json]
 * ----
 * {"name":"xslTest","source":"/C:/vertx-marklogic/marklogic-client/target/test-classes/xslt/xslt1.xsl","format":"xslt"}
 * ----
 */
@io.vertx.docgen.Document
@ModuleGen(name = "marklogic-client", groupPackage = "org.etourdot.vertx")
package org.etourdot.vertx.marklogic.model.client;

import io.vertx.codegen.annotations.ModuleGen;
