package Common.network.requests;

import Common.domein.Product;

import java.io.Serializable;

public class Request implements Serializable {
    private final String commandName;
    private final String argument;
    private Product body;

    public Request(String commandName, String argument, Product body) {
        this.commandName = commandName;
        this.argument = argument;
        this.body = body;
    }

    public Request(String commandName, String argument) {
        this.commandName = commandName;
        this.argument = argument;
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

    @Override
    public String toString() {
        return "Request{" +
                "commandName='" + commandName + '\'' +
                ", argument='" + argument + '\'' +
                ", body=" + body +
                '}';
    }
}
