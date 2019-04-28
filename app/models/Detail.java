package models;

import play.data.validation.Min;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Detail extends Model {
    @ManyToOne
    DetailType type;

    @Min(1)int cost;

    String name;
    String description;

    @Min(0)int count;

    public Detail(DetailType type, int cost, String name, String description, int count)
    {
        this.type = type;
        this.cost = cost;
        this.name = name;
        this.description = description;
        this.count = count;
    }
}
