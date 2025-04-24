package miage.knarr.equipeC.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import miage.knarr.equipeC.model.enums.CouleurBateau;
import miage.knarr.equipeC.model.enums.TypeAction;
import miage.knarr.equipeC.model.enums.VikingCouleur;

@SuppressWarnings("unused")
public abstract class Joueur implements Clonable<Joueur> {
    protected  final String nom;
    protected final CouleurBateau couleurBateau;
    protected int pionsRecrue;
    protected int pionsBracelet;
    protected int pointVictoire;
    protected int pointRenomme;
    protected HashMap<VikingCouleur, ArrayList<CarteViking>> zoneEquipage;
    protected List<CarteViking> cartesVikingMain;
    protected List<CarteDestination> cartesDestination;
    protected boolean aCommerceCeTour; 
    protected Jeu jeu; 
    protected boolean actionPrincipaleEffectuee;
    protected final String type;
    private boolean isClone;


    public Joueur(String type, String nom, CouleurBateau couleurBateau, int pionsRecrue, int pionsBracelet, int pointVictoire, int pointRenomme) {        this.nom = nom;
        this.couleurBateau = couleurBateau;
        this.type = type;
        this.pionsRecrue = 1;
        this.pionsBracelet = 1;
        this.pointVictoire = pointVictoire;
        this.pointRenomme = pointRenomme;
        this.actionPrincipaleEffectuee = false;
        this.aCommerceCeTour = false;
        this.zoneEquipage = new HashMap<>();
        for (VikingCouleur couleur : VikingCouleur.values()) {
            if (couleur != VikingCouleur.COULEURANY && couleur != VikingCouleur.COULEURDIFF) {
                zoneEquipage.put(couleur, new ArrayList<>());
            }
        }
        this.cartesVikingMain = new ArrayList<>();
        this.cartesDestination = new ArrayList<>();
        this.isClone = false;         
    }

