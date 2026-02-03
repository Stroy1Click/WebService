package ru.stroy1click.web.common.service;


public interface JwtService {

    String generate();

    boolean isTokenExpired(String token);
}
