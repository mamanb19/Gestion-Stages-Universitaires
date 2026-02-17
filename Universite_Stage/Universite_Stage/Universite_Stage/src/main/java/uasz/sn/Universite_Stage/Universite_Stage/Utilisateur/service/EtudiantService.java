package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.modele.Role;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Etudiant;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.repository.EtudiantRepository;

import java.util.List;

@Service
@Transactional
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    public Etudiant ajouter(Etudiant etudiant) { return etudiantRepository.save(etudiant);}

    public List<Etudiant> lister() { return etudiantRepository.findAll();}

    public Etudiant rechercher(Long id) { return etudiantRepository.findById(id).get();}

    public Etudiant modifier(Etudiant etudiant) { return etudiantRepository.save(etudiant);}

    public void supprimer(Long id) { etudiantRepository.deleteById(id);}


    public  void activer(Long id) {
        Etudiant etudiant=etudiantRepository.findById(id).get();
        if (etudiant.isActive()){ etudiant.setActive(false);}
        else { etudiant.setActive(true);}
        etudiantRepository.save(etudiant);
    }

    public void archiver(Long id){
        Etudiant etudiant=etudiantRepository.findById(id).get();
        if (etudiant.isArchive()){
            etudiant.setArchive(false);}
        else { etudiant.setArchive(true);}
        etudiantRepository.save(etudiant);
    }

    public Etudiant trouverParUsername(String username) {
        return etudiantRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
    }

    public long getNombreEtudiants() {
        return etudiantRepository.count();
    }

}
