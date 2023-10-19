package com.example;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.TimeZone;

@WebFilter("/time")
public class TimezoneValidateFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String timezoneParam = request.getParameter("timezone");

        if (timezoneParam != null && !timezoneParam.isEmpty()) {
            TimeZone timeZone = TimeZone.getTimeZone(timezoneParam);
            if (!timeZone.getID().equals(timezoneParam)) {
                response.setContentType("text/html");
                response.getWriter().write("Invalid timezone");
                ((HttpServletResponse) response).setStatus(400);
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

    public void destroy() {

    }
}
