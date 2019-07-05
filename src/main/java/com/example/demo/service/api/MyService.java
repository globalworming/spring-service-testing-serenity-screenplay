package com.example.demo.service.api;

public interface MyService {

  void authenticate(String name);

  boolean isAuthenticated(String name);

  void logout(String name);

}
