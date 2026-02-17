package uasz.sn.Universite_Stage.Universite_Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.modele.Role;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.service.UtilisateurService;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Administrateur;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Enseignant;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Etudiant;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.service.AdministrateurService;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.service.EnseignantService;


import java.util.Date;

@SpringBootApplication
@EnableScheduling
public class UniversiteStageApplication implements CommandLineRunner {

	public static void main(String[] args) { SpringApplication.run(UniversiteStageApplication.class, args);
	}

	@Autowired
	private UtilisateurService utilisateurService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EnseignantService enseignantService;

	@Autowired
	private AdministrateurService administrateurService;

	@Override
	public void run(String... args) throws Exception {
		// Vérifiez et créez des rôles
		Role administrateur = utilisateurService.ajouter_Role(new Role("Administrateur"));
		Role enseignant = utilisateurService.ajouter_Role(new Role("Enseignant"));
		Role etudiant = utilisateurService.ajouter_Role(new Role("Etudiant"));



		// Vérifiez et ajoutez le premier utilisateur ayant comme role Administrateur
		if (utilisateurService.rechercher_Utilisateur("awas6220@gmail.com") == null) {
			Administrateur user_1 = new Administrateur();
			user_1.setNom("SECK");
			user_1.setPrenom("Awa");
			user_1.setUsername("awas6220@gmail.com");
			user_1.setPassword(passwordEncoder.encode("heulminesabiline"));
			user_1.setDateCreation(new Date());
			user_1.setActive(true);
			user_1.setMatricule("ID2024");
			administrateurService.ajouterAdministrateur(user_1);
			utilisateurService.ajouter_UtilisateurRoles(user_1, administrateur);
		}

		// Vérifiez et ajoutez le deuxième utilisateur ayant comme role Enseignant
		if (utilisateurService.rechercher_Utilisateur("idiop@uasz.sn") == null) {
			Enseignant user_2 = new Enseignant();
			user_2.setNom("DIOP");
			user_2.setPrenom("Ibrahima");
			user_2.setUsername("idiop@uasz.sn");
			user_2.setPassword(passwordEncoder.encode("Passer123"));
			user_2.setDateCreation(new Date());
			user_2.setActive(true);
			user_2.setSpecialite("Web Semantique");
			user_2.setMatricule("AD2024");
			user_2.setGrade("Professeur");
			enseignantService.ajouter(user_2);
			utilisateurService.ajouter_UtilisateurRoles(user_2, enseignant);
		}

		// Ajoutez un utilisateur avec le rôle de Enseignant
		if (utilisateurService.rechercher_Utilisateur("sdiagne@uasz.sn") == null) {
			Enseignant user_3 = new Enseignant();
			user_3.setNom("DIAGNE");
			user_3.setPrenom("Serigne");
			user_3.setUsername("sdiagne@uasz.sn");
			user_3.setPassword(passwordEncoder.encode("Passer123"));
			user_3.setDateCreation(new Date());
			user_3.setActive(true);
			user_3.setSpecialite("Base de données");
			user_3.setMatricule("SD2024");
			user_3.setGrade("Professeur");
			enseignantService.ajouter(user_3);
			utilisateurService.ajouter_UtilisateurRoles(user_3, enseignant); }


		// Ajout de l'étudiant (pour visionner uniquement)
		if (utilisateurService.rechercher_Utilisateur("djeumbeb@gmail.com") == null) {
			Etudiant user_4 = new Etudiant();
			user_4.setNom("BA");
			user_4.setPrenom("Djeumbe");
			user_4.setUsername("djeumbeb@gmail.com");
			user_4.setPassword("_db1911_");
			user_4.setDateCreation(new Date());
			user_4.setActive(true);
			user_4.setMatricule("DB2024");
			user_4.setAdresse("Kénia");
			user_4.setTelephone("778217892");
			utilisateurService.ajouter_Utilisateur(user_4);
			utilisateurService.ajouter_UtilisateurRoles(user_4, etudiant);
		}

		if (utilisateurService.rechercher_Utilisateur("monouveaumail10@gmail.com") == null) {
			Etudiant user_5 = new Etudiant();
			user_5.setNom("NDIAYE");
			user_5.setPrenom("Fatima");
			user_5.setUsername("monouveaumail10@gmail.com");
			user_5.setPassword("monouveaumail06");
			user_5.setDateCreation(new Date());
			user_5.setActive(true);
			user_5.setMatricule("FN2024");
			user_5.setAdresse("Kénia");
			user_5.setTelephone("772815832");
			utilisateurService.ajouter_Utilisateur(user_5);
			utilisateurService.ajouter_UtilisateurRoles(user_5, etudiant);
		}

		if (utilisateurService.rechercher_Utilisateur("mbayemamejarra1@gmail.com") == null) {
			Etudiant user_6 = new Etudiant();
			user_6.setNom("MBAYE");
			user_6.setPrenom("Mame Diarra");
			user_6.setUsername("mbayemamejarra1@gmail.com");
			user_6.setPassword("Matar1476@");
			user_6.setDateCreation(new Date());
			user_6.setActive(true);
			user_6.setMatricule("MD2024");
			user_6.setAdresse("Kénia");
			user_6.setTelephone("776542147");
			utilisateurService.ajouter_Utilisateur(user_6);
			utilisateurService.ajouter_UtilisateurRoles(user_6, etudiant);
		}


	}

}
