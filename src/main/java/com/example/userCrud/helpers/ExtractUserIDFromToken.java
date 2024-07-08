package com.example.userCrud.helpers;

import com.example.userCrud.services.JwtTokenService;
import com.example.userCrud.services.auth.MyCustomUserDetails;
import com.example.userCrud.services.auth.MyCustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.http.HttpRequest;

@Component
public class ExtractUserIDFromToken {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private MyCustomUserDetailsService myCustomUserDetailsService;


    private MyCustomUserDetails myCustomUserDetails;

    public int getUserIdFromToken(HttpServletRequest request){
        Integer userId=null;

        // GET AUTHENTICATION HEADER:
        String authHeader = request.getHeader("Authorization");

        // SET JWT PROPERTY:
        String jwtToken = null;

        // SET USERNAME PROPERTY:
        String userEmail = null;

        // CHECK / VALIDATE AUTHORIZATION HEADER:
        if(authHeader != null || authHeader.startsWith("Bearer ")){
            // SET JWT TOKEN VALUE RETRIEVED FROM AUTHORIZATION HEADER:
            jwtToken = authHeader.substring(7);

            // EXTRACT THE USER EMAIL FROM JWT TOKEN:
            userEmail = jwtTokenService.extractUsername(jwtToken);
        }
        // END OF CHECK / VALIDATE AUTHORIZATION HEADER.


        // CHECK IF USERNAME / EMAIL IS NOT NULL AND SET ID IF BLOCK:
        if(userEmail != null){
            // GET USER DETAILS BY USER EMAIL:
            myCustomUserDetails = (MyCustomUserDetails) myCustomUserDetailsService.loadUserByUsername(userEmail);

            // SET USER ID:
            userId = myCustomUserDetails.getId();
        }
        // END OF CHECK IF USERNAME / EMAIL IS NOT NULL AND SET ID IF BLOCK:


        return userId;


    }
    //end of getUserIdFromToken method.
}
