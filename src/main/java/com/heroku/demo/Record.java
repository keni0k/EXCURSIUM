package com.heroku.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotEmpty
    private String data = "Same";

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    private String locate = "Same";

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    private String head = "Same";

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    private String imgLink = "Same";

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
