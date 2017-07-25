package com.heroku.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/drop")
    void drop() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS twits");
    }

    @RequestMapping("/create")
    void create() {
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS twits("+
                        "_id INTEGER NOT NULL," +
                        "token VARCHAR," +
                        "twit VARCHAR," +
                        "who INTEGER)"
        );
    }

    @RequestMapping ("/deletemsg")
    Boolean deletetwit(@RequestParam("token") String token,
                        @RequestParam("id") int id,
                       @RequestParam("who") int who){
        jdbcTemplate.update
                ("DELETE FROM twits WHERE token='"+token+"' AND _id = '" + id + "' AND who = '" + who + "'");
        return true;
    }

    @RequestMapping ("/deletemsgs")
    Boolean deletetwits(@RequestParam("token") String token,
                        @RequestParam("who") int who){
        jdbcTemplate.update
                ("DELETE FROM twits WHERE token='"+token+"' AND who = '"+who+"'");
        return true;
    }

    @RequestMapping ("/findmsg")
    String findtwit(@RequestParam("token") String token,
                     @RequestParam("id") int id,
                    @RequestParam("who") int who){
        String st = "null";
        if (jdbcTemplate.query("SELECT * FROM twits WHERE token ='" + token+"' AND _id = '" + id +"' AND who= '" + who +"'", new Object[]{}, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("twit");
            }}).size()>0)
            st =  "{"+jdbcTemplate.query("SELECT * FROM twits WHERE token ='" + token+"' AND _id = '" + id +"' AND who= '" + who +"'", new Object[]{}, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return "{\"msg\":\""+rs.getString("twit")+"\"}";
            }}).get(0);
        return st;
    }

    @RequestMapping ("/getmsgs")
    String twits(@RequestParam("token") String token,
                       @RequestParam("who") int who){

        List<String> arrayList = jdbcTemplate.query("SELECT * FROM twits WHERE token ='" + token+"' AND who = '" + who + "'", new Object[]{}, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return "{\"msg\":\""+rs.getString("twit")+"\"}";
            }});
        StringBuilder stringBuilder = new StringBuilder("{\"msgs\":[");
        for (int i = 0; i<arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size()-i>1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }

    @RequestMapping ("/getmsgsafter")
    String twits(@RequestParam("token") String token,
                 @RequestParam("who") int who,
                 @RequestParam("id") int id){


        List<String> arrayList = jdbcTemplate.query("SELECT * FROM twits WHERE token ='" + token+"' AND who = '" + who + "' AND _id > '"+id+"'", new Object[]{}, new RowMapper<String>(){
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return "{\"msg\":\""+rs.getString("twit")+"\"}";
            }});
        StringBuilder stringBuilder = new StringBuilder("{\"msgs\":[");
        for (int i = 0; i<arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size()-i>1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]\n}");
        return stringBuilder.toString();
    }

    @RequestMapping ("/getmsgss")
    String twitss(@RequestParam("token") String token){
        return "[{\"msg\":\""+token+"\"}, {\"msg\":\"200\"}]";
    }

    @RequestMapping ("/addmsg")
    Boolean addtwit(@RequestParam("token") String token,
                    @RequestParam("id") int id,
                    @RequestParam("msg") String twit,
                    @RequestParam("who") int who) {
        jdbcTemplate.update
                ("INSERT INTO twits (_id, token, twit, who) VALUES (?,?,?,?)",
                        id, token, twit, who);
        return true;
    }

    @RequestMapping ("/gettokens")
    String gettokens (@RequestParam("pass") String pass){
        if (pass.equals("Kolokolji9")){
            List<String> arrayList = jdbcTemplate.query("SELECT * FROM twits", new Object[]{}, new RowMapper<String>(){
                @Override
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return "{\"token\":\""+rs.getString("token")+"\"}";
                }});
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i<arrayList.size(); i++) {
                stringBuilder.append(arrayList.get(i));
                if (arrayList.size()-i>1) stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }
        return "false";

    }


}
