package com.business_card.business_card.security;

import com.business_card.business_card.model.CustomUserPrincipal;
import com.business_card.business_card.model.User;
import com.business_card.business_card.service.UserService;
import com.business_card.business_card.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                String phone = jwtUtil.extractPhone(token);

                Optional<User> userOpt = userService.findByPhone(phone);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();

                    // ✅ Create proper principal
                    CustomUserPrincipal principal = new CustomUserPrincipal(user.getPhone(), user.getPassword());

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("🧪 Token = " + token);
                    System.out.println("🧪 Extracted Phone = " + phone);
                    System.out.println("🧪 Is Auth Set? " + SecurityContextHolder.getContext().getAuthentication());


                    System.out.println("✅ Authenticated user in filter: " + user.getPhone());
                }
            }
        }

        filterChain.doFilter(request, response);
    }

}
