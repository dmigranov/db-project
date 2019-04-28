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
        boolean isResultSet = statement.execute("select t.name as t_name, trouble_count FROM Trouble t JOIN (select t.id as id, count(*) as trouble_count FROM Project p JOIN Trouble t ON p.trouble_id = t.id GROUP BY t.id) tc ON t.id = tc.id ORDER BY trouble_count DESC");
        ResultSet resultSet = null;
        List<Object[]> resultList = new ArrayList<>();
        if (isResultSet) {
            resultSet = statement.getResultSet();
            while (resultSet.next()) {
                resultList.add(new Object[]{resultSet.getString("t_name"), resultSet.getInt("trouble_count")});
            }
        }

        render(resultList);
    }

    public void fillDB() {
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

        /*Client c = new Client("FSsf", "fdsfsdf", "79003431234", "vano@nsi.com", false);
        if(!validation.valid(c).ok)
        {
            //todo: методика я так понимаю такая
            System.out.println("hellloooo");
        }
        else
        {
            c.save();
        }*/

        /*Employee manager = new Employee("Petr", "Petrov", "manager", "89003431234", "vano@google.com", 40000, 0.05).save();
        System.out.println(validation.valid(manager).ok);
        Employee engineer = new Employee("Tttt", "Cccc", "fdsfsdf", "89003431234", "vano@google.com", 40000, 0.05).save();
        System.out.println(validation.valid(engineer).ok);
        */

        DetailType processor = new DetailType("processor");
        DetailType motherboard = new DetailType("motherboard");
        DetailType ram = new DetailType("ram");
        DetailType videocard = new DetailType("video card");
        DetailType storage = new DetailType("storage");
        DetailType powersupply = new DetailType("power supply");
        DetailType networkcard = new DetailType("network card");
        DetailType diskstorage = new DetailType("disk storage");

//        DetailType type, int cost, String name, String description, int count
        Detail detail1 = new Detail(processor, 10000, "Intel Core i7", "very good processor", 3);
        Detail detail2 = new Detail(processor, 11000, "AMD Ryzen 7", "not very good processor", 2);
        Detail detail3 = new Detail(videocard, 20000, "Nvidia GeForce 1660", "top videocard", 10);
        Detail detail4 = new Detail(videocard, 15000, "Nvidia GeForce 1080", "old videocard", 5);

        processor.save();
        motherboard.save();
        ram.save();
        videocard.save();
        storage.save();
        powersupply.save();
        networkcard.save();
        diskstorage.save();


        detail1.save();
        detail2.save();
        detail3.save();
        detail4.save();

    }

    ///выводит список всех проектов, на которых задействован работник
    public static void projectsOfEmployee(long id, Date startDate, Date endDate) {      //todo: дата

        List projects = Project.find(
                "Select p, c.firstName, c.lastName FROM Project p JOIN Client c ON p.client = c  where p.engineer.id = ?1 or p.manager.id = ?1", id
        ).fetch();

        Employee employee = Employee.findById(id);
        List employees = Employee.findAll();

        render(projects, employee, employees);
    }

    ///выводит список всех проектов и сортирует их
    public static void projects(int sortType, int desc) {   //sortType = 0 - по сумме стимостей, 2 - по типу; desc = 0 - asc, desc = 1 = desc
        //todo: по идее, стоимость деталей является избыточной, её нужно получать как сумму из DetailOrder'ов!
        List projects = null;
        String order = (desc == 1 ? " desc" : " asc");
        if(sortType == 0) {     //по стоимости
            projects = Project.find(
                    "Select p, c.firstName, c.lastName, (detailCost + workCost) as cost FROM Project p JOIN Client c ON p.client = c ORDER BY cost" + order
            ).fetch();
        }
        else
        {
            projects = Project.find(
                    "Select p, c.firstName, c.lastName, (detailCost + workCost) as cost FROM Project p JOIN Client c ON p.client = c ORDER BY type" + order
            ).fetch();
        }

        render(projects);
    }

    //id - detailType.id
    public static void popularDetails(long id) {
        Detail.find("Select count(d.id) FROM Detail d JOIN DetailOrder o ON DetailOrder.detail = d WHERE d.type = ?1 GROUP BY d.id", id).fetch(5);
        render();
    }

    public static void details() throws SQLException {
        //todo: сделать возможность изменения количества деталей (только добавления, удалять будем при заказах (триггер))!!!
        List types = DetailType.findAll();

        List resultList = Detail.findAll();
        /*Connection conn = DB.getConnection();
        Statement statement = conn.createStatement();
        boolean isResultSet = statement.execute("select * from Detail d_table ORDER BY name");
        ResultSet resultSet = null;
        List<Object[]> resultList = new ArrayList<>();
        if (isResultSet) {
            resultSet = statement.getResultSet();
            while (resultSet.next()) {
                resultList.add(new Object[]{resultSet.getString("name")});
            }
        }*/ //это же неправильно (точнее, возвращает только бесструктурные имена), я объяснил

        render(resultList, types);
    }

    public static void addDetail(String name, String description, int cost, int count, long type) throws SQLException {
        System.out.println(name + description + type);

        Application.details();
    }

}
