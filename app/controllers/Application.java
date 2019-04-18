package controllers;

import play.*;
import play.data.validation.Valid;
import play.db.DB;
import play.mvc.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public static void troubles() throws SQLException {
        /*List troubles = Project.find(
                "select t.name, count(*) as trouble_count FROM Project p JOIN Trouble t ON p.trouble = t GROUP BY t.id, t.name"
        ).fetch();*/

        Connection conn = DB.getConnection();
        Statement statement = conn.createStatement();
        boolean isResultSet = statement.execute("select t.name as t_name, trouble_count FROM Trouble t JOIN (select t.id as id, count(*) as trouble_count FROM Project p JOIN Trouble t ON p.trouble_id = t.id GROUP BY t.id) tc ON t.id = tc.id");
        ResultSet resultSet = null;
        List<Object[]> resultList = new ArrayList<>();
        if(isResultSet) {
            resultSet = statement.getResultSet();
            while(resultSet.next())
            {
                resultList.add(new Object[] {resultSet.getString("t_name"), resultSet.getInt("trouble_count")});
            }
        }

        render(resultList);
    }


    public void fillDB()
    {
        /*Client ivan = new Client("Ivan", "Ivanov", "89003431234", "vano@google.com", false).save();
        new Client("FSfds", "fdsfds", "89003431234", "vdsgle.com", false).save();

        Employee manager = new Employee("Petr", "Petrov", "manager", "89003431234", "vano@google.com", 40000, 0.05).save();
        Employee engineer = new Employee("Tttt", "Cccc", "engineer", "89003431234", "vano@google.com", 40000, 0.05).save();
        new Employee("Zzzz", "Ssss", "engineer", "89003431234", "vano@google.com", 40000, 0.05).save();

        Trouble trouble = new Trouble("Some trouble").save();
        Trouble trouble2 = new Trouble("Another trouble").save();

        new Project(ivan, engineer, manager, 10, 30, new Date(2019, 4, 14), new Date(2019, 4, 15), false, trouble,true).save();
        new Project(ivan, engineer, manager, 20, 60, new Date(2019, 4, 14), new Date(2019, 4, 15), false, trouble,true).save();
        new Project(ivan, engineer, manager, 345, 435, new Date(2019, 4, 12), new Date(2019, 4, 13), false, trouble2,true).save();*/

        Client c = new Client("FSsf", "fdsfsdf", "79003431234", "vano@nsi.com", false);

        if(!validation.valid(c).ok)
        {
            //todo: методика я так понимаю такая
            System.out.println("hellloooo");
        }
        else
        {
            c.save();
        }
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