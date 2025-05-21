package Common.network.responses;

import Common.utiluty.Commands;

public class UpdateResponse extends Response {
    public UpdateResponse(String error) {
        super(Commands.UPDATE, error);
    }
}