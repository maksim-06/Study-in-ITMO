package Common.network.responses;

import Common.utiluty.Commands;

public class AddResponse extends Response {
    public final long newId;

    public AddResponse(long newId, String error) {
        super(Commands.ADD, error);
        this.newId = newId;
    }
}
