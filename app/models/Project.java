package models;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Date;

public class Project {
    @ManyToOne
    Client client;

    @ManyToOne
    Employee engineer;

    @ManyToOne
    Employee manager;

    long detailCost; //нужно ли хранить? можно же посчитаьт
    int workCost;

    Date workBegin, workEnd;

    boolean isGuaranteed;

    @ManyToOne
    Trouble trouble;    //или лучше manytomany?

    boolean type;
}
