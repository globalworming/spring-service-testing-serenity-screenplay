package com.example.demo.screenplay.abilities;

import net.serenitybdd.screenplay.Actor;

import java.lang.reflect.Proxy;

public class InstrumentedInterface {

  public static Object of(Object implementation, Class<?> interfaceClass,  Actor actor) {
    return Proxy.newProxyInstance(
        InstrumentedInterface.class.getClassLoader(),
        new Class[] { interfaceClass },
        new StepReportingProxy(implementation, actor));
  }
}
