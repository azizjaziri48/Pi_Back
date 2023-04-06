package com.example.pi_back.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ImgUploadController {
    @GetMapping("/uploadimage") public String displayUploadForm(Model model) {
        return "photo";
    }
}
