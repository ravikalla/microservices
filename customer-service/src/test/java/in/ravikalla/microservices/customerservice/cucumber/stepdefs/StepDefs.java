package in.ravikalla.microservices.customerservice.cucumber.stepdefs;

import in.ravikalla.microservices.customerservice.CustomerserviceApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = CustomerserviceApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
