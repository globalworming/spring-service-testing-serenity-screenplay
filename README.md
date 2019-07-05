# DRY testing of @Autowired @Services using a proxy 

## premise

When testing spring boot applications with [serenity-bdd](http://www.thucydides.info/) I tried multiple approaches, three in more details below.

Given is a pretty basic service with three business functions. The goal it to create a BDD report on the tests of these functions.

    public interface MyService {
      void authenticate(String name);
      boolean isAuthenticated(String name);
      void logout(String name);
    }
      
## experiments
### step library
Following the [documentation on step libraries](https://serenity-bdd.github.io/theserenitybook/latest/step-libraries.html) and [spring testing](https://github.com/serenity-bdd/serenity-documentation/blob/master/src/asciidoc/spring.adoc) it is easy to use these in your tests like

    class MyServiceSteps {
    @Autowired MyService myService
    
      void authenticate(String name) {
        myservice.authenticate(name)
      }
      // TODO repeat for the other methods
    }
    
    class MyserviceTest() {
      @Steps MyServiceSteps myServiceSteps
    }
      
As you can see, the drawback here is that you have to duplicate every service method as step.

### screenplay
This has the same drawback, you have to duplicate the service as performables.

    // TODO create performables for the other methods
    class Authenticate implements Performable {
      public void <T extends Actor> performAs(T actor) {
        UseTheService.as(actor).getService().authenticate(actor.name, actor.token);
      }
    }
        
### call the service directly
Using the @Autowired instance directly works, but does not show up in the BDD reports

    class MyServiceTest {
      @Autowired MyService myService;
      
      public void whenAuthenticating() {
        myService.authenticate(actor.getName(), actor.getToken);
        actor.should(seeThat(a -> myService.isAuthenticated(actor.getToken), is(true));
      }
    }
        
## workaround
I was able to find a compromise between being DRY, readable tests and reporting on the steps using a proxy. By introducing a common interface `api.MyService` one can intercept the methods called and convert them to instrumented performables so the steps are reported. Also the connection to the actor is maintained well IMHO. See the example [ServiceTest](https://github.com/globalworming/spring-service-testing-serenity-screenplay/blob/master/src/test/java/com/example/demo/service/MyServiceImplTest.java), [UseTheService.as(actor).getService()](https://github.com/globalworming/spring-service-testing-serenity-screenplay/blob/master/src/test/java/com/example/demo/screenplay/abilities/UseTheService.java#L32) and [StepReportingProxy](https://github.com/globalworming/spring-service-testing-serenity-screenplay/blob/master/src/test/java/com/example/demo/screenplay/abilities/StepReportingProxy.java)
