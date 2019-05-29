package com.jet.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author awad_yoo
 * @create 2019-05-28 10:57
 */
public class Url {

    public static void main(String[] args) throws IOException {

       /* SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(15000);
        factory.setReadTimeout(5000);
        RestTemplate restTemplate = new RestTemplate(factory);

        Object forObject = restTemplate.getForObject("https://blog.csdn.net/gozhuyinglong/article/details/80376122", String.class);
        System.out.println(forObject);*/
        int a = 1, b = 3;
        a = a + b - (b = a);
        System.out.println(a);
        System.out.println(b);
    }
}
