package com.example.demo.screenplay.abilities;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.InstrumentedTask;
import net.serenitybdd.screenplay.Performable;
import net.thucydides.core.annotations.Step;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class StepReportingProxy implements InvocationHandler {

  private final List<String> OBJECT_METHODS = Arrays.stream(Object.class.getDeclaredMethods()).map(Method::getName).collect(Collectors.toList());

  private final Object target;
  private final Map<String, Method> methods = new HashMap<>();
  private final Actor actor;

  public StepReportingProxy(Object target, Actor actor) {
    this.target = target;
    this.actor = actor;

    for(Method method: target.getClass().getDeclaredMethods()) {
      this.methods.put(method.getName(), method);
    }
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{

    // do not report on this methods, just pass along
    if (OBJECT_METHODS.contains(method.getName())) {
      return methods.get(method.getName()).invoke(target, args);
    }

    String whatTheActorDoes = "uses the service to " + method.getName().replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase();
    if (args != null) {
      whatTheActorDoes += " " +  Arrays.stream(args).map(Object::toString).collect(Collectors.joining());
    }

    PerformableRemembersResult task = InstrumentedTask.of(new PerformableRemembersResult(whatTheActorDoes, () -> method.invoke(target, args)));
    actor.attemptsTo(task);
    return task.getResult();

  }

  public static class PerformableRemembersResult implements Performable {

    private Callable<Object> task;
    private Object result;
    @SuppressWarnings("unused")
    private String whatTheActorDoes;

    Object getResult() {
      return result;
    }

    @SuppressWarnings("unused")
    // needed for instrumentation
    public PerformableRemembersResult() {
    }

    public PerformableRemembersResult(String whatTheActorDoes, Callable<Object> task) {
      this.task = task;
      this.whatTheActorDoes = whatTheActorDoes;
    }

    @Override
    @Step("{0} #whatTheActorDoes")
    public <T extends Actor> void performAs(T actor) {
      try {
        result = task.call();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}
