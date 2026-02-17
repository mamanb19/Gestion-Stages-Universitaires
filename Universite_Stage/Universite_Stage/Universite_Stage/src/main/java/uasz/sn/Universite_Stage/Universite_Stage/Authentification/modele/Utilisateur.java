package uasz.sn.Universite_Stage.Universite_Stage.Authentification.modele;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @NotNull
    private String password;

    private String nom;

    private String prenom;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    private boolean active;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>() ;
}
