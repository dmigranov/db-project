package models;

import javax.persistence.*;

import play.data.validation.Email;
import play.data.validation.Phone;
import play.db.jpa.*;

@Entity
public class Client extends Model {
    public String firstName;
    public String lastName;
    @Phone public String phoneNumber;

    @Email public String email;
    public boolean isPhysical;
    //id? но в суперкласе Model уже. @GeneratedValue

    public Client(String firstName, String lastName, String phoneNumber, String email, boolean isPhysical)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isPhysical = isPhysical;
    }

}
