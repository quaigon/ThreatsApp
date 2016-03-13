package com.quaigon.threatsapp;

import com.google.inject.AbstractModule;
import com.quaigon.threatsapp.connection.AuthenticationRepository;
import com.quaigon.threatsapp.connection.AuthenticationRepositoryImpl;

/**
 * Created by Kamil on 12.03.2016.
 */
public class MainModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AuthenticationRepository.class).to(AuthenticationRepositoryImpl.class);
    }
}
