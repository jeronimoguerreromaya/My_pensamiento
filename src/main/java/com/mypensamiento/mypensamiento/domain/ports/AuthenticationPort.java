package com.mypensamiento.mypensamiento.domain.ports;

public interface AuthenticationPort {

    void authenticate(String email, String password);

}

