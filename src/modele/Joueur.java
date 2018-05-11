package modele;

import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class Joueur {

	// Les noms ont été choisis pour être courts et de sexe neutre.
	static LinkedList<String> noms = new LinkedList<>(
			Arrays.asList("Hall", "Brett", "Alex", "Fynn", "Jude", "Dave"));
	
	private int x;
	private int y;
	String nom;
	private Modele modele;

	Artefact cle;

	public Joueur(Modele modele, int x, int y) throws NoSuchElementException{
		this.x = x;
		this.y = y;
		this.modele = modele;

		// Prendre un nom au hasard
		Collections.shuffle(Joueur.noms);
		try {
			this.nom = Joueur.noms.pop();
		} catch (NoSuchElementException n) {
			throw new NoSuchElementException("Plus de noms disponibles !");
		}
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
	
	public String toString() {
		return this.nom;
	}

	public boolean assecher(int i, int j) {
		if (Math.abs(i - x) < 2 && Math.abs(j - y) < 2) {
			this.modele.cellules[i][j].assecher();
			return true;
		} else {
			return false;
		}
	}

	public boolean ExisteJoueur(int x, int y) {
		if (this.x == x && this.y == y) {
			return true;
		} else
			return false;
	}

	boolean faitAction(String instruction) {
		Cellule[][] cellules = this.modele.cellules;

		if (instruction.equals("dg") && this.x - 1 >= 0) {
			cellules[this.x][this.y].retirerJoueur(this);
			cellules[this.x - 1][this.y].ajouterJoueur(this);
			this.x = this.x - 1;
			System.out.println("tu es allé(e) à gauche");
		} else if (instruction.equals("dd") && this.x + 1 < Modele.LARGEUR) {
			cellules[this.x][this.y].retirerJoueur(this);
			cellules[this.x + 1][this.y].ajouterJoueur(this);
			this.x = this.x + 1;
			System.out.println("tu es allé(e) à droite");
		} else if (instruction.equals("dh") && this.y - 1 >= 0) {
			cellules[this.x][this.y].retirerJoueur(this);
			cellules[this.x][this.y - 1].ajouterJoueur(this);
			this.y = this.y - 1;
			System.out.println("tu es allé(e) en haut");
		} else if (instruction.equals("db") && this.y + 1 < Modele.HAUTEUR) {
			cellules[this.x][this.y].retirerJoueur(this);
			cellules[this.x][this.y + 1].ajouterJoueur(this);
			this.y = this.y + 1;
			System.out.println("tu es allé(e) en bas");
		}

		else if (instruction.equals("ag") && this.x - 1 >= 0) {
			this.assecher(this.x - 1, this.y);
			System.out.println("tu as asséché à gauche");
		} else if (instruction.equals("ad") && this.x + 1 < Modele.LARGEUR) {
			this.assecher(this.x + 1, this.y);
			System.out.println("tu as asséché à droite");
		} else if (instruction.equals("ah") && this.y - 1 >= 0) {
			this.assecher(this.x, this.y - 1);
			System.out.println("tu as asséché en haut");
		} else if (instruction.equals("ab") && this.y + 1 < Modele.HAUTEUR) {
			this.assecher(this.x, this.y + 1);
			System.out.println("tu as asséché en bas");
		}
		
		else if (instruction.equals("rg") && this.x - 1 >= 0) {
			if (this.recupereArtefact(this.x - 1, this.y)== null) {
				System.out.println("Ta clé n'est pas compatible avec cette artefact.");
			} else {
				System.out.println("Bravo ! On va y arriver !");
			}
		} else if (instruction.equals("rd") && this.x + 1 < Modele.LARGEUR) {
			if (this.recupereArtefact(this.x + 1, this.y)== null) {
				System.out.println("Ta clé n'est pas compatible avec cette artefact.");
			} else {
				System.out.println("Bravo ! On va y arriver !");
			}
		} else if (instruction.equals("rh") && this.y - 1 >= 0) {
			if (this.recupereArtefact(this.x, this.y-1)== null) {
				System.out.println("Ta clé n'est pas compatible avec cette artefact.");
			} else {
				System.out.println("Bravo ! On va y arriver !");
			}
		} else if (instruction.equals("rb") && this.y + 1 < Modele.HAUTEUR) {
			if (this.recupereArtefact(this.x, this.y+1)== null) {
				System.out.println("Ta clé n'est pas compatible avec cette artefact.");
			} else {
				System.out.println("Bravo ! On va y arriver !");
			}
		}

		else {
			System.err.println("instructions incorrectes");
			return false;
		}
		
		this.modele.cellules = cellules;
		
		return true;
	}

	public void demandeAction() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("Choisis une action, "+this.nom);
		if (this.cle!=null) {
			System.out.print("; tu possèdes la clé : "+this.cle);
		}
		System.out.println();
		
		try {
			String action,direction;
			do {
				System.out.println("Sélectionner action : (d)éplacement, (a)ssèchement, (r)écupérer artefact, (q)ue dalle");
				action = br.readLine();
				if (action.equals("q")) {
					System.err.println("Patience et longueur de temps valent mieux que force ni que rage");
					return;
				}
				System.out.println("déplacements : (g)auche, (d)roite, (b)as, (h)aut :");
				direction = br.readLine();
			} while (!this.faitAction(action+direction));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	void recevoirCle(Artefact cle) {
		this.cle = cle;
	}
	
	// Cette fonction permet de récupérer un artefact depuis des coordonnées
	Artefact recupereArtefact(int i, int j) {
		Cellule c = this.modele.getCellule(i, j);
		
		if (Math.abs(i - x) < 2 && Math.abs(j - y) < 2 && this.cle == c.artefact) {
			Artefact returned = c.artefact;
			this.cle = null;
			c.artefact = null;
			return returned;
		}
		
		return null;
	}
}
