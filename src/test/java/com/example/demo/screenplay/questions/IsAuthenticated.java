package com.example.demo.screenplay.questions;

import com.example.demo.screenplay.abilities.UseTheService;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class IsAuthenticated implements Question<Boolean> {
  @Override
  public Boolean answeredBy(Actor actor) {
    return UseTheService.as(actor).getService().isAuthenticated(actor.getName());
  }

  @Override
  public String getSubject() {
    return "answer if is authenticated" ;
  }
}
