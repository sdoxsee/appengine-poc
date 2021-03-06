package com.example.backend.gaeconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@Profile("gcp")
class GaeServiceConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new GaeHeaderInterceptor());
  }

  class GaeHeaderInterceptor implements HandlerInterceptor {
    public static final String X_APPENGINE_INBOUND_APPID = "X-Appengine-Inbound-Appid";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      String header = request.getHeader(X_APPENGINE_INBOUND_APPID);
      if (!StringUtils.hasText(header)) {
        throw new GaeUnauthorizedException();
      }
      return true;
    }
  }

  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  class GaeUnauthorizedException extends RuntimeException {}
}