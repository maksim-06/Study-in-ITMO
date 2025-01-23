package Story.constant;

public enum Stone {
    BIG("большой"),
    AVERAGE("средний"),
    SMALL("маленький"),

    COLOUR("Узкий пологий берег, тянувшийся полосой вдоль моря, \n" +
            "был ограничен с противоположной стороны обрывистыми, словно подмытыми водой, холмами, которые поросли сверху зеленой травкой и мелким кустарником. \n" +
            "Сам берег был покрыт ослепительно белым песочком и какими-то прозрачными камнями, напоминавшими обломки ледяных или стеклянных глыб.");

    private final String description;

    Stone(String description){
        this.description = description;
    }

    @Override
    public String toString(){
        return description;
    }
}
