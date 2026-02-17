package uasz.sn.Universite_Stage.Universite_Stage.Authentification.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.modele.Utilisateur;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.service.UtilisateurService;

import java.security.Principal;

@Controller
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @RequestMapping(value = "/login")
    public String index() {
        return "login";
    }

    @RequestMapping("/")
    public String login(Principal principal) {
        String url = "login";

        if (principal == null) {
            return url; // Redirection vers login si non connecté
        }

        Utilisateur utilisateur = utilisateurService.rechercher_Utilisateur(principal.getName());

        if (utilisateur != null && utilisateur.getRoles() != null && !utilisateur.getRoles().isEmpty()) {
            String role = utilisateur.getRoles().get(0).getRole();

            if ("Administrateur".equals(role)) {
                url = "redirect:/Administrateur/Accueil";
            } else if ("Enseignant".equals(role)) {
                url = "redirect:/Enseignant/Accueil";
            } else if ("Etudiant".equals(role)) {
                url = "redirect:/Etudiant/Accueil";
            }
        }

        return url;
    }

    @RequestMapping(value = "/logout")
    public String LogOutAndRedirectToLoginPage(Authentication authentication,
                                               HttpServletRequest request,
                                               HttpServletResponse response) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout=true";
    }

    @RequestMapping(value = "/profil", method = RequestMethod.GET)
    public String profil_Utilisateur(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Redirige si non connecté
        }

        Utilisateur utilisateur = utilisateurService.rechercher_Utilisateur(principal.getName());

        if (utilisateur != null) {
            model.addAttribute("utilisateur", utilisateur);
            model.addAttribute("nom", utilisateur.getNom());
            model.addAttribute("prenom", utilisateur.getPrenom().charAt(0));
        }

        return "profil";
    }
}
