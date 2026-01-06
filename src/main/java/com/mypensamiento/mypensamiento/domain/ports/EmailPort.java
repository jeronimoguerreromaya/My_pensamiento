package com.mypensamiento.mypensamiento.domain.ports;

public interface EmailPort {

    void send(String to, String subject, String content);

}
