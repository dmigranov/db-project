package models;

import play.data.validation.Email;
import play.db.jpa.Model;

public class Employee extends Model {
    public String firstName;
    public String lastName;
    public String position; //todo
    public String phoneNumber;
//    @Email
    public String email;
    public int salary;
    public double bonusPercent;



}
