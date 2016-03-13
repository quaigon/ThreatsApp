package com.quaigon.threatsapp.dto;

/**
 * Created by Kamil on 12.03.2016.
 */
public class Token {
    private String token;
    private String role;

    public Token(String tokenName, String role) {
        this.token = tokenName;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }
}
