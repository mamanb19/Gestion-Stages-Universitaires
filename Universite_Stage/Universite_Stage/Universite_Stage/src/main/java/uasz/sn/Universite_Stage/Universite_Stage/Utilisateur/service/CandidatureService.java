package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Candidature;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Etudiant;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Stage;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.StatutCandidature;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.repository.CandidatureRepository;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.repository.EtudiantRepository;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.repository.StageRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class CandidatureService {

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private EmailService emailService;

    // Ajouter une candidature
    public void ajouterCandidature(Long etudiantId, Long stageId, MultipartFile lettreMotivation, MultipartFile cv) throws IOException {
        // Validation des fichiers PDF
        if (!lettreMotivation.getContentType().equals("application/pdf")) {
            throw new RuntimeException("Le fichier de lettre de motivation doit être un PDF.");
        }
        if (!cv.getContentType().equals("application/pdf")) {
            throw new RuntimeException("Le fichier de CV doit être un PDF.");
        }

        // Enregistrer les fichiers PDF
        String lettreMotivationPath = fileStorageService.storeFile(lettreMotivation);
        String cvPath = fileStorageService.storeFile(cv);

        // Récupérer l'étudiant et le stage à partir de leurs IDs
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé avec l'ID : " + etudiantId));
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new RuntimeException("Stage non trouvé avec l'ID : " + stageId));

        // Créer la candidature
        Candidature candidature = new Candidature();
        candidature.setEtudiant(etudiant); // Utilisation de l'objet Etudiant récupéré
        candidature.setOffreStage(stage); // Utilisation de l'objet Stage récupéré
        candidature.setDateCandidature(LocalDate.now());
        candidature.setStatut(StatutCandidature.EN_ATTENTE);
        candidature.setLettreMotivationPath(lettreMotivationPath);
        candidature.setCvPath(cvPath);

        // Sauvegarder la candidature
        candidatureRepository.save(candidature);
    }

    // Modifier une candidature
    public void modifierCandidature(Long id, Long etudiantId, Long stageId, MultipartFile lettreMotivation, MultipartFile cv, StatutCandidature statut) throws IOException {
        // Récupérer la candidature existante
        Candidature candidature = candidatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée avec l'ID : " + id));

        // Récupérer l'étudiant et le stage à partir de leurs IDs
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé avec l'ID : " + etudiantId));
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new RuntimeException("Stage non trouvé avec l'ID : " + stageId));

        // Mettre à jour les fichiers PDF si fournis
        if (lettreMotivation != null && !lettreMotivation.isEmpty()) {
            if (!lettreMotivation.getContentType().equals("application/pdf")) {
                throw new RuntimeException("Le fichier de lettre de motivation doit être un PDF.");
            }
            String lettreMotivationPath = fileStorageService.storeFile(lettreMotivation);
            candidature.setLettreMotivationPath(lettreMotivationPath);
        }

        if (cv != null && !cv.isEmpty()) {
            if (!cv.getContentType().equals("application/pdf")) {
                throw new RuntimeException("Le fichier de CV doit être un PDF.");
            }
            String cvPath = fileStorageService.storeFile(cv);
            candidature.setCvPath(cvPath);
        }

        // Mettre à jour les autres champs
        candidature.setEtudiant(etudiant);
        candidature.setOffreStage(stage);
        candidature.setStatut(statut);

        // Sauvegarder les modifications
        candidatureRepository.save(candidature);
    }

    // Récupérer toutes les candidatures
    public List<Candidature> listerCandidatures() {
        return candidatureRepository.findAll();
    }

    // Récupérer une candidature par son ID
    public Candidature trouverCandidatureParId(Long id) {
        return candidatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée avec l'ID : " + id));
    }

    // Supprimer une candidature
    public void supprimerCandidature(Long id) {
        if (!candidatureRepository.existsById(id)) {
            throw new RuntimeException("Candidature non trouvée avec l'ID : " + id);
        }
        candidatureRepository.deleteById(id);
    }

    public List<Candidature> listerCandidaturesParEtudiantEtStatut(Long etudiantId, StatutCandidature statut) {
        return candidatureRepository.findByEtudiantIdAndStatut(etudiantId, statut);
    }

    // Récupérer les candidatures par statut
    public List<Candidature> listerCandidaturesParStatut(StatutCandidature statut) {
        return candidatureRepository.findByStatut(statut);
    }

    public long getNombreCandidatures() {
        return candidatureRepository.count();
    }
    public void changerStatutCandidature(Long id, StatutCandidature statut) {
        Candidature candidature = candidatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée"));

        candidature.setStatut(statut);
        candidatureRepository.save(candidature);
    }


}