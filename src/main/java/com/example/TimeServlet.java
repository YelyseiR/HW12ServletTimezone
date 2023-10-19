package com.example;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(req.getServletContext());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        String timezone = req.getParameter("timezone");

        Cookie[] cookies = req.getCookies();
        String lastTimezone = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("lastTimezone".equals(cookie.getName())) {
                    lastTimezone = cookie.getValue();
                    break;
                }
            }
        }

        TimeZone tz = null;
        if (timezone != null) {
            tz = TimeZone.getTimeZone(timezone);
        } else if (lastTimezone != null) {
            tz = TimeZone.getTimeZone(lastTimezone);
        } else {
            tz = TimeZone.getTimeZone("UTC");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(tz);

        String currentTime = sdf.format(new Date());

        if (timezone != null) {
            Cookie cookie = new Cookie("lastTimezone", timezone);
            resp.addCookie(cookie);
        }

        resp.setContentType("text/html");
        WebContext ctx = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        ctx.setVariable("currentTime", currentTime);
        ctx.setVariable("selectedTimezone", tz.getID());

        templateEngine.process("time-template", ctx, resp.getWriter());
    }
}
