package com.integration;

import com.Application;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//application 是SpringBoot的入口 ，所以测试的是Application.用随机的端口去启动
@TestPropertySource(locations = "classpath:test.properties")
public class IntegrationTest {
    @Inject
    Environment environment;  //获得随机端口用的是哪一个端口

    @Test
    public void SmokeTest() throws IOException {
        //因为是端口是随机启动的，所以要知道用的是哪一个端口
        String port = environment.getProperty("local.server.port");
        CloseableHttpClient httpClient = HttpClients.custom().build();
        notLoginByDefault(httpClient, port);

    }


    public void notLoginByDefault(CloseableHttpClient httpClient, String port) throws IOException {
        HttpGet httpGet = new HttpGet("http://localhost:" + port + "/auth");
        InputStream inputStream;
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                Integer status = response.getStatusLine().getStatusCode();
                inputStream = entity.getContent();
                Assertions.assertEquals(200, status);
                System.out.println(inputStream);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                Assertions.assertTrue(bufferedReader.readLine().contains("用户没有登录"));
            }
        } finally {
            httpClient.close();
            response.close();
        }

    }
}


