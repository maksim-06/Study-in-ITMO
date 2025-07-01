package Common.user;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private  final String login;
    private final String password;

    public User(String login, String password){
        this.login = login;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public boolean validate(){
        return getLogin().length() < 40;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='********'" +
                '}';
    }
}
