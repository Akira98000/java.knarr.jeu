package miage.knarr.equipeC.model.setup;

import java.util.ArrayList;
import java.util.List;

import miage.knarr.equipeC.model.Jeu;
import miage.knarr.equipeC.model.Joueur;
import miage.knarr.equipeC.model.MoteurJeu;
import miage.knarr.equipeC.model.bot.BOTA;
import miage.knarr.equipeC.model.bot.BOTB;
import miage.knarr.equipeC.model.bot.BOTC;
import miage.knarr.equipeC.model.bot.BOTD;
import miage.knarr.equipeC.model.enums.CouleurBateau;
import miage.knarr.equipeC.model.enums.Difficulte;

public class Setup {

    public Setup(String jsonFilePath) {
    }

    public MoteurJeu initialiserJeu(int nombreJoueurs, Difficulte difficulte, List<String> botTypes) {
        if (nombreJoueurs > CouleurBateau.values().length) {
            throw new IllegalArgumentException("Nombre de joueurs trop élevé. Maximum possible: " + CouleurBateau.values().length);
        }
        if (botTypes.size() != nombreJoueurs) {
            throw new IllegalArgumentException("Le nombre de types de bots doit correspondre au nombre de joueurs.");
        }

        List<Joueur> joueurs = getJoueurs(nombreJoueurs, botTypes);

        Jeu jeu = new Jeu(joueurs, difficulte);
        MoteurJeu moteurJeu = new MoteurJeu(jeu);
        moteurJeu.lancerJeu();

        for (Joueur joueur : joueurs) {
            joueur.setJeu(jeu);
        }

        return moteurJeu;
    }

    private static List<Joueur> getJoueurs(int nombreJoueurs, List<String> botTypes) {
        List<Joueur> joueurs = new ArrayList<>();

        for (int i = 0; i < nombreJoueurs; i++) {
            CouleurBateau couleur = CouleurBateau.values()[i];
            String nom = "Joueur " + couleur;
            Joueur joueur = switch (botTypes.get(i)) {
                case "BOTA" -> new BOTA(botTypes.get(i), nom, couleur, 1, 1, 0, 0);
                case "BOTB" -> new BOTB(botTypes.get(i), nom, couleur, 1, 1, 0, 0);
                case "BOTC" -> new BOTC(botTypes.get(i), nom, couleur, 1, 1, 0, 0);
                case "BOTD" -> new BOTD(botTypes.get(i), nom, couleur, 1, 1, 0, 0);
                default -> throw new IllegalArgumentException("Type de bot inconnu: " + botTypes.get(i));
            };
            joueurs.add(joueur);
        }
        return joueurs;
    }
}