package models;

import play.db.jpa.Model;

public class Trouble extends Model {
    public String name;

    public Trouble(String name)
    {
        this.name = name;
    }
}
