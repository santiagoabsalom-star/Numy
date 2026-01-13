package com.surrogate.numy.views.login;
import com.surrogate.numy.models.login.LoginRequest;
import com.surrogate.numy.models.login.LoginResponse;
import com.surrogate.numy.services.auth.AuthService;

import com.surrogate.numy.views.register.RegisterView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.jetbrains.annotations.NotNull;

@Route("/login")
public class LoginView extends VerticalLayout {
    public LoginView(AuthService authService) {
     setSizeFull();


        getStyle().set("background-size", "cover");
        getStyle().set("background-position", "center");
        getStyle().set("background-repeat", "no-repeat");
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        LoginForm loginForm = getLoginForm(authService);
        Button registerButton = new Button("Registrarse");
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registerButton.addClickListener(e -> UI.getCurrent().navigate(RegisterView.class))  ;

        add(loginForm, registerButton);

}

    private static @NotNull LoginForm getLoginForm(AuthService authService) {
        LoginForm loginForm = new LoginForm();
        LoginI18n loginI18n = LoginI18n.createDefault();
        loginI18n.getForm().setTitle("Inicio de sesion");
        loginI18n.getForm().setSubmit("Aceptar");
        loginI18n.getErrorMessage().setTitle("Error en el inicio de sesion");
       
       
       
       


        loginForm.setI18n(loginI18n);
        loginForm.setForgotPasswordButtonVisible(false);

        loginForm.addLoginListener(event -> {
            String username = event.getUsername();
            String password = event.getPassword();
            LoginRequest loginRequest = new LoginRequest(username, password);
            LoginResponse loginResponse = authService.login(loginRequest);


                if (loginResponse.getHttpCode() ==200 ) {

                    UI.getCurrent().getPage().setLocation("/");




            } else {
                    loginForm.setError(true);

                    loginI18n.getErrorMessage().setMessage(loginResponse.getMessage());

                    loginForm.setI18n(loginI18n);
                }
        });
        return loginForm;
    }
}
