package com.example.userCrud.config.filters;

import com.example.userCrud.services.JwtTokenService;
import com.example.userCrud.services.auth.MyCustomUserDetailsService;
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
public class JWTAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private MyCustomUserDetailsService myCustomUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
         //Get Authentication header.
        String authHeader = request.getHeader("Authorization");

        //Get JWT Property
        String jwtToken = null;

        //Set Username property
        String userEmail = null;

        // CHECK / VALIDATE AUTHORIZATION HEADER:
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            // KILL CODE EXECUTION HERE:
            return;
        }
        // END OF CHECK / VALIDATE AUTHORIZATION HEADER.

        // SET JWT TOKEN VALUE RETRIEVED FROM AUTHORIZATION HEADER:
        jwtToken = authHeader.substring(7);

        // EXTRACT THE USER EMAIL FROM JWT TOKEN:
        userEmail = jwtTokenService.extractUsername(jwtToken);

        // CHECK IF USER NOT NULL AND IS AUTHENTICATED:
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails =  myCustomUserDetailsService.loadUserByUsername(userEmail);

            // VALIDATE TOKEN:
            if(jwtTokenService.validateToken(jwtToken, userDetails)){
                UsernamePasswordAuthenticationToken userAuthToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()

                        );
                // END OF USERNAME PASSWORD AUTH TOKEN OBJECT.
                userAuthToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // END OF SET USER AUTH TOKEN DETAILS OBJECT.

                SecurityContextHolder.getContext().setAuthentication(userAuthToken);
                // END OF SET SECURITY CONTEXT HOLDER AUTHENTICATION.

            }
            // END OF VALIDATE TOKEN.
        }
        // END OF CHECK IF USER NOT NULL AND IS AUTHENTICATED.

        //move to the next request.
        filterChain.doFilter(request,response);
    }
    //End of do filter internal method.


}
//End of JWT Auth Filter Class.
