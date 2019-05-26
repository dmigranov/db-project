package models;

import org.hibernate.annotations.Formula;
import play.data.validation.*;
import play.db.jpa.Model;

import javax.persistence.Entity;

@Entity
public class Employee extends Model {
    public String firstName;
    public String lastName;
    @CheckWith(PositionCheck.class) public String position;
    @Phone public String phoneNumber;
    @Email public String email;
    @Min(0) public int salary;
    @Min(0) @Max(1)public double bonusPercent;
    //@Formula("(select salary + bonusPercent * SUM(COALESCE(p.workCost, 0)) from Project p JOIN Employee e on p.engineer_id = e.id OR p.manager_id = e.id WHERE e.id = id )")
    @Formula("(select salary + bonusPercent * COALESCE(SUM(COALESCE(p.workCost, 0)), 0) from Project p WHERE p.engineer_id = id OR p.manager_id = id)")
    public int resultSalary = 0;

    public Employee(String firstName, String lastName, String position, String phoneNumber, String email, int salary, double bonusPercent)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.salary = salary;
        this.bonusPercent = bonusPercent;
        this.resultSalary = salary;
    }

    private class PositionCheck extends Check {
        @Override
        public boolean isSatisfied(Object validatedObject, Object value) {
            return "manager".equals(value) || "engineer".equals(value);
        }
    }
}
