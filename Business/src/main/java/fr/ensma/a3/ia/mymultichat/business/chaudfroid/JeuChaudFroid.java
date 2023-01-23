package fr.ensma.a3.ia.mymultichat.business.chaudfroid;

import java.util.ArrayList;
import java.util.List;

public class JeuChaudFroid {
	
	private int nombre;
	private String idJoueurCourant; //id du joueur auquel c'est le tour de jouer
	private List<String> idJoueurs; // liste de tous les joueurs
	
	
	public JeuChaudFroid(int borne_max, String id) {
		nombre  = (int) (Math.random()*borne_max); //nombre entier aléatoire compris entre 0 et borne_max
		idJoueurs = new ArrayList<>();
		idJoueurs.add(id);
		idJoueurCourant = idJoueurs.get(0);
	}
	
	public String proposeNombre(int n) {
		
		
		suivant();
		
		if (n > nombre) {
			return "trop grand";
		}
		else if (n < nombre) {
			return "trop petit";
		}
		else {
			return "bravo !";
		}
		
		
	}
	
	
	private void suivant() {

		if (idJoueurs.indexOf(idJoueurCourant) +1 < idJoueurs.size() ) {
			idJoueurCourant = idJoueurs.get(idJoueurs.indexOf(idJoueurCourant) + 1 ); //si le joueur n'est pas le dernier de la liste on augmente l'index de 1
		}
		else { //sinon on revient a 0
			idJoueurCourant = idJoueurs.get(0);
		}
	}
	
	public void addJoueur(String id) {
		idJoueurs.add(id);
	}
	

	public String getIdJoueurCourant() {
		return idJoueurCourant;
	}

}
