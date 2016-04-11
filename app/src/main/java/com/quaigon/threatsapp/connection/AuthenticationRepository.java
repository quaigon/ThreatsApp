package com.quaigon.threatsapp.connection;

/**
 * Created by Kamil on 12.03.2016.
 */
import com.quaigon.threatsapp.dto.Token;


public interface AuthenticationRepository {
    Token loadToken ();
    String loadUserLogin();

    void saveToken (Token token);
    void saveUserLogin(String login);
}

