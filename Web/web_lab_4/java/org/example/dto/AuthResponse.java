package org.example.dto;

public class AuthResponse {
    private String message;
    private Long id;
    private String login;

    public AuthResponse() {
    }

    public AuthResponse(Long id, String login) {
        this.message = "Success";
        this.id = id;
        this.login = login;
    }


    public AuthResponse(String message,  Long id, String login) {
        this.message = message;
        this.id = id;
        this.login = login;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
