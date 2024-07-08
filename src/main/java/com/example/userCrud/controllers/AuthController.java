package com.example.userCrud.controllers;

import com.example.userCrud.dto.requests.LoginRequest;
import com.example.userCrud.dto.responses.AuthResponse;
import com.example.userCrud.services.JwtTokenService;
import com.example.userCrud.services.auth.MyCustomUserDetails;
import com.example.userCrud.services.auth.MyCustomUserDetailsService;
import com.example.userCrud.services.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private MyCustomUserDetailsService myCustomUserDetailsService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest){
        //Set Authentication.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
        );

        //Set user object
        MyCustomUserDetails userDetails =
                (MyCustomUserDetails) myCustomUserDetailsService.loadUserByUsername(loginRequest.getEmail());

        //Set security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Generate token
        String token = jwtTokenService.generateToken(userDetails);

        //Set parameters.
        AuthResponse response = new AuthResponse(token,userDetails);
        return new ResponseEntity(response, HttpStatus.ACCEPTED);

    }
    //End of login method.

    @PostMapping("/register")
    public ResponseEntity signUp(@RequestParam("first_name") String firstName,
                                 @RequestParam("last_name") String lastName,
                                 @RequestParam("email") String email,
                                 @RequestParam("password") String password){

        // TODO: VALIDATE IF EMAIL IS NOT ALREADY TAKEN.

        // HASH PASSWORD:
        String hashed_password = passwordEncoder.encode(password);

        // STORE USER.
        int result = userService.signUpUser(firstName,lastName,email, hashed_password);
        // END OF STORE USER.

        // CHECK FOR RESULT SET:
        if(result != 1){
            return new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
        }
        // END OF CHECK FOR RESULT SET.

        // RETURN SUCCESS RESPONSE:
        return new ResponseEntity("User Sign Up Successful!", HttpStatus.CREATED);
    }
    // END OF USER SIGN UP POST METHOD.
}
//End of Auth Controller.
