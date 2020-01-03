package com.example.lewjun.filters;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 请求日志过滤器
 *
 * @author huiye
 */
@WebFilter(urlPatterns = {"/api/*"})
public class ReqlogFilter implements Filter {
  private static final Logger LOGGER =
          LoggerFactory.getLogger(ReqlogFilter.class);

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    LOGGER.info("init");
    Enumeration<String> initParameterNames =
            filterConfig.getInitParameterNames();
    while (initParameterNames.hasMoreElements()) {
      String initParameterName = initParameterNames.nextElement();
      LOGGER.debug("{}: {}", initParameterName,
              filterConfig.getInitParameter(initParameterName));
    }
  }

  @Override
  public void doFilter(
          ServletRequest servletRequest, ServletResponse servletResponse,
          FilterChain filterChain)
          throws IOException, ServletException {
    LOGGER.info("【-----------------------------------】");

    HttpServletRequest req = (HttpServletRequest) servletRequest;
    HttpServletResponse resp = (HttpServletResponse) servletResponse;

    //        getServletPath getRequestURI
    // /api/user/logout
    LOGGER.info("getServletPath: {}", req.getServletPath());
    String queryString = StringUtils.defaultIfBlank(req.getQueryString(), "");
    // GET /scott-servlet/api/user/logout?null
    LOGGER.info("{} {}?{}", req.getMethod(), req.getRequestURI(), queryString);
    StringBuilder cookieStr = new StringBuilder("cookie start\n");
    Cookie[] cookies = req.getCookies();
    for (Cookie cookie : cookies) {
      cookieStr.append(
              String.format(
                      "  %s:%s %s %d %n",
                      cookie.getName(), cookie.getValue(), cookie.getPath(),
                      cookie.getMaxAge()));
    }
    cookieStr.append("cookie end\n");
    LOGGER.debug(cookieStr.toString());

    StringBuilder headerInfo = new StringBuilder("\n【header start】\n");
    Enumeration<String> headerNames = req.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      headerInfo.append(String.format("  %s:%s%n", headerName,
              req.getHeader(headerName)));
    }
    headerInfo.append("【header end】\n");
    LOGGER.debug(headerInfo.toString());

    LOGGER.info("【parameter start】");
    Enumeration<String> parameterNames = req.getParameterNames();
    while (parameterNames.hasMoreElements()) {
      String parameter = parameterNames.nextElement();
      String[] parameterValues = req.getParameterValues(parameter);
      LOGGER.info("  {}:{}", parameter, parameterValues);
    }
    LOGGER.info("【parameter end】");
    LOGGER.info("【-----------------------------------】\n\n");
    filterChain.doFilter(req, resp);
  }

  @Override
  public void destroy() {
    LOGGER.info("destroy");
  }
}