    @Override
    public Joueur clone() {
        try {
            Joueur cloned = (Joueur) super.clone();
            cloned.zoneEquipage = new HashMap<>();
            for (Map.Entry<VikingCouleur, ArrayList<CarteViking>> entry : this.zoneEquipage.entrySet()) {
                ArrayList<CarteViking> clonedList = new ArrayList<>();
                for (CarteViking carte : entry.getValue()) {
                    clonedList.add(carte.clone());
                }
                cloned.zoneEquipage.put(entry.getKey(), clonedList);
            }
            cloned.cartesVikingMain = new ArrayList<>();
            for (CarteViking carte : this.cartesVikingMain) {
                cloned.cartesVikingMain.add(carte.clone());
            }
            cloned.cartesDestination = new ArrayList<>();
            for (CarteDestination carte : this.cartesDestination) {
                cloned.cartesDestination.add(carte.clone());
            }
            cloned.isClone = true;
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    public Jeu getJeu() {
        return this.jeu;
    }

    public String getNom() {
        return nom;
    }

    public CouleurBateau getCouleurBateau() {
        return couleurBateau;
    }

    public void reinitialiserPoints() {
        this.pointVictoire = 0;
        this.pointRenomme = 0;
    }

    public int getPionsRecrue() {
        return pionsRecrue;
    }

    public int getPionsBracelet() {
        return pionsBracelet;
    }

    public int getPointVictoire() {
        return pointVictoire;
    }

    public int getPointRenomme() {
        return pointRenomme;
    }

    public HashMap<VikingCouleur, ArrayList<CarteViking>> getZoneEquipage() {
        return zoneEquipage;
    }

    public List<CarteViking> getCartesVikingMain() {
        return cartesVikingMain;
    }

    public List<CarteDestination> getCartesDestination() {
        return cartesDestination;
    }

    public boolean getaCommerceCeTour() {
        return aCommerceCeTour;
    }

    public void debuterTour() {
        this.aCommerceCeTour = false;
        this.actionPrincipaleEffectuee = false; 
    }
    
    public boolean peutCommercer() {
        return this.pionsBracelet > 0 && !this.aCommerceCeTour;
    }

    public void ajouterPointVictoire(int points) {
        if (points > 0) {
            this.pointVictoire += points;
            if (jeu != null) {
                jeu.mettreAJourScores(this, this.pointVictoire, this.pointRenomme);
            }
        }
    }

    public void ajouterPointRenomme(int points) {
        if (points > 0) {
            this.pointRenomme += points;
            if (jeu != null) {
                jeu.mettreAJourScores(this, this.pointVictoire, this.pointRenomme);
            }
        }
    }

    public void ajouterPionsRecrue(int nombre) {
        if (nombre > 0) {
            this.pionsRecrue = Math.min(this.pionsRecrue + nombre, 3);
        }
    }
    
    public void retirerPionsRecrue(int nombre) {
        if (nombre > 0) {
            this.pionsRecrue = Math.max(this.pionsRecrue - nombre, 0);
        }
    }
    
    public void ajouterPionsBracelet(int nombre) {
        if (nombre > 0) {
            this.pionsBracelet = Math.min(this.pionsBracelet + nombre, 3);
        }
    }
    
    public void retirerPionsBracelet(int nombre) {
        if (nombre > 0) {
            this.pionsBracelet = Math.max(this.pionsBracelet - nombre, 0);
        }
    }
    

    public void ajouterCarteVikingMain(CarteViking carte) {
        if (carte != null) {
            this.cartesVikingMain.add(carte);
        }
    }

    public void retirerCarteVikingMain(CarteViking carte) {
        this.cartesVikingMain.remove(carte);
    }

    public void ajouterCarteVikingEquipage(CarteViking carte) {
        if (carte != null) {
            VikingCouleur couleur = carte.getCouleur();
            zoneEquipage.get(couleur).add(carte);
        }
    }

    public void ajouterCarteDestination(CarteDestination carte) {
        if (carte != null) {
            this.cartesDestination.add(carte);
        }
    }

    public void appliquerGain(Gain gain) {
        appliquerGain(gain, gain.getValeur());
    }

    

    public void appliquerGain(Gain gain, int valeur) {
        switch (gain.getType()) {
            case POINT:
                ajouterPointVictoire(valeur);
                if (isClone) {
                    System.out.println("[CLONE] " + nom + " gagne " + valeur + " points de victoire.");
                } else {
                    System.out.println(nom + " gagne " + valeur + " points de victoire.");
                }
                break;
            case RENOMME:
                ajouterPointRenomme(valeur);
                if (isClone){
                    System.out.println("[CLONE] " + nom + " gagne " + valeur + " points de renommée.");
                } else{
                    System.out.println(nom + " gagne " + valeur + " points de renommée.");
                }
                break;
            case RECRUE:
                ajouterPionsRecrue(valeur);
                if (isClone){
                    System.out.println("[CLONE] " + nom + " gagne " + valeur + " pions recrue.");
                } else{
                    System.out.println(nom + " gagne " + valeur + " pions recrue.");
                }
                break;
            case BRACELET:
                ajouterPionsBracelet(valeur);
                if (isClone){
                    System.out.println("[CLONE] " + nom + " gagne " + valeur + " pions bracelet.");
                } else{
                    System.out.println(nom + " gagne " + valeur + " pions bracelet.");
                }
                break;
            case CARTE_VIKING:
                if (isClone){
                    System.out.println("[CLONE] " + nom + " pioche " + valeur + " cartes Viking.");
                } else{
                    System.out.println(nom + " pioche " + valeur + " cartes Viking.");
                }
                for (int i = 0; i < valeur; i++) {
                    CarteViking carte = jeu.getVikingDeck().tirer();
                    if (carte != null) {
                        ajouterCarteVikingMain(carte);
                        if (isClone){
                            System.out.println("[CLONE] " + nom + " pioche la carte Viking: " + carte);
                        } else{
                            System.out.println(nom + " pioche la carte Viking: " + carte);
                        }
                    } else {
                        jeu.gererPiocheViking();
                        carte = jeu.getVikingDeck().tirer();
                        if (carte != null) {
                            ajouterCarteVikingMain(carte);
                            if (isClone){
                                System.out.println("[CLONE] " + nom + " pioche la carte Viking: " + carte);
                            } else {
                                System.out.println(nom + " pioche la carte Viking: " + carte);
                            }
                        }
                    }
                }
                break;
            default:
                System.out.println("Type de gain inconnu : " + gain.getType());
                break;
        }
    }

    public String getType() {
        return type;
    }
   
    public int getNombreCartesEquipage() {
        int total = 0;
        for (ArrayList<CarteViking> cartes : zoneEquipage.values()) {
            total += cartes.size();
        }
        return total;
    }

    public void setPointVictoire(int pointVictoire) {
        this.pointVictoire = pointVictoire;
    }

    public void setPionsRecrue(int pionsRecrue) {
        this.pionsRecrue = pionsRecrue;
    }

    public void setPionsBracelet(int pionsBracelet) {
        this.pionsBracelet = pionsBracelet;
    }

    public abstract TypeAction choisirAction();

    public abstract List<TypeAction> choisirActionsDuTour();

    public abstract void deciderEtEffectuerCommerce();

    public abstract TypeAction choisirActionPrincipale();

    public abstract void recruter();

    public abstract void explorer();

    public abstract void commercer();

    public abstract CarteViking choisirCarteEquipage();

    public abstract boolean peutAcquerirCarteDestination(CarteDestination carteDestination);
    
    public abstract boolean acquerirCarteDestination(CarteDestination carteDestination);
    
    public abstract void utiliserPionsRecrut(int montant);

}