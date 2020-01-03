package com.example.lewjun.filters;

import com.example.lewjun.base.EnumApiResultCode;
import com.example.lewjun.utils.ServletUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 登录过滤器，配置/api/user/login和/api/user/logout不过滤
 */
@WebFilter(
        urlPatterns = "/api/*",
        initParams = {
                @WebInitParam(name = "exclusionUrls", value = "/api/user" +
                        "/login," + "/api/user/logout")
        })
public class LoginFilter implements Filter {
  private final List<String> exclusions = new ArrayList<>();

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    String exclusionUrls = filterConfig.getInitParameter("exclusionUrls");
    exclusions.addAll(Arrays.asList(exclusionUrls.split(",")));
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain filterChain)
          throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;

    String servletPath = req.getServletPath();
    if (!exclusions.contains(servletPath)) {
      HttpSession session = req.getSession(false);
      if (session == null || session.getAttribute("loginUser") == null) {
        // 判断是否为ajax请求
        if (req.getHeader("x-requested-with") != null) {
          ServletUtils.failure(resp, EnumApiResultCode.USER_NOT_LOGGED_IN);
        } else {
          ServletUtils.forward(req, resp, "/user/login");
        }
        return;
      }
    }
    filterChain.doFilter(req, resp);
  }

  @Override
  public void destroy() {
  }
}
