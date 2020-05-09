package com.bridgelaz.bridgelabzlms.contoller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BridgeLabz_LMS_Controller {
    @RequestMapping({"/hello", "", "/"})
    public String displayHomePage() {
        return "Welcome to Bridgelabz LMS API development project.";
    }
}
