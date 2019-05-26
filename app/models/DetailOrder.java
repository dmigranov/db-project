package models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import play.data.validation.Min;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class DetailOrder extends Model {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")

    public Project project;
    @ManyToOne
    public Detail detail;
    @Min(1) public int count;   //trigger!

    public DetailOrder(Project project, Detail detail, int count)
    {
        this.project = project;
        this.detail = detail;
        this.count = count;
    }
}
