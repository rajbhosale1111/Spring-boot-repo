package com.at.filter;

import static com.at.util.Constants.HEADER_AUTH;
import static com.at.util.Constants.HEADER_AUTH_PREFIX;
import static com.at.util.Constants.HEADER_AUTH_PREFIX_LENGTH;

import com.at.service.JwtService;
import com.at.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserService userService;
  private List<String> urlsNeedToken;

  @Value("${api.v1.baseUrl}")
  private String apiBaseUrl;

  @PostConstruct
  public void init() {
    urlsNeedToken = List.of(apiBaseUrl + "/users/*");
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    if (!urlRequiresToken(request.getRequestURI())) {
      filterChain.doFilter(request, response);
      return;
    }

    String authHeader = request.getHeader(HEADER_AUTH);
    if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader,
        HEADER_AUTH_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }

    String jwt = authHeader.substring(HEADER_AUTH_PREFIX_LENGTH);
    String userEmail = jwtService.extractUserName(jwt);
    if (StringUtils.isNotEmpty(userEmail)
        && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userService.userDetailsService()
          .loadUserByUsername(userEmail);
      if (jwtService.isTokenValid(jwt, userDetails)) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
      }
    }
    filterChain.doFilter(request, response);
  }

  private boolean urlRequiresToken(String url) {
    return urlsNeedToken.stream().anyMatch(ex -> {
      Pattern stringPattern = Pattern.compile(ex);
      Matcher m = stringPattern.matcher(url);
      return m.find();
    });
  }
}