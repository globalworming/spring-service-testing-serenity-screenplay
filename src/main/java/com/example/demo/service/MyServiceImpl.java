package com.example.demo.service;

import com.example.demo.service.api.MyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyServiceImpl implements MyService {

  private List<String> authenticated = new ArrayList<>();

  @Override
  public void authenticate(String name) {
    authenticated.add(name);
  }

  @Override
  public boolean isAuthenticated(String name) {
    return authenticated.contains(name);
  }

  @Override
  public void logout(String name) {
    authenticated.remove(name);
  }
}
