package Story.constant;

public enum Condition {
    SAD("грустный"),
    HAPPY("счастливый");


    private final String description;
    Condition(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

}
