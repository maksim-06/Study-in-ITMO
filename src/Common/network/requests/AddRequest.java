package Common.network.requests;

import Common.domein.Product;
import Common.utiluty.Commands;

public class AddRequest extends Request{
    public final Product product;

    public AddRequest(Product product) {
        super(Commands.ADD);
        this.product = product;
    }
}
