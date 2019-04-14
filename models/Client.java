package models;

import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Client extends Model {
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String email;
    public boolean isPhysical;

}
