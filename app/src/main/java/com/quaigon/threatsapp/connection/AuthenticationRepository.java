package com.quaigon.threatsapp.connection;

/**
 * Created by Kamil on 12.03.2016.
 */
import com.quaigon.threatsapp.pojo.Token;


public interface AuthenticationRepository {
    Token loadToken ();

    void saveToken (Token token);
}

