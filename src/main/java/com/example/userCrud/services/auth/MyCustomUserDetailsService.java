package com.example.userCrud.services.auth;

import com.example.userCrud.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyCustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("Unable to load user.");
        }
        return new MyCustomUserDetails(user);
    }
    //End of Load User by UserName Method.
}
//End of MyCustomUserDetailsService class.
