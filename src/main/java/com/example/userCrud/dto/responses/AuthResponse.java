package com.example.userCrud.dto.responses;

import com.example.userCrud.services.auth.MyCustomUserDetails;

public class AuthResponse {
    private String token;
    private MyCustomUserDetails myCustomUserDetails;

    public AuthResponse(String token, MyCustomUserDetails myCustomUserDetails){
        this.token = token;
        this.myCustomUserDetails = myCustomUserDetails;
    }
    //End of all Args Constructor.

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public MyCustomUserDetails getMyCustomUserDetails() {
        return myCustomUserDetails;
    }

    public void setMyCustomUserDetails(MyCustomUserDetails myCustomUserDetails) {
        this.myCustomUserDetails = myCustomUserDetails;
    }

    public String getUsername(){
        return this.myCustomUserDetails.getUsername();
    }

    public String getFirstName(){
        return this.myCustomUserDetails.getFirstName();
    }

    public String getLastName(){
        return this.myCustomUserDetails.getLastName();
    }

    public int getId(){
        return this.myCustomUserDetails.getId();
    }

}
//End of Auth response.
