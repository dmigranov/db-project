package models;

import play.db.jpa.Model;

import javax.persistence.Entity;

@Entity
public class DetailType extends Model {
    public String name;

    public DetailType(String name)
    {
        this.name = name;
    }
}
