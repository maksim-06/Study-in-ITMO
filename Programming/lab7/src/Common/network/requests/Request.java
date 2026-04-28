package Common.network.requests;

import Common.domein.Product;
import Common.user.User;

import java.io.Serializable;

public class Request implements Serializable {
    private final String commandName;
    private final String argument;
    private Product body;
    public User user;

    public Request(String commandName, String argument, Product body, User user) {
        this.commandName = commandName;
        this.argument = argument;
        this.body = body;
        this.user = user;
    }

    public Request(String commandName, String argument, User user) {
        this.commandName = commandName;
        this.argument = argument;
        this.user = user;
    }



    public String getName() {
        return commandName;
    }

    public String getArgument() {
        return argument;
    }

    public Product getBody() {
        return body;
    }

    public User getUser(){
        return user;
    }

    @Override
    public String toString() {
        return "Request{" +
                "commandName='" + commandName + '\'' +
                ", argument='" + argument + '\'' +
                ", body=" + body +
                '}';
    }
}
