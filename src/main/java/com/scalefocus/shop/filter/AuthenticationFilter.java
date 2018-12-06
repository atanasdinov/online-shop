package com.scalefocus.shop.filter;

import com.scalefocus.shop.configuration.SecurityConfig;
import com.scalefocus.shop.model.entity.User;
import com.scalefocus.shop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is inheriting {@link Filter} in order to call each time a route is requested.
 * Its purpose is to make the Authentication logic for the application.
 */
public class AuthenticationFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private UserService userService;

    /**
     * Assign a value to the property.
     *
     * @param userService contain the autowired value from {@link SecurityConfig}.
     */
    public AuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * {@inheritDoc}
     * The logic of Authentication is made here. The cookie is gotten from the request in order to check if user contains such token.
     * If so the role in the database is set to the SecurityContext. If not the logger is notified.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Cookie cookie = WebUtils.getCookie(request, "token");
        String token = null;

        if (cookie != null)
            token = cookie.getValue();
        else
            logger.warn("No cookie: " + cookie);

        User user = userService.checkToken(token);
        UsernamePasswordAuthenticationToken auth = null;

        if (user != null) {
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
            auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), roles);
        } else
            logger.warn("No token: " + user);

        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(ctx);

        if (auth != null)
            ctx.setAuthentication(auth);
        else
            logger.warn("No auth: " + auth);

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
