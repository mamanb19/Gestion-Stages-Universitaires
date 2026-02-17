package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.modele.Role;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Enseignant;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.repository.EnseignantRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EnseignantService {

    @Autowired
    private EnseignantRepository enseignantRepository;

    public Enseignant ajouter(Enseignant enseignant) { return enseignantRepository.save(enseignant);}

    public List<Enseignant> lister() { return enseignantRepository.findAll();}

    public Enseignant rechercher(Long id) { return enseignantRepository.findById(id).get();}

    public Enseignant modifier(Enseignant enseignant) { return enseignantRepository.save(enseignant);}

    public void supprimer(Long id) { enseignantRepository.deleteById(id);}



    public void archiver(Long id){
        Enseignant enseignant=enseignantRepository.findById(id).get();
        if (enseignant.isArchive()==true){
            enseignant.setArchive(false);}
        else { enseignant.setArchive(true);}
        enseignantRepository.save(enseignant);
    }


    public Optional<Enseignant> getEnseignantById(Long id) {
        return enseignantRepository.findById(id);  // Returns Optional<Enseignant>
    }

}
