package models;

import play.db.jpa.Model;

import javax.persistence.ManyToOne;

public class Detail extends Model {
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
