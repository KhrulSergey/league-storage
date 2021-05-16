package com.freetonleague.storage.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Class for pre filtering request and search for token in header or JSESSIONID
 */
@RequiredArgsConstructor
public class AuthenticationCustomFilter extends UsernamePasswordAuthenticationFilter {

    private final String serviceTokenName;

    private final String serviceToken;

    private final String serviceTokenAuthority;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        String requestToken = request.getParameter(serviceTokenName);

        if (!isBlank(requestToken)) {
            if (requestToken.equals(serviceToken)) {
                UserDetails user = User.builder()
                        .username(serviceTokenName)
                        .password(serviceToken)
                        .authorities(serviceTokenAuthority)
                        .accountLocked(false)
                        .accountExpired(false)
                        .credentialsExpired(false)
                        .disabled(false)
                        .roles(serviceTokenAuthority)
                        .build();
                this.setUserToContext(user);
            }
        }
        chain.doFilter(req, res);
    }

    private void setUserToContext(UserDetails user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }
}
