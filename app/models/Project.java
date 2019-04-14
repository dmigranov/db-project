package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Project extends Model {
    @ManyToOne
    Client client;

    @ManyToOne
    Employee engineer;

    @ManyToOne
    Employee manager;

    long detailCost; //нужно ли хранить? можно же посчитаьт
    int workCost;

    Date workBegin;
    Date workEnd;

    boolean isGuaranteed;

    @ManyToOne
    Trouble trouble;    //или лучше manytomany?

    boolean type;   //сборка - 0, ремонт - 1

    public Project(Client client, Employee engineer, Employee manager, long detailCost, int workCost, Date workBegin, Date workEnd, boolean isGuaranteed, Trouble trouble, boolean type)
    {

        this.client = client;
        this.engineer = engineer;
        this.manager = manager;
        this.detailCost = detailCost;
        this.workCost = workCost;
        this.workBegin = workBegin;
        this.workEnd = workEnd;
        this.isGuaranteed = isGuaranteed;
        this.trouble = trouble;
        this.type = type;
    }
}
