package in.ravikalla.microservices.ui.cucumber.stepdefs;

import in.ravikalla.microservices.ui.UiApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = UiApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
