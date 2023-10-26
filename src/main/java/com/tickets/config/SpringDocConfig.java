package com.tickets.config;


import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableKnife4j
public class SpringDocConfig {
    private static final String WEBSITE_NAME = "黄文文";
    private static final String WEBSITE_URL = "https://zeantong.com";

    private static final String WEBSITE_EMAIL = "1158307158@qq.com";
    private Info info(){
        Contact contact=new Contact();
        contact.setName(WEBSITE_NAME);
        contact.setEmail(WEBSITE_EMAIL);
        contact.setUrl(WEBSITE_URL);
        return new Info()
                .title("API接口文档")
                .description("tickets项目")
//                .contact(new Contact(WEBSITE_NAME ,WEBSITE_URL, WEBSITE_EMAIL))
                .contact(contact)
                .termsOfService("HWW/X-2.0协议")
                .version("v1.9.0");
    }
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(info())
                .externalDocs( new ExternalDocumentation().description(WEBSITE_NAME)
                        .url(WEBSITE_URL));
    }


}
