package com.fame.zbw;

import com.zbw.fame.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author zbw
 * @since 2019/8/3 13:08
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(classes = {Application.class, SwaggerGenerator.SwaggerTestsConfig.class})
public class SwaggerGenerator extends BaseTests {

    @Autowired
    private MockMvc mockMvc;

    private static final String SWAGGER_URL = "/v2/api-docs";

    private static final String SWAGGER_JSON = "swagger.json";

    private static final String SAVE_PATH = System.getProperty("user.dir") + File.separator;

    @Test
    public void start() throws Exception {
        log.info("SwaggerGenerator start!");

        MvcResult mvcResult = mockMvc
                .perform(get(SWAGGER_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String swaggerJson = mvcResult.getResponse().getContentAsString();
        log.info("swaggerJson: {}", swaggerJson);

        Files.createDirectories(Paths.get(SAVE_PATH));
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAVE_PATH, SWAGGER_JSON), StandardCharsets.UTF_8)) {
            writer.write(swaggerJson);
        }
        log.info("generator swagger.json success! file path:{}", Paths.get(SAVE_PATH, SWAGGER_JSON));
    }


    @TestConfiguration
    @EnableSwagger2
    static public class SwaggerTestsConfig {

        @Bean
        public Docket frontApi() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                    .paths(PathSelectors.ant("/api/**"))
                    .build()
                    .apiInfo(apiInfo())
                    .useDefaultResponseMessages(false)
                    .ignoredParameterTypes(MultipartFile.class);
        }


        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title("Fame API Documentation")
                    .description("Documentation for Fame API")
                    .version("1.0.0")
                    .termsOfServiceUrl("https://github.com/zzzzbw/Fame")
                    .contact(new Contact("zzzzbw", "https://zzzzbw.cn/", "zzzzbw@gmail.com"))
                    .build();
        }
    }
}
