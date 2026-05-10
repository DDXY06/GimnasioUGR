package com.example.gimnasiougr.Controllers.Admin;

import com.example.gimnasiougr.Models.BonoDTO;
import com.example.gimnasiougr.Services.BonoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminBonoController {
    private final BonoService bonoService;

    @GetMapping("/bonos")
    public String listarBonos(Model model) {
        List<BonoDTO> listaBonos = bonoService.listarTodos();
        model.addAttribute("listaBonos", listaBonos);
        return "admin/bonos";
    }
}
