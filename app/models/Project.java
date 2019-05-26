package models;

import org.hibernate.annotations.Formula;
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

    //@Formula("select sum(o.count * o.detail.count) from DetailOrder o where o.project.id = id")
    //@Formula("select sum(o.count * (select d.count from Detail d where d.id = o.detail_id)) from DetailOrder o where o.project_id = id")

    @Formula("(select COALESCE(sum(o.count * (select d.cost from Detail d where d.id = o.detail_id)), 0) from DetailOrder o where o.project_id = id)")
    @Min(0)long detailCost; //нужно ли хранить? можно же посчитаьт
    @Min(1)int workCost;

    @OneToMany(cascade = {CascadeType.REMOVE}, mappedBy = "project", orphanRemoval = true)
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

    public void setDetailCost(long cost) {
        detailCost = cost;
    }

    public long getDetailCost()
    {
        return detailCost;
    }
}
