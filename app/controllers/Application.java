package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        Client client = Client.find("order by firstName desc").first();

        render(client);

    }

    public static void salary() {
        List<Employee> employees = Employee.find("order by salary desc").fetch();

        //todo: подавать ещё итоговую зарплату как функцию оклада процента и проектов в которых участвовал
        render(employees);
    }

    public static void troubles() {
        /*List troubles = Project.find(
                "select t.name, trouble_count FROM Trouble t JOIN (select t.id as id, count(*) as trouble_count FROM Project p JOIN Trouble t ON p.trouble.id = t.id GROUP BY t.id) as tc ON t.id = tc.id"
        ).fetch();*/

        List troubles = Project.find(
                "select t.name, count(*) as trouble_count FROM Project p JOIN Trouble t ON p.trouble = t GROUP BY t.id, t.name"
        ).fetch();

        troubles.get(0);

        render(troubles);
    }


    public static void projects(long id) {
        //System.out.println(id);
        /*List<Project> projects = Project.find(
                "engineer.id = ?1 or manager.id = ?1" , id
        ).fetch();*/

        List projects = Project.find(
                "Select p, c.firstName, c.lastName FROM Project p JOIN Client c ON p.client = c  where p.engineer.id = ?1 or p.manager.id = ?1", id
        ).fetch();

        Employee employee = Employee.findById(id);


        render(projects, employee);

    }

}