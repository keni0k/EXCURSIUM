package com.heroku.demo.support;

import com.heroku.demo.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/support")
public class SupportController {

    private static final String AUTH_KEY = "";

    private SupportServiceImpl supportService;

    @Autowired
    public SupportController(SupportRepository supportRepository){
        supportService = new SupportServiceImpl(supportRepository);
    }

    @RequestMapping(value = "/listjson", method = RequestMethod.POST)
    public String listJson(@RequestParam("auth") String authKey) {
        List<Support> supportList = supportService.getAll();
        return Utils.list("support", supportList, authKey, AUTH_KEY);
    }

}
