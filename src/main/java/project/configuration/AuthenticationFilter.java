package project.configuration;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.util.WebUtils;
import project.Service.RoleService;
import project.Service.UserService;
import project.model.entities.User;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    private RoleService roleService;
    private UserService userService;

    public AuthenticationFilter(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        Cookie cookie = WebUtils.getCookie(request, "token");
        String token = null;

        if(cookie != null)
            token = cookie.getValue();
        else
            System.out.println("No cookie");

        User user = userService.checkToken(token);
        UsernamePasswordAuthenticationToken auth = null;

        if(user != null) {

            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));

            auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), roles);
        }
        else
            System.out.println("No token");

        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(ctx);

        if(auth != null) {
            ctx.setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
