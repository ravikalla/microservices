package in.ravikalla.microservices.savingsaccount.cucumber.stepdefs;

import in.ravikalla.microservices.savingsaccount.SavingsaccountApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = SavingsaccountApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
