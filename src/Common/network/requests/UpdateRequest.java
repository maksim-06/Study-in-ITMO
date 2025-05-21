package Common.network.requests;

import Common.domein.Product;
import Common.utiluty.Commands;

public class UpdateRequest extends Request {
    public final long id;
    public final Product updatedProduct;

    public UpdateRequest(long id, Product updatedProduct) {
        super(Commands.UPDATE);
        this.id = id;
        this.updatedProduct = updatedProduct;
    }
}
