package com.heroku.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Record {

    public Record(long id, String data, String locate, String head, String img_link, int what) {
        this.id = id;
        this.data = data;
        this.locate = locate;
        this.head = head;
        this.img_link = img_link;
        this.what = what;
    }

    public long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotEmpty
    private String data = "";

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    private String locate = "";

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    private String head = "";

    public String getImg_link() {
        return img_link;
    }

    public void setImg_link(String img_link) {
        this.img_link = img_link;
    }

    private String img_link = "";

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    private int what = "";

}
