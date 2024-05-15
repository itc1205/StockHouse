package com.itc.StockHouse.motherobject;

import com.itc.StockHouse.model.CustomerEntity;

public class CustomerBuilder {
    private static final String DEFAULT_LOGIN = "DEFAULT_LOGIN";
    private static final String DEFAULT_EMAIL = "DEFAULT_EMAIL";

    private static final Boolean DEFAULT_IS_ACTIVE = true;

    private String login = DEFAULT_LOGIN;
    private String email = DEFAULT_EMAIL;
    private Boolean isActive = DEFAULT_IS_ACTIVE;


    public CustomerBuilder withLogin(String login) {
        this.login = login;
        return this;
    }

    public CustomerBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public CustomerBuilder withIsActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public CustomerEntity build() {
        return CustomerEntity.builder()
                .email(email)
                .isActive(true)
                .login(login)
                .build();
    }
}
