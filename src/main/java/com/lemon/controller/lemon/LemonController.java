package com.lemon.controller.lemon;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by simpletour_Jenkin on 2016/8/10.
 */
@Controller
public class LemonController {

    @RequestMapping(value = "/lemon/lemons")
    public String myLemons(){

        return "";
    }
}


