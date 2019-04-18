package models;

import play.data.validation.Min;
import play.db.jpa.Model;

public class DetailOrder extends Model {
    public Project project;
    public Detail detail;
    @Min(0)int count;   //trigger!

    public DetailOrder()
    {

    }
}
