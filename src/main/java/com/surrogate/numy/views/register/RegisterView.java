package com.surrogate.numy.views.register;

import com.surrogate.numy.models.register.RegisterRequest;
import com.surrogate.numy.models.register.RegisterResponse;
import com.surrogate.numy.services.auth.AuthService;
import com.surrogate.numy.views.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route("/register")
public class RegisterView extends VerticalLayout {
    private final Logger logger = LoggerFactory.getLogger(RegisterView.class);
    public RegisterView(AuthService authService){

        setSizeFull();
        getStyle().set("background-size", "cover");
        getStyle().set("background-position", "center");
        getStyle().set("background-repeat", "no-repeat");
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        LoginForm loginForm = getRegisterForm(authService);
        Button loginButton = new Button("Iniciar Sesion");
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginButton.addClickListener(e -> UI.getCurrent().navigate(LoginView.class));

        add(loginForm, loginButton);
    }
    private  @NotNull LoginForm getRegisterForm(AuthService authService) {
        LoginForm registerForm = new LoginForm();
        LoginI18n registerI18n = LoginI18n.createDefault();
        registerI18n.getForm().setTitle("Registro");
        registerI18n.getForm().setSubmit("Aceptar");







        registerForm.setI18n(registerI18n);
        registerForm.setForgotPasswordButtonVisible(false);

        registerForm.addLoginListener(event -> {
            String username = event.getUsername();
            logger.info("Attempting to register user: {}", username);
            String password = event.getPassword();
            RegisterRequest registerRequest = new RegisterRequest(username, password, "USUARIO", null, null, username+"@gmail.com");
            RegisterResponse response = authService.register(registerRequest);


            if (response.getHttpCode() !=200 ) {

                registerForm.setError(true);
                registerI18n.getErrorMessage().setMessage(response.getMessage());
                registerForm.setI18n(registerI18n);

            }
            else {
                UI.getCurrent().getPage().setLocation("/login");
            }



        });
        return registerForm;
    }
}


