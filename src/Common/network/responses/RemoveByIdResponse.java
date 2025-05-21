package Common.network.responses;


import Common.utiluty.Commands;

public class RemoveByIdResponse extends Response {
    public RemoveByIdResponse(String error) {
        super(Commands.REMOVE_BY_ID, error);
    }
}