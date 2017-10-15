package com.heroku.demo.message;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Message {

    public Message(String p1, String p2, int id_of_guide, String data) {
        this.p1 = p1;
        this.p2 = p2;
        this.id_of_guide = id_of_guide;
        this.data = data;
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\", \"p1\":\"" + p1 + "\", \"p2\":\"" + p2 +
                "\" , \"id_of_guide\":\"" + id_of_guide + "\" , \"data\":\"" + data + "\"}";
    }

    public Message() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String p1 = "";
    private String p2 = "";
    private int id_of_guide = -1;
    private String data = "";

    public long getId() {
        return id;
    }

    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public String getP2() {
        return p2;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }

    public int getId_of_guide() {
        return id_of_guide;
    }

    public void setId_of_guide(int id_of_guide) {
        this.id_of_guide = id_of_guide;
    }

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }


}