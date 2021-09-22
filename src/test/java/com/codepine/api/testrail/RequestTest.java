/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Kunal Shah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.codepine.api.testrail;

import com.codepine.api.testrail.internal.ListToCsvSerializer;
import com.codepine.api.testrail.internal.UrlConnectionFactory;
import com.codepine.api.testrail.model.Page;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link com.codepine.api.testrail.Request}.
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestTest {

    private static final String TEST_END_POINT = "https://test.end.point.com";
    private static final TestRailConfig config = TestRail.builder(TEST_END_POINT, "testUser", "testPassword").build().getConfig();

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    @Mock private HttpURLConnection mockConnection;
    @Mock private HttpURLConnection mockConnection1;
    @Mock private HttpURLConnection mockConnection2;
    @Mock private HttpURLConnection mockConnection3;
    @Mock
    private UrlConnectionFactory mockUrlConnectionFactory;

    private Models models;

    @BeforeClass
    public static void setUpLogger() {
        PropertyConfigurator.configure(RequestTest.class.getResourceAsStream("/log4j.properties"));
    }

    @Before
    public void setUp() throws IOException {
        when(mockUrlConnectionFactory.getUrlConnection(any(String.class))).thenReturn(mockConnection);
        when(mockUrlConnectionFactory.getUrlConnection("https://test.end.point.com/index.php?/api/v2/get_models/1")).thenReturn(mockConnection1);
        when(mockUrlConnectionFactory.getUrlConnection("https://test.end.point.com/index.php?/api/v2/get_models/2")).thenReturn(mockConnection2);
        when(mockUrlConnectionFactory.getUrlConnection("https://test.end.point.com/index.php?/api/v2/get_models/3")).thenReturn(mockConnection3);
        models = new Models(mockUrlConnectionFactory);
    }

    @Test
    public void G_unauthorizedUser_W_getModel_T_verifyError() throws IOException {
        // THEN set up
        expectedException.expect(TestRailException.class);
        expectedException.expectMessage("401 - Authentication failed: invalid or missing user/password or session cookie.");

        // GIVEN
        when(mockConnection.getResponseCode()).thenThrow(IOException.class).thenReturn(401);
        when(mockConnection.getErrorStream()).thenReturn(this.getClass().getResourceAsStream("/auth_error.json"));

        // WHEN
        models.get().execute();
    }

    @Test
    public void G_modelExists_W_getModel_T_verifyModel() throws IOException {
        // GIVEN
        when(mockConnection.getResponseCode()).thenReturn(200);
        when(mockConnection.getInputStream()).thenReturn(this.getClass().getResourceAsStream("/get_model.json"));

        // WHEN
        final Model actualModel = models.get().execute();

        // THEN
        final Model expectedModel = new Model().setId(1).setName("Test Model 1").setShowAnnouncement(false).setIsCompleted(true).setCompletedOn(new Date(1424641170000L)).setSuiteMode(2);
        assertEquals(expectedModel, actualModel);
    }

    @Test
    public void G_modelDoesNotExist_W_getModel_T_verifyError() throws IOException {
        // THEN set up
        expectedException.expect(TestRailException.class);
        expectedException.expectMessage("400 - Field :model_id is not a valid or accessible model.");

        // GIVEN
        when(mockConnection.getResponseCode()).thenReturn(400);
        when(mockConnection.getErrorStream()).thenReturn(this.getClass().getResourceAsStream("/get_model_error.json"));

        // WHEN, THEN
        models.get().execute();
    }

    @Test
    public void G_modelsExists_W_getModelsWithFilter_T_verifyFilterQueryAndModels_paginated() throws IOException {
        // GIVEN
        when(mockConnection1.getResponseCode()).thenReturn(200);
        when(mockConnection2.getResponseCode()).thenReturn(200);
        when(mockConnection3.getResponseCode()).thenReturn(200);
        when(mockConnection1.getInputStream()).thenReturn(this.getClass().getResourceAsStream("/get_modelsA.json"));
        when(mockConnection2.getInputStream()).thenReturn(this.getClass().getResourceAsStream("/get_modelsB.json"));
        when(mockConnection3.getInputStream()).thenReturn(this.getClass().getResourceAsStream("/get_modelsC.json"));

        // WHEN
        final List<Model> actualModels = models.listPaginated().setSectionId(1).setCreatedAfter(new Date(1424641170000L)).execute();

        // THEN -- verify objects returned
        final List<Model> expectedModels = new ArrayList<>();
        expectedModels.add(new Model().setId(1).setName("Test Model 1").setShowAnnouncement(false).setIsCompleted(true).setCompletedOn(new Date(1424641170000L)).setSuiteMode(2));
        expectedModels.add(new Model().setId(3).setName("Test Model 3").setShowAnnouncement(true).setIsCompleted(true).setCompletedOn(new Date(1424651896000L)).setSuiteMode(3));
        expectedModels.add(new Model().setId(4).setName("Test Model 4").setShowAnnouncement(false).setIsCompleted(false).setCompletedOn(new Date(1426110846000L)).setSuiteMode(1));
        expectedModels.add(new Model().setId(5).setName("Test Model 5").setShowAnnouncement(false).setIsCompleted(false).setCompletedOn(new Date(1426110846000L)).setSuiteMode(1));
        assertEquals(expectedModels, actualModels);
    }

    @Test
    public void G_modelsExists_W_getModelsWithFilter_T_verifyFilterQueryAndModels() throws IOException {
        // GIVEN
        when(mockConnection.getResponseCode()).thenReturn(200);
        when(mockConnection.getInputStream()).thenReturn(this.getClass().getResourceAsStream("/get_models.json"));

        // WHEN
        final List<Model> actualModels = models.list().setSectionId(1).setCreatedAfter(new Date(1424641170000L)).execute();

        // THEN -- verify filter query
        String expectedUrlWithFilterQuery = String.format("%s/index.php?/api/v2/get_models/0&section_id=1&created_after=1424641170", TEST_END_POINT);
        Mockito.verify(mockUrlConnectionFactory).getUrlConnection(expectedUrlWithFilterQuery);

        // THEN -- verify models returned
        final List<Model> expectedModels = new ArrayList<>();
        expectedModels.add(new Model().setId(1).setName("Test Model 1").setShowAnnouncement(false).setIsCompleted(true).setCompletedOn(new Date(1424641170000L)).setSuiteMode(2));
        expectedModels.add(new Model().setId(3).setName("Test Model 3").setShowAnnouncement(true).setIsCompleted(true).setCompletedOn(new Date(1424651896000L)).setSuiteMode(3));
        expectedModels.add(new Model().setId(4).setName("Test Model 4").setShowAnnouncement(false).setIsCompleted(false).setCompletedOn(new Date(1426110846000L)).setSuiteMode(1));
        assertEquals(expectedModels, actualModels);
    }

    @Test
    public void W_addModel_T_verifyPostBodyAndModel() throws IOException {
        // set up
        ByteArrayOutputStream postBodyOutputStream = new ByteArrayOutputStream();
        when(mockConnection.getResponseCode()).thenReturn(200);
        when(mockConnection.getOutputStream()).thenReturn(postBodyOutputStream);
        when(mockConnection.getInputStream()).thenReturn(this.getClass().getResourceAsStream("/add_model.json"));

        // WHEN
        final Model expectedModel = new Model().setId(1).setName("Test Model 1").setShowAnnouncement(true).setIsCompleted(false).setSuiteMode(2);
        final Model actualModel = models.add(expectedModel).execute();

        // THEN -- verify post body
        byte[] postBodyBytes = postBodyOutputStream.toByteArray();
        assertNotNull("post body should not be null", postBodyBytes);
        assertTrue("post body should not be empty", postBodyBytes.length > 0);
        Map<String, String> postBody = getObjectMapper().readValue(postBodyBytes, new TypeReference<HashMap<String, String>>() {
        });
        Set<String> expectedProperties = new HashSet<>(Arrays.asList("name", "show_announcement", "suite_mode"));
        assertTrue("Expected properties: " + expectedProperties + " but found: " + postBody.keySet(), postBody.keySet().equals(expectedProperties));

        // THEN -- verify model
        assertEquals(expectedModel, actualModel);
    }

    @Test
    public void W_updatePartialModel_T_verifyPostBodyAndModel() throws IOException {
        // set up
        ByteArrayOutputStream postBodyOutputStream = new ByteArrayOutputStream();
        when(mockConnection.getResponseCode()).thenReturn(200);
        when(mockConnection.getOutputStream()).thenReturn(postBodyOutputStream);
        when(mockConnection.getInputStream()).thenReturn(this.getClass().getResourceAsStream("/add_model.json"));

        // WHEN
        final Model updatedModel = new Model().setId(1).setName("Test Model 1").setShowAnnouncement(true).setSuiteMode(2);
        final Model actualModel = models.update(updatedModel).execute();

        // THEN -- verify post body
        byte[] postBodyBytes = postBodyOutputStream.toByteArray();
        assertNotNull("post body should not be null", postBodyBytes);
        assertTrue("post body should not be empty", postBodyBytes.length > 0);
        Map<String, String> postBody = getObjectMapper().readValue(postBodyBytes, new TypeReference<HashMap<String, String>>() {
        });
        Set<String> expectedProperties = new HashSet<>(Arrays.asList("show_announcement"));
        assertTrue("Expected properties: " + expectedProperties + " but found: " + postBody.keySet(), postBody.keySet().equals(expectedProperties));

        // THEN -- verify model
        Model expectedModel = new Model().setId(1).setName("Test Model 1").setShowAnnouncement(true).setIsCompleted(false).setSuiteMode(2);
        assertEquals(expectedModel, actualModel);
    }

    @Test
    public void W_deleteModel_T_verifyZeroContentLength() throws IOException {
        // set up
        when(mockConnection.getResponseCode()).thenReturn(200);

        // WHEN
        models.delete(1).execute();

        // THEN -- verify content length set to 0
        verify(mockConnection).setFixedLengthStreamingMode(eq(0));
    }

    @Test
    public void W_listWithSupplementForDeserialization_T_verifyModelHasBeenInjectedWithSupplement() throws IOException {
        // set up
        when(mockConnection.getResponseCode()).thenReturn(200);
        when(mockConnection.getInputStream()).thenReturn(this.getClass().getResourceAsStream("/get_models.json"));

        // WHEN
        final String expectedSupplementModelName = "Supplement Model";
        final List<ModelWithAltName> actualModels = models.listWithAltName().setSupplementModelName(expectedSupplementModelName).execute();

        // THEN
        final List<ModelWithAltName> expectedModels = new ArrayList<>();
        expectedModels.add((ModelWithAltName) new ModelWithAltName().setAltName(expectedSupplementModelName).setId(1).setName("Test Model 1").setShowAnnouncement(false).setIsCompleted(true).setCompletedOn(new Date(1424641170000L)).setSuiteMode(2));
        expectedModels.add((ModelWithAltName) new ModelWithAltName().setAltName(expectedSupplementModelName).setId(3).setName("Test Model 3").setShowAnnouncement(true).setIsCompleted(true).setCompletedOn(new Date(1424651896000L)).setSuiteMode(3));
        expectedModels.add((ModelWithAltName) new ModelWithAltName().setAltName(expectedSupplementModelName).setId(4).setName("Test Model 4").setShowAnnouncement(false).setIsCompleted(false).setCompletedOn(new Date(1426110846000L)).setSuiteMode(1));
        assertEquals(expectedModels, actualModels);
    }


    @Data
    public static class Model {
        private int id;

        @JsonView({RequestTest.Models.Add.class})
        private String name;

        @JsonView({RequestTest.Models.Add.class, RequestTest.Models.Update.class})
        private Boolean showAnnouncement;

        @JsonView(RequestTest.Models.Update.class)
        @Getter(value = AccessLevel.PRIVATE)
        @Setter(value = AccessLevel.PRIVATE)
        private Boolean isCompleted;

        @JsonView(RequestTest.Models.Update.class)
        private Date completedOn;

        @JsonView({RequestTest.Models.Add.class})
        private int suiteMode;
    }

    @RequiredArgsConstructor
    public static class Models {

        private final UrlConnectionFactory urlConnectionFactory;

        public Get get() {
            Get get = new Get();
            get.setUrlConnectionFactory(urlConnectionFactory);
            return get;
        }

        public List list() {
            List list = new List();
            list.setUrlConnectionFactory(urlConnectionFactory);
            return list;
        }

        public ListPaginated listPaginated() {
            ListPaginated list = new ListPaginated();
            list.setUrlConnectionFactory(urlConnectionFactory);
            return list;
        }

        public ListWithAltName listWithAltName() {
            ListWithAltName list = new ListWithAltName();
            list.setUrlConnectionFactory(urlConnectionFactory);
            return list;
        }

        public Add add(Model model) {
            Add add = new Add(model);
            add.setUrlConnectionFactory(urlConnectionFactory);
            return add;
        }

        public Update update(Model model) {
            Update update = new Update(model);
            update.setUrlConnectionFactory(urlConnectionFactory);
            return update;
        }

        public Delete delete(int id) {
            final Delete delete = new Delete(id);
            delete.setUrlConnectionFactory(urlConnectionFactory);
            return delete;
        }

        public static class Get extends Request<Model> {

            Get() {
                super(config, Method.GET, "get_model/1", Model.class);
            }

        }

        @Getter
        @Setter
        public static class List extends Request<java.util.List<Model>> {

            @JsonView(List.class)
            private Integer sectionId;

            @JsonView(List.class)
            private Date createdAfter;

            @JsonView(List.class)
            @JsonSerialize(using = ListToCsvSerializer.class)
            private java.util.List<Integer> createdBy;

            List() {
                super(config, Method.GET, "get_models/0", new TypeReference<java.util.List<Model>>() {
                });
            }
        }

        @Getter
        @Setter
        public static class ListPaginated extends Request<java.util.List<Model>> {

            @JsonView(List.class)
            private Integer sectionId;

            @JsonView(List.class)
            private Date createdAfter;

            @JsonView(List.class)
            @JsonSerialize(using = ListToCsvSerializer.class)
            private java.util.List<Integer> createdBy;

            ListPaginated() {
                super(config, Method.GET, "get_models/1", new TypeReference<java.util.List<Model>>() {
                }, new TypeReference<Page<java.util.List<Model>>>(){});
            }
        }

        @Setter
        public static class ListWithAltName extends Request<java.util.List<ModelWithAltName>> {

            private String supplementModelName;

            ListWithAltName() {
                super(config, Method.GET, "get_models/0", new TypeReference<java.util.List<ModelWithAltName>>() {
                });
            }

            @Override
            Object getSupplementForDeserialization() {
                return supplementModelName;
            }
        }

        public static class Add extends Request<Model> {

            private final Model model;

            Add(Model model) {
                super(config, Method.POST, "add_model/1", Model.class);
                this.model = model;
            }

            @Override
            protected Object getContent() {
                return model;
            }
        }

        public static class Update extends Request<Model> {

            private final Model model;

            Update(Model model) {
                super(config, Method.POST, "update_model/1", Model.class);
                this.model = model;
            }

            @Override
            protected Object getContent() {
                return model;
            }
        }

        public static class Delete extends Request<Void> {

            Delete(final int id) {
                super(config, Method.POST, "delete_model/" + id, Void.class);
            }
        }
    }

    @Data
    public static class ModelWithAltName extends Model {

        @JacksonInject("class com.codepine.api.testrail.RequestTest$ModelWithAltName")
        private String altName;

    }
}
