package models;

import play.data.validation.Min;
import play.db.jpa.Model;

import javax.persistence.ManyToOne;

public class DetailOrder extends Model {
    @ManyToOne
    public Project project;
    @ManyToOne
    public Detail detail;
    @Min(0) public int count;   //trigger!

    public DetailOrder(Project project, Detail detail, int count)
    {
        this.project = project;
        this.detail = detail;
        this.count = count;
    }
}
