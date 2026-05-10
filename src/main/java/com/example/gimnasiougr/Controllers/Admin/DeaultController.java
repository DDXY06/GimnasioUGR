package com.example.gimnasiougr.Controllers.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class DeaultController {
    @GetMapping({"", "/", "/index"})
    public String adminIndex() {
        return "admin/index";
    }
}
