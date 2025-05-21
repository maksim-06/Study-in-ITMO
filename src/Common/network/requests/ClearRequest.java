package Common.network.requests;


import Common.utiluty.Commands;

public class ClearRequest extends Request{
    public ClearRequest(){
        super(Commands.CLEAR);
    }
}
