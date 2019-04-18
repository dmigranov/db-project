package models;

import play.data.validation.Min;
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

    @Min(0) long detailCost; //нужно ли хранить? можно же посчитаьт
    @Min(0)int workCost;

    Date workBegin;
    Date workEnd;

    boolean isGuaranteed;

    @ManyToOne
    Trouble trouble;    //или лучше manytomany?

    boolean type;   //сборка - 0, ремонт - 1. плохо?

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
