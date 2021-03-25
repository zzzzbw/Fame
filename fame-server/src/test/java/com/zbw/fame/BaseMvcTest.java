package com.zbw.fame;

import com.zbw.fame.util.FameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

/**
 * @author by zzzzbw
 * @since 2021/01/20 14:13
 */
@AutoConfigureMockMvc
public abstract class BaseMvcTest extends BaseTest {

    @Autowired
    protected MockMvc mockMvc;

    protected MvcResult getJson(String url, Object... uriVars) {
        try {
            MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url, uriVars)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON);
            return mockMvc.perform(builder).andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected MvcResult postJson(String url, Object requestBody, Object... uriVars) {
        try {
            MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                    .post(url, uriVars)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON);

            if (!Objects.isNull(requestBody)) {
                String content = FameUtils.objectToJson(requestBody);
                builder.content(content);
            }
            return mockMvc.perform(builder).andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
