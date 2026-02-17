package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Notification;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.service.CandidatureService;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.service.EtudiantService;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.service.NotificationService;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.service.StageService;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private StageService stageService;

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private CandidatureService candidatureService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/Administrateur/dashboard")
    public String afficherDashboard(Model model) {
        List<Notification> notifications = notificationService.getNotificationsNonLues();
        model.addAttribute("notifications", notifications);
        model.addAttribute("nombreStages", stageService.getNombreStages());
        model.addAttribute("nombreEtudiants", etudiantService.getNombreEtudiants());
        model.addAttribute("nombreCandidatures", candidatureService.getNombreCandidatures());
        return "dashboard";
    }
    @GetMapping("/notifications/marquer-comme-lue")
    public String marquerNotificationCommeLue(@RequestParam Long id) {
        notificationService.marquerCommeLue(id);
        return "redirect:/Administrateur/dashboard"; // Rediriger vers le tableau de bord
    }
}