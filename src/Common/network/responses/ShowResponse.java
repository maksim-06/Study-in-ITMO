package Common.network.responses;

import Common.domein.Product;
import Common.utiluty.Commands;

import java.util.List;

public class ShowResponse extends Response {
    public final List<Product> products;

    public ShowResponse(List<Product> products, String error) {
        super(Commands.SHOW, error);
        this.products = products;
    }
}