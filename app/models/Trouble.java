package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Trouble extends Model {
    @Id @GeneratedValue
    public long id;
    public String name;
    public Trouble(String name)
    {
        this.name = name;
    }
}
