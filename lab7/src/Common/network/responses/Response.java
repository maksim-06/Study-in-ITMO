package Common.network.responses;

import Common.domein.Product;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

public class Response implements Serializable {
    private final String name;
    private Collection<Product> collection;
    private String error;
    private String response = "";

    public Response(String name, Collection<Product> collection) {
        this.name = name;
        this.collection = collection;
    }

    public Response(String name, String error) {
        this.name = name;
        this.error = error;
    }

    public String getName() {
        return name;
    }

    public String getResponse() {
        return response;
    }

    public Collection<Product> getCollection() {
        return collection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return Objects.equals(name, response.name) && Objects.equals(collection, response.collection)
                && Objects.equals(error, response.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, collection);
    }

    @Override
    public String toString(){
        return (name.isEmpty()
                        ? ""
                        : name) +
                (collection == null
                        ? ""
                        : collection.toString());
    }
}



