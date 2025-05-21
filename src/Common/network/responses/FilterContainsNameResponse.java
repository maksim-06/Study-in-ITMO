package Common.network.responses;

import Common.domein.Product;
import Common.utiluty.Commands;

import java.util.List;

public class FilterContainsNameResponse extends Response {
    public final List<Product> filteredProducts;

    public FilterContainsNameResponse(List<Product> filteredProducts, String error) {
        super(Commands.FILTER_CONTAINS_PART_NUMBER, error);
        this.filteredProducts = filteredProducts;
    }
}
