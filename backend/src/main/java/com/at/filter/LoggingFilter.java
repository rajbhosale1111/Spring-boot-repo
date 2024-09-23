package com.at.filter;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
public class LoggingFilter extends OncePerRequestFilter {

  private static final Logger log = LogManager.getLogger(LoggingFilter.class);
  private List<String> urlsToBeLogged;

  @Value("${api.v1.baseUrl}")
  private String apiBaseUrl;

  @PostConstruct
  public void init() {
    urlsToBeLogged = List.of(apiBaseUrl + "/auth/*", apiBaseUrl + "/users/*");
  }

  private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
    try {
      return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return "";
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

    long startTime = System.currentTimeMillis();
    filterChain.doFilter(requestWrapper, responseWrapper);
    long timeTaken = System.currentTimeMillis() - startTime;

    String requestBody = getStringValue(requestWrapper.getContentAsByteArray(),
        request.getCharacterEncoding());
    String responseBody = getStringValue(responseWrapper.getContentAsByteArray(),
        response.getCharacterEncoding());

    if (urlToBLogged(request.getRequestURI())) {
      log.info(
          "REPRODUCT REQUEST: METHOD={}; URI={}; REQUEST PAYLOAD={}; RESPONSE CODE={}; RESPONSE={}; TIME TAKEN={}",
          request.getMethod(), request.getRequestURI(), requestBody, response.getStatus(),
          responseBody, timeTaken);
    }
    responseWrapper.copyBodyToResponse();
  }

  private boolean urlToBLogged(String url) {
    return urlsToBeLogged.stream().anyMatch(ex -> {
      Pattern stringPattern = Pattern.compile(ex);
      Matcher m = stringPattern.matcher(url);
      return m.find();
    });
  }
}
