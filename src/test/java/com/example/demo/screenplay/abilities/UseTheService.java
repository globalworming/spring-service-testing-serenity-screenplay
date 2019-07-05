package com.example.demo.screenplay.abilities;

import com.example.demo.service.api.MyService;
import net.serenitybdd.core.exceptions.TestCompromisedException;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.RefersToActor;

public class UseTheService implements Ability, RefersToActor {

  private final MyService service;
  private Actor actor;

  public UseTheService(MyService service) {
    this.service = service;
  }

  public static UseTheService as(Actor actor) {
    if (actor.abilityTo(UseTheService.class) == null) {
      throw new TestCompromisedException(actor.getName() + " misses the ability");
    }
    return actor.abilityTo(UseTheService.class).asActor(actor);
  }

  @Override
  public <T extends Ability> T asActor(Actor actor) {
    this.actor = actor;
    return (T) this;
  }

  public MyService getService() {
    return (MyService) InstrumentedInterface.of(service, MyService.class, actor);
  }

}
