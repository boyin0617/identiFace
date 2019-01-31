package com.example;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SampleController {

    @RequestMapping("/")
    @ResponseBody
    public ModelAndView home() {

       ModelAndView model = new ModelAndView("main");
       return model;

    }
}
