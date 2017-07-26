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

    @RequestMapping("/addpointt")
    public String insertPoint(ModelMap model) {
        pointRepository.save(new Point("p1","p2","3","Data"));
        return points(model);
    }

    @RequestMapping("/addpoint")
    public String insertPoint(ModelMap model,
                              @ModelAttribute("insertPoint") @Valid Point point,
                              BindingResult result) {
        pointRepository.save(point);
        return points(model);
    }

    @RequestMapping("/deletepoint")
    public String deletePoint(ModelMap model, @ModelAttribute("id") String id,
                              BindingResult result) {
        pointRepository.delete(Long.parseLong(id));
        return points(model);
    }


}
