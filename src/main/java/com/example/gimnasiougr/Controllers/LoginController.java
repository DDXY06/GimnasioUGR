package com.example.gimnasiougr.Controllers;

import com.example.gimnasiougr.Models.TipoUsuario;
import com.example.gimnasiougr.Models.Usuario;
import com.example.gimnasiougr.Repositories.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("correo") String correo,
            @RequestParam("contrasenia") String contrasenia,
            HttpSession session,
            Model model) {

        Optional<Usuario> resultado = usuarioRepository.findByCorreoAndContrasenia(correo, contrasenia);

        if (resultado.isEmpty()) {
            model.addAttribute("loginError", "Correo o contraseña incorrectos.");
            return "index";
        }

        Usuario usuario = resultado.get();
        session.setAttribute("usuarioLogeado", usuario);

        if (usuario.getRol() == TipoUsuario.ADMINISTRADOR) {
            return "redirect:/admin/index";
        } else if (usuario.getRol() == TipoUsuario.ENTRENADOR) {
            return "redirect:/entrenador/index";
        } else {
            return "redirect:/cliente/index";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}