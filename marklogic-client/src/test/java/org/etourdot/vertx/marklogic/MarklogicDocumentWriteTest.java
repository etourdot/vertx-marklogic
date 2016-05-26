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

import com.marklogic.client.document.BinaryDocumentManager;
import com.marklogic.client.document.DocumentManager;
import com.marklogic.client.io.BytesHandle;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.StringHandle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import org.etourdot.vertx.marklogic.model.client.Document;
import org.etourdot.vertx.marklogic.model.client.Documents;
import org.etourdot.vertx.marklogic.model.client.Transformation;
import org.etourdot.vertx.marklogic.model.options.TransformOptions;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.core.Is.*;

public class MarklogicDocumentWriteTest extends AbstractTestMarklogicDocument {


  private static final String XMLSOURCE =
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<product><name>Just-In-Time</name>" +
      "<industry>Retail</industry></product>\n";

   public void setUp() throws Exception {
    super.setUp();
    deleteOnExit = true;
  }

  @Test
  public void write_single_json_document() throws Exception {
    Document document = getDocument("json/doc2_uri.json");
    mlClient.save(document.toJson(), onSuccess(ids -> {
      String id = ids.getString(0);
      assertEquals(getDirectory() + "doc2_uri.json", id);
      retrieveDocument(id, onSuccess(s -> {
        try {
          JSONAssert.assertEquals(document.getContent().toString(), s, true);
          testComplete();
        } catch (JSONException e) {
          fail();
        }
      }));
    }));
    await();
  }

  @Test
  public void write_single_json_document_without_uri() throws Exception {
    Document document = getDocument("json/doc1_no_uri.json");
    mlClient.save(document.toJson(), onSuccess(ids -> {
      String id = ids.getString(0);
      assertTrue(id.contains(getDirectory()));
      assertTrue(id.endsWith("json"));
      retrieveDocument(id, onSuccess(s -> {
        try {
          JSONAssert.assertEquals(document.getContent().toString(), s, true);
          testComplete();
        } catch (JSONException e) {
          fail();
        }
      }));
    }));
    await();
  }

  @Test
  public void write_single_json_document_with_metas() throws Exception {
    Document document = getDocument("json/doc3_uri.json");
    mlClient.save(document.toJson(), onSuccess(ids -> {
      String id = ids.getString(0);
      assertNotNull(id);
      retrieveMetadatas(id, databaseClient.newJSONDocumentManager(), onSuccess(metadataHandle -> {
        assertThat(metadataHandle.getCollections(), is(new HashSet(Arrays.asList("coll/c1", "coll/c2"))));
        assertEquals(2, metadataHandle.getQuality());
        assertThat(metadataHandle.getProperties().keySet(),
          is(new HashSet(Arrays.asList(new QName("aProp1"), new QName("aProp2")))));
        assertThat(new HashSet(metadataHandle.getProperties().values()),
          is(new HashSet(Arrays.asList("aValue1", "aValue2"))));
        assertTrue(metadataHandle.getPermissions().containsKey("app-user"));
        assertThat(metadataHandle.getPermissions().get("app-user"), is(new HashSet(Collections.singletonList(
          DocumentMetadataHandle.Capability.UPDATE))));
        testComplete();
      }));
    }));
    await();
  }

  @Test
  public void write_multiple_json_document() throws Exception {
    Document document1 = getDocument("json/doc1_no_uri.json");
    Document document2 = getDocument("json/doc2_uri.json");
    Documents documents = Documents.create();
    documents.addDocuments(document1, document2);
    mlClient.save(documents.toJson(), onSuccess(ids -> {
      assertEquals(2, ids.size());
      testComplete();
    }));
    await();
  }

  @Test
  public void write_single_xml_document() throws Exception {
    Document document = getXMLDocument();
    Documents documents = Documents.create();
    documents.addDocuments(document);
    mlClient.save(documents.toJson(), onSuccess(ids -> {
      String id = ids.getString(0);
      assertNotNull(id);
      retrieveDocument(id, onSuccess(s -> {
        assertEquals(XMLSOURCE, s);
        retrieveMetadatas(id, databaseClient.newXMLDocumentManager(), onSuccess(metadataHandle -> {
          assertEquals(1, metadataHandle.getQuality());
          assertThat(new HashSet(metadataHandle.getCollections()),
            is(new HashSet(Arrays.asList("coll1", "coll2"))));
          testComplete();
        }));
      }));
    }));
    await();
  }

