package models;

import play.db.jpa.Model;

import javax.persistence.Entity;

@Entity
public class Trouble extends Model {
    public String name;

    public Trouble(String name)
    {
        this.name = name;
    }
}
