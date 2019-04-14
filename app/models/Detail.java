package models;

import javax.persistence.ManyToOne;

public class Detail {
    @ManyToOne
    DetailType type;

    int cost;

    String name;
    String description;

    int count;

    public Detail(DetailType type, int cost, String name, String description, int count)
    {
        this.type = type;
        this.cost = cost;
        this.name = name;
        this.description = description;
        this.count = count;
    }
}