  @Test
  public void write_and_transform_xml_document() throws Exception {
    Document document = getXMLDocument("xml/doc1.xml");
    Documents documents = Documents.create();
    documents.addDocuments(document);
    Transformation config = new Transformation();
    config.name("xslTest");
    config.format("xslt");
    config.source(getClass().getClassLoader().getResource("xslt/xslt1.xsl").getFile());
    mlClient.saveTransformation(config, onSuccess(f -> {
      TransformOptions options = new TransformOptions();
      options.name("xslTest");
      mlClient.saveAndTransform(documents.toJson(), options, onSuccess(ids -> {
        String id = ids.getString(0);
        assertNotNull(id);
        retrieveDocument(id, onSuccess(s -> {
          assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<pays>France</pays>\n", s);
          testComplete();
        }));
      }));
    }));
    await();
  }

  @Test
  public void write_single_text_document() throws Exception {
    String txtSource = "a simple text with chinese: 箷 笓粊 痑祣筇 軿鉯頏 磏 峬峿 蔏蔍蓪 忷扴汥";
    Document document = Document.create();
    document.content(txtSource)
      .uri(getDirectory() + "doc1.txt")
      .contentType("text/plain");
    mlClient.save(document.toJson(), onSuccess(ids -> {
      String id = ids.getString(0);
      assertNotNull(id);
      retrieveDocument(id, onSuccess(s -> {
        assertEquals(txtSource, s);
        testComplete();
      }));
    }));
    await();
  }

  @Test
  public void write_single_binary_document() throws Exception {
    Document document = getBinDocument();
    mlClient.save(document.toJson(), onSuccess(ids -> {
      String id = ids.getString(0);
      assertNotNull(id);
      retrieveBinaryDocument(id, onSuccess(buffer -> {
        // Compare start with base64
        assertEquals(3840, buffer.length());
        assertEquals("R0lGODlhpAFaAOYAAKA", buffer.getString(0, 19));
        testComplete();
      }));
    }));
    await();
  }

  @Test
  public void write_multiple_formats_documents() throws Exception {
    Document document1 = getDocument("json/doc1_no_uri.json");
    Document document2 = getDocument("json/doc2_uri.json");
    Document docBin = getBinDocument();
    Document docXML = getXMLDocument();
    Documents documents = Documents.create();
    documents.addDocuments(document1, docBin, docXML, document2);
    mlClient.save(documents.toJson(), onSuccess(ids -> {
      assertEquals(4, ids.size());
      testComplete();
    }));
    await();
  }

  private Document getXMLDocument() {
    Document docXML = Document.create();
    docXML.content(XMLSOURCE)
      .uri(getDirectory() + "doc1.xml")
      .contentType("application/xml")
      .collections(new JsonArray(Arrays.asList("coll1", "coll2")))
      .quality(1);
    return docXML;
  }

  private Document getBinDocument() throws URISyntaxException, IOException {
    Document docBin = Document.create();
    URI imageUri = getClass().getClassLoader().getResource("marklogic.gif").toURI();
    byte[] data = Files.readAllBytes(Paths.get(imageUri));
    docBin.content(Buffer.buffer(data))
      .uri(getDirectory() + "img2.gif")
      .contentType("application/octet-stream");
    return docBin;
  }

  private void retrieveDocument(String id, Handler<AsyncResult<String>> resultHandler) {
    vertx.executeBlocking(future -> {
      DocumentManager documentManager = databaseClient.newDocumentManager();
      StringHandle stringHandle = new StringHandle();
      documentManager.read(id, stringHandle);
      future.complete(stringHandle.get());
    }, resultHandler);
  }

  private void retrieveBinaryDocument(String id, Handler<AsyncResult<Buffer>> resultHandler) {
    vertx.executeBlocking(future -> {
      BinaryDocumentManager documentManager = databaseClient.newBinaryDocumentManager();
      BytesHandle bytesHandle = new BytesHandle();
      documentManager.read(id, bytesHandle);
      future.complete(Buffer.buffer(bytesHandle.get()));
    }, resultHandler);
  }

  private void retrieveMetadatas(String id, DocumentManager documentManager, Handler<AsyncResult<DocumentMetadataHandle>> resultHandler) {
    vertx.executeBlocking(future -> {
      DocumentMetadataHandle metadataHandle = new DocumentMetadataHandle();
      documentManager.readMetadata(id, metadataHandle);
      future.complete(metadataHandle);
    }, resultHandler);
  }
}
