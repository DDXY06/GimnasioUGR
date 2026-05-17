package com.example.gimnasiougr.Controllers.Cliente;

import com.example.gimnasiougr.Controllers.LoginController;
import com.example.gimnasiougr.Models.BonoDTO;
import com.example.gimnasiougr.Models.ClienteDTO;
import com.example.gimnasiougr.Models.Usuario;
import com.example.gimnasiougr.Services.BonoService;
import com.example.gimnasiougr.Services.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteBonoController {

    private final BonoService bonoService;
    private final ClienteService clienteService;

    @GetMapping("/mis-bonos")
    public String misBonos(Model model) {
        Usuario usuario = LoginController.usuarioLogeadoGlobal;
        if (usuario == null) {
            return "redirect:/";
        }

        ClienteDTO clienteDTO = clienteService.buscarPorUsuarioId(usuario.getId()) ;
        List<BonoDTO> bonos = bonoService.buscarPorClienteId(clienteDTO.getId());

        model.addAttribute("bonos", bonos);
        model.addAttribute("nombreCliente", clienteDTO.getNombre());

        return "cliente/mis-bonos";
    }
}