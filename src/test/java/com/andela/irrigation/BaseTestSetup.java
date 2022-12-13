package com.andela.irrigation;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Main.class)
@WebAppConfiguration
@TestConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SuppressWarnings("java:S5786")
public class BaseTestSetup {
    protected MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;
    @BeforeAll
    public void setup() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected  MockHttpServletRequestBuilder withHeaders(MockHttpServletRequestBuilder requestBuilder, Map<String,String> headers) {
        if(headers != null) {
            headers.forEach(requestBuilder::header);
        }
        return requestBuilder;
    }

    protected  <T> String post( String url, T object, ResultMatcher resultMatcher, Map<String,String> headers) throws Exception {

        MvcResult result = mvc.perform(
                        withHeaders(MockMvcRequestBuilders.post(url), headers)
                                .content(mapToJson(object))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(resultMatcher)
                .andReturn();

        return result.getResponse().getContentAsString();
    }

    protected  <T,R> R post( String url, T object, Class<R> clazz, ResultMatcher resultMatcher, Map<String,String> headers) throws Exception{

        MvcResult result = mvc.perform(
                        withHeaders(MockMvcRequestBuilders.post(url), headers)
                                .content(mapToJson(object))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(resultMatcher)
                .andReturn();

        String content = result.getResponse().getContentAsString();
        if(content.length() > 0) {
            return mapFromJson(result.getResponse().getContentAsString(), clazz);
        }

        return null;
    }

    protected  <T,R> R post( String url, T object, Class<R> clazz, ResultMatcher resultMatcher) throws Exception {
        return post(url, object, clazz, resultMatcher, Map.of());
    }

    protected  <R> R postUrlEncoded( String url, String content, Class<R> clazz, ResultMatcher resultMatcher, Map<String,String> headers) throws Exception{

        MvcResult result = mvc.perform(
                        withHeaders(MockMvcRequestBuilders.post(url), headers)
                                .content(content)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(resultMatcher)
                .andReturn();

        String response = result.getResponse().getContentAsString();
        if(response.length() > 0) {
            return mapFromJson(response, clazz);
        }
        return null;
    }

    protected  <T,R> R put( String url, T object, Class<R> clazz, ResultMatcher resultMatcher, Map<String,String> headers) throws Exception {
        MvcResult result = mvc.perform(
                        withHeaders(MockMvcRequestBuilders.put(url), headers)
                                .content(mapToJson(object))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(resultMatcher)
                .andReturn();

        return mapFromJson(result.getResponse().getContentAsString(), clazz);
    }


    protected  <T,R> R put( String url, T object, Class<R> clazz, ResultMatcher resultMatcher) throws Exception {
        return put(url, object, clazz, resultMatcher, Map.of());
    }

    protected  void delete( String url, ResultMatcher expected, Map<String,String> headers) throws Exception {
        mvc.perform(withHeaders(MockMvcRequestBuilders.delete(url), headers))
                .andExpect(expected)
                .andReturn();
    }

    protected  String getText( String url, Map<String,String> headers, ResultMatcher resultMatcher) throws Exception {
        MvcResult result = mvc.perform(
                        withHeaders(MockMvcRequestBuilders.get(url), headers)
                                .accept(MediaType.TEXT_PLAIN)
                )
                .andExpect(resultMatcher)
                .andReturn();

        return result.getResponse().getContentAsString();
    }

    protected  <R> R get( String url, Class<R> clazz, ResultMatcher resultMatcher,  Map<String,String> headers) throws Exception {
        MvcResult result = mvc.perform(
                        withHeaders(MockMvcRequestBuilders.get(url), headers)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(resultMatcher)
                .andReturn();

        return mapFromJson(result.getResponse().getContentAsString(), clazz);
    }

    protected  String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected  <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}
