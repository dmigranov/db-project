package models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import play.data.validation.Min;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Project extends Model {
    @ManyToOne @OnDelete(action = OnDeleteAction.CASCADE)
    Client client;

    @ManyToOne @OnDelete(action = OnDeleteAction.CASCADE)
    Employee engineer;

    @ManyToOne @OnDelete(action = OnDeleteAction.CASCADE)
    Employee manager;

    @Min(0)long detailCost; //нужно ли хранить? можно же посчитаьт
    @Min(1)int workCost;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project", orphanRemoval = true)
    List<DetailOrder> detailOrders = new ArrayList<>();

    Date workBegin;
    Date workEnd;

    boolean isGuaranteed;

    @ManyToOne
    Trouble trouble;    //или лучше manytomany?
    String troubleDescription;

    boolean type;   //сборка - 0, ремонт - 1. плохо?

    public Project(Client client, Employee engineer, Employee manager, long detailCost, int workCost, Date workBegin, Date workEnd, boolean isGuaranteed, Trouble trouble, String troubleDescription, boolean type)
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
        this.troubleDescription = troubleDescription;
        this.type = type;
    }

    public void setManager(Employee manager)
    {
        this.manager = manager;
    }

    public void setEngineer(Employee engineer)
    {
        this.engineer = engineer;
    }
}
