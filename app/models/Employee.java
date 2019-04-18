package models;

import play.data.validation.*;
import play.db.jpa.Model;

import javax.persistence.Entity;

@Entity
public class Employee extends Model {
    public String firstName;
    public String lastName;
    @CheckWith(PositionCheck.class) public String position; //todo
    @Phone public String phoneNumber;
    @Email public String email;
    @Min(0) public int salary;
    @Min(0) @Max(1)public double bonusPercent;

    public Employee(String firstName, String lastName, String position, String phoneNumber, String email, int salary, double bonusPercent)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.salary = salary;
        this.bonusPercent = bonusPercent;
    }

    private class PositionCheck extends Check {
        @Override
        public boolean isSatisfied(Object validatedObject, Object value) {
            return "manager".equals(value) || "engineer".equals(value);
        }
    }
}
