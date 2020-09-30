package com.integration;

import com.Application;


import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.io.IOException;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment =
        SpringBootTest.WebEnvironment.RANDOM_PORT) //application 是SpringBoot的入口 ，所以测试的是Application.用随机的端口去启动
@TestPropertySource(locations = "classpath:test.properties")
public class IntegrationTest {
    @Inject
    Environment environment;  //获得随机端口用的是哪一个端口

    @Test
    public void notLoginByDefault() {
        //因为是端口是随机启动的，所以要知道用的是哪一个端口
        String port = environment.getProperty("local.server.port");
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:" + port + "/auth");
        System.out.println(port);
        try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
            Integer status = response1.getStatusLine().getStatusCode();
            ResponseHandler<String> handler = new BasicResponseHandler();
            String body = httpclient.execute(httpGet, handler);
            Assertions.assertEquals(200, status);
            Assertions.assertTrue(body.contains("用户没有登录"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
