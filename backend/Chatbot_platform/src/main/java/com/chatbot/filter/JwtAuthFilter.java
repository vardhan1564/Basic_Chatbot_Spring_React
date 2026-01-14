package com.chatbot.filter;

import com.chatbot.service.CustomUserDetailsService;
import com.chatbot.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // 1. Check if the Header exists
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(token);
                System.out.println("✅ JWT Filter: Token found for user: " + username);
            } catch (Exception e) {
                System.out.println("❌ JWT Filter: Error extracting username. Token might be invalid.");
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠️ JWT Filter: No valid 'Bearer' token found in request to: " + request.getRequestURI());
        }

        // 2. Validate the Token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Set the user in the Security Context (This logs them in)
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("🔓 JWT Filter: Authentication SUCCESS for " + username);
                } else {
                    System.out.println("⛔ JWT Filter: Token is invalid or expired!");
                }
            } catch (Exception e) {
                System.out.println("❌ JWT Filter: User not found in database: " + username);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}