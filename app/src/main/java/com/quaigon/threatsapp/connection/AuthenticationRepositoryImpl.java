package com.quaigon.threatsapp.connection;

import android.content.SharedPreferences;

import com.google.inject.Inject;
import com.quaigon.threatsapp.pojo.Token;

/**
 * Created by Kamil on 12.03.2016.
 */
public class AuthenticationRepositoryImpl implements AuthenticationRepository {

    public static final String PREFS_NAME = "Prefs";

    @Inject
    SharedPreferences preferences;

    @Override
    public Token loadToken() {
        String token = preferences.getString("Token", null);
        String role = preferences.getString("Role", null);

        return new Token(token,role);
    }

    @Override
    public void saveToken(Token token) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Token", token.getToken());
        editor.putString("Role", token.getRole());
        editor.commit();
    }
}