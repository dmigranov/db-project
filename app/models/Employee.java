package models;

import play.data.validation.Email;

public class Employee {
    public String firstName;
    public String lastName;
    public String position;
    public String phoneNumber;
    @Email
    public String email;
    public int salary;
    public double bonusPercent;
}
