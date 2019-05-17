package com.example.gateway.gaeconfig;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("gcp")
public class GaeUrlFetchServiceConfig {

    @Bean
    public CloseableHttpClient customHttpClient() {
        return HttpClients.custom()
                .setConnectionManager(
                        new GaeHttpClientConnectionManager(
                                SocketConfig.DEFAULT,
                                ConnectionConfig.DEFAULT,
                                RequestConfig.custom().setRedirectsEnabled(false).build())
                )
                .build();
    }
}
