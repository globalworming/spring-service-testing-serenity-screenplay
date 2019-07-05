package com.example.demo.service;

import com.example.demo.screenplay.abilities.InstrumentedInterface;
import com.example.demo.screenplay.abilities.UseTheService;
import com.example.demo.screenplay.questions.IsAuthenticated;
import com.example.demo.service.api.MyService;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.junit.spring.integration.SpringIntegrationMethodRule;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.Cast;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static org.hamcrest.core.Is.is;

@RunWith(SerenityRunner.class)
@SpringBootTest
public class MyServiceImplTest {

  @Rule
  public SpringIntegrationMethodRule springIntegrationMethodRule = new SpringIntegrationMethodRule();

  @Autowired
  MyServiceImpl myServiceImpl;


  private Actor client;

  @Before
  public void setUp() {
    Cast cast = Cast.whereEveryoneCan(new UseTheService(myServiceImpl));
    client = cast.actorNamed("client");
  }

  @Test
  public void login() {
    UseTheService.as(client).getService().authenticate(client.getName());

    then(client).should(seeThat(
        new IsAuthenticated(), is(true)
    ));
  }

  @Test
  public void logout() {
    MyService service = UseTheService.as(client).getService();
    service.authenticate(client.getName());
    service.logout(client.getName());

    then(client).should(seeThat(
        new IsAuthenticated(), is(false)
    ));
  }
}