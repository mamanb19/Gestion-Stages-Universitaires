package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.service;


import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Stage;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.StatutOffre;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.repository.StageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class StageService {

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private EmailService emailService;

    public List<Stage> getAllStages() {
        return stageRepository.findAll();
    }

    public Stage getStageById(Long id) {
        return stageRepository.findById(id).orElse(null);
    }

    public void ajouterStage(Stage stage) {
        stageRepository.save(stage);
    }

    // Modifier un stage
    public Stage modifierStage(Long id,Stage stage) {
        // Vérifie si le stage existe déjà dans la base de données
        return stageRepository.findById(id).map(stageExistant -> {
            // Met à jour les champs du stage existant
            stageExistant.setTitre(stage.getTitre());
            stageExistant.setDescription(stage.getDescription());
            stageExistant.setEntreprise(stage.getEntreprise());
            stageExistant.setDuree(stage.getDuree());
            stageExistant.setCompetencesRequises(stage.getCompetencesRequises());
            stageExistant.setDateLimite(stage.getDateLimite());
            stageExistant.setLieu(stage.getLieu());
            // Sauvegarde les modifications
            return stageRepository.save(stageExistant);
        }).orElseThrow(() -> new RuntimeException("Stage introuvable avec l'ID: " + id));
    }

    public void supprimerStage(Long id) {
        stageRepository.deleteById(id);
    }


    // Méthode pour vérifier les stages expirés
    @Scheduled(cron = "0 0 8 * * ?") // Exécution quotidienne à 8h00
    public void verifierStagesExpires() {
        LocalDate aujourdhui = LocalDate.now();
        List<Stage> stagesExpires = stageRepository.findByDateLimiteBefore(aujourdhui);

        for (Stage stage : stagesExpires) {
            String sujet = "Alerte : Stage expiré - " + stage.getTitre();
            String texte = "Le stage '" + stage.getTitre() + "' a expiré le " + stage.getDateLimite() + ".";
            emailService.sendEmail("awas6220@gmail.com", sujet, texte); // Remplacez par l'e-mail de l'admin
        }
    }

    public long getNombreStages() {
        return stageRepository.count();
    }
    public List<Stage> trouverOffresDisponibles() {
        return stageRepository.findByStatut(StatutOffre.DISPONIBLE);
    }
    public List<Stage> trouverOffresArchivees() {
        return stageRepository.findByStatut(StatutOffre.ARCHIVEE);
    }

    public List<Stage> trouverOffresValidees() {
        return stageRepository.findByStatut(StatutOffre.VALIDEE);
    }
    // Archiver une formation
    public Stage archiveStage(Long id) {
        return stageRepository.findById(id).map(stage -> {
            stage.setArchive(true);
            return stageRepository.save(stage);
        }).orElseThrow(() -> new RuntimeException("Stage introuvable avec l'ID: " + id));
    }
}
