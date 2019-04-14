package models;

import javax.persistence.*;

import play.data.validation.Email;
import play.db.jpa.*;

@Entity
public class Client extends Model {
    public String firstName;
    public String lastName;
    public String phoneNumber;
    @Email
    public String email;
    public boolean isPhysical;
    //id? но в суперкласе Model уже

    public Client(String firstName, String lastName, String phoneNumber, String email, boolean isPhysical)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isPhysical = isPhysical;
    }

}
