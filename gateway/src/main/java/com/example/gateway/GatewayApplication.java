package com.example.gateway;

import com.google.apphosting.api.ApiProxy;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.commons.httpclient.HttpClientConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableZuulProxy
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public CloseableHttpClient client() {
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

//@Component
//@Profile("gcp")
//class AddHeader extends ZuulFilter {
//
//	private Logger log = LoggerFactory.getLogger(getClass());
//
//	@Override
//	public String filterType() {
//		return "pre";
//	}
//
//	@Override
//	public int filterOrder() {
//		return 1;
//	}
//
//	@Override
//	public boolean shouldFilter() {
//		return true;
//	}
//
//	@Override
//	public Object run() throws ZuulException {
//		RequestContext context = RequestContext.getCurrentContext();
//		ApiProxy.Environment currentEnvironment = ApiProxy.getCurrentEnvironment();
//		if (currentEnvironment != null) {
//			context.addZuulRequestHeader("X-Appengine-Inbound-Appid", currentEnvironment.getAppId());
//			log.info("X-Appengine-Inbound-Appid added!!");
//		} else {
//			log.error("GAE current environment not found :(");
//			context.addZuulRequestHeader("X-Appengine-Inbound-Appid", "something");
//		}
//
//		return null;
//	}
//}
