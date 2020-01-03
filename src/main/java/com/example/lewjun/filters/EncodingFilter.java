package com.example.lewjun.filters;

import com.example.lewjun.utils.ResourcesConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author huiye
 */
@WebFilter(urlPatterns = "/api/*")
public class EncodingFilter implements Filter {
  private static final Logger LOGGER =
          LoggerFactory.getLogger(EncodingFilter.class);

  private String charset;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    charset = ResourcesConfigUtils.getString("charset");
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp,
                       FilterChain filterChain)
          throws IOException, ServletException {
    req.setCharacterEncoding(charset);
    resp.setCharacterEncoding(charset);
    filterChain.doFilter(req, resp);
  }

  @Override
  public void destroy() {
    LOGGER.info("【destroy】");
  }
}
