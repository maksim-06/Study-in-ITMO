package Story.items;


public record Crystal(Story.constant.Stone size){
    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Crystal)) {
            return false;
        }
        Crystal crystal = (Crystal) obj;
        return this.size.equals(crystal.size);
    }

    @Override
    public String toString() {
        return size.name();
    }

    @Override
    public int hashCode() {
        return size.hashCode();
    }
}
