/*
 * Copyright 2015 Benedikt Ritter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.heroku.demo;

import com.heroku.demo.point.Point;
import com.heroku.demo.point.PointRepository;
import com.heroku.demo.point.PointServiceImpl;
import com.heroku.demo.record.Record;
import com.heroku.demo.record.RecordRepository;
import com.heroku.demo.record.RecordServiceImpl;

import javax.validation.Valid;
import javax.xml.ws.RequestWrapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HomeController {

    private RecordRepository recordRepository;
    private PointRepository pointRepository;
    PointServiceImpl pointService;

    @Autowired
    public HomeController(RecordRepository repository, PointRepository pRepository) {
        this.recordRepository = repository;
        this.pointRepository = pRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap model) {
        return "index";
    }

    @RequestMapping("/news")
    public String news(ModelMap model) {
        List<Record> records = RecordServiceImpl.getByType(recordRepository, 0);//findByType(0); //ByType(0);
        model.addAttribute("records", records);
        model.addAttribute("insertRecord", new Record());
        return "news";
    }

    @RequestMapping("/addnews")
    public String insertData(ModelMap model,
                             @ModelAttribute("insertRecord") @Valid Record record,
                             BindingResult result) {
        if (!result.hasErrors()) {
            record.setWhat(0);
            recordRepository.save(record);
        }
        return news(model);
    }

    @RequestMapping("/deletenews")
    public String deleteData(ModelMap model, @ModelAttribute("id") String id,
                             BindingResult result) {
        recordRepository.delete(Long.parseLong(id));
        return news(model);
    }

    @RequestMapping("/guides")
    public String guides(ModelMap model) {
        List<Record> records = RecordServiceImpl.getByType(recordRepository, 1);//findByType(1);
        model.addAttribute("records", records);
        model.addAttribute("insertRecord", new Record());
        return "guides";
    }

    @RequestMapping("/addguide")
    public String insertGuide(ModelMap model,
                              @ModelAttribute("insertRecord") @Valid Record record,
                              BindingResult result) {

        if (!result.hasErrors()) {
            record.setWhat(1);
            recordRepository.save(record);
        }
        return guides(model);
    }

    @RequestMapping("/deleteguide")
    public String deleteGuide(ModelMap model, @ModelAttribute("id") String id,
                              BindingResult result) {
        recordRepository.delete(Long.parseLong(id));
        //TODO: delete points
        return guides(model);
    }

    @RequestMapping("/points")
    public String points(ModelMap model) {
        List<Record> records = RecordServiceImpl.getByType(recordRepository, 1);//findByType(1);
        List<Point> points = pointRepository.findAll();
        model.addAttribute("records", records);
        model.addAttribute("points", points);
        model.addAttribute("insertPoint", new Point());
        return "points";
    }

    @RequestMapping("/addpoint")
    public String insertPoint(ModelMap model,
                              @ModelAttribute("insertPoint") @Valid Point point,
                              BindingResult result) {
        if (!result.hasErrors()) {
            pointRepository.save(point);
        }
        return points(model);
    }

    @RequestMapping("/deletepoint")
    public String deletePoint(ModelMap model, @ModelAttribute("id") String id,
                              BindingResult result) {
        pointRepository.delete(Long.parseLong(id));
        return points(model);
    }

    @RequestMapping("/contacts")
    public String contacts(ModelMap model) {
        List<Record> records = RecordServiceImpl.getByType(recordRepository, 2);//findByType(1);
        model.addAttribute("records", records);
        model.addAttribute("insertRecord", new Record());
        return "contacts";
    }

    @RequestMapping("/addcontact")
    public String insertContact(ModelMap model,
                              @ModelAttribute("insertRecord") @Valid Record record,
                              BindingResult result) {

        if (!result.hasErrors()) {
            record.setWhat(2);
            recordRepository.save(record);
        }
        return contacts(model);
    }

    @RequestMapping("/deletecontact")
    public String deleteContact(ModelMap model, @ModelAttribute("id") String id,
                              BindingResult result) {
        recordRepository.delete(Long.parseLong(id));
        return contacts(model);
    }


    @RequestMapping("/gallery")
    public String gallery(ModelMap model) {
        List<Record> records = RecordServiceImpl.getByType(recordRepository, 3);//findByType(1);
        model.addAttribute("records", records);
        model.addAttribute("insertRecord", new Record());
        return "gallery";
    }

    @RequestMapping("/addgallery")
    public String insertGallery(ModelMap model,
                                @ModelAttribute("insertRecord") @Valid Record record,
                                BindingResult result) {

        if (!result.hasErrors()) {
            record.setWhat(3);
            recordRepository.save(record);
        }
        return gallery(model);
    }

    @RequestMapping("/deletegallery")
    public String deleteGallery(ModelMap model, @ModelAttribute("id") String id,
                                BindingResult result) {
        recordRepository.delete(Long.parseLong(id));
        //TODO: delete photos
        return gallery(model);
    }

    @RequestMapping("/photos")
    public String photos(ModelMap model) {
        List<Record> records = RecordServiceImpl.getByType(recordRepository, 3);//findByType(1);
        List<Record> photos = RecordServiceImpl.getByType(recordRepository, 4);
        model.addAttribute("records", records);
        model.addAttribute("photos", photos);
        model.addAttribute("insertPhoto", new Record());
        return "photos";
    }

    @RequestMapping("/addphoto")
    public String insertPhotos(ModelMap model,
                                @ModelAttribute("insertPhoto") @Valid Record record,
                                BindingResult result) {

        if (!result.hasErrors()) {
            record.setWhat(4);
            recordRepository.save(record);
        }
        return photos(model);
    }

    @RequestMapping("/deletephoto")
    public String deletePhotos(ModelMap model, @ModelAttribute("id") String id,
                                BindingResult result) {
        recordRepository.delete(Long.parseLong(id));
        return photos(model);
    }

    @RequestMapping("/getjson")
    public String getPoints(ModelMap model, @ModelAttribute("type") int type,
                            BindingResult result){
        ArrayList<String> arrayList = new ArrayList<>();
        switch (type){
            case 5:
                List<Point> points = pointRepository.findAll();
                for (Point p:points){
                    arrayList.add(p.toString());
                }
                break;
            default:
                List<Record> records = RecordServiceImpl.getByType(recordRepository, type);
                for (Record r:records){
                    arrayList.add(r.toString());
                }
        }

        StringBuilder stringBuilder = new StringBuilder("[");
        for (int i = 0; i<arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size()-i>1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]");
        model.addAttribute("json", stringBuilder);
        return "json"
    }

}
