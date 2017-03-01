import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Graphe {
	private ArrayList<Sommet> sommets = new ArrayList<Sommet>();
	private ArrayList<Arc> arcs = new ArrayList<Arc>(); 

	public void creerGraphe(String fichier) {

		ArrayList<String> contenu = new ArrayList<String>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(fichier));
			String ligne;

			while ((ligne = reader.readLine()) != null) {
				contenu.add(ligne);
			}
			reader.close();
		} 
		catch (Exception e) {
			System.err.format("Incapable de lire '%s'.", fichier + ". Essayer un autre format.");
			e.printStackTrace();
		}

		ArrayList<String> sommets = new ArrayList<String>(Arrays.asList(contenu.get(0).split(";"))); 
		ArrayList<String> arcs = new ArrayList<String>(Arrays.asList(contenu.get(1).split(";"))); 

		this.initialiserSommets(sommets);
		this.initialiserArcs(arcs);

	}

	public void initialiserSommets (ArrayList<String> sommets){
		for (int i = 0; i<sommets.size(); i++){
			String identifiant = sommets.get(i).split(",")[0];
			String type = sommets.get(i).split(",")[1];
			int gain  = Integer.parseInt(sommets.get(i).split(",")[2]);
			this.sommets.add(new Sommet (identifiant, type, gain, new ArrayList<Arc>(), Integer.MAX_VALUE));
		}
	}

	public void initialiserArcs (ArrayList<String> arcs){
		for (int i = 0; i<arcs.size(); i++){
			int distance  = Integer.parseInt(arcs.get(i).split(",")[2]);
			String startPoint = arcs.get(i).split(",")[0];
			String endPoint = arcs.get(i).split(",")[1];

			for (int j = 0; j<this.sommets.size(); j++){

				if (startPoint.equals(this.sommets.get(j).getIdentifiant())){

					for (int k = 0; k<this.sommets.size(); k++){

						if (endPoint.equals( this.sommets.get(k).getIdentifiant())){

							Arc connexion = new Arc(distance, this.sommets.get(k));
							this.arcs.add(connexion);
							this.sommets.get(j).addArc(connexion);

							//on répète dans le sens entrant/sortant inverse pour avoir bidirectionalité
							connexion = new Arc(distance, this.sommets.get(j));
							this.arcs.add(connexion);
							this.sommets.get(k).addArc(connexion);
						}

					}
				}
			}
		}
	}

	public void lireGrapheSimplement() {
		for (int i = 0; i<this.sommets.size(); i++){
			System.out.println("Sommet " + i +": " + sommets.get(i).toString());
		}
	}

	public void lireGraphe(Graphe graphe){

		//vérifier que la liste des sommets n'est pas vide
		if(!graphe.getSommets().isEmpty() && !graphe.getArcs().isEmpty()) {

			for(int i=0;i< sommets.size();i++){
				//affichage des sommets
				System.out.print("(" + sommets.get(i).getIdentifiant() + ", " +
						sommets.get(i).getType() + ", " + sommets.get(i).getGain() + ", (");

				//affichage des sommets voisins et distances
				int voisinCount = 0;
				for(int j=0;j< arcs.size();j++) {
					if(sommets.get(i).getIdentifiant()==arcs.get(j).getVoisin().getIdentifiant()) {
						voisinCount++;
						System.out.print("VOISIN " + voisinCount + ": (" + arcs.get(j).getVoisin().getIdentifiant() + ", " + arcs.get(j).getDistance() + ") "  );
					}

				}

				System.out.println("");
			}
		}

	}

	//permet de d´eterminer, en vous inspirant de l’algorithme ´
	//de Dijkstra, le plus court chemin pour atteindre un gain minimal pass´e en param`etre. L’origine
	//(point de d´epart) et le gain voulu doivent ˆetre pass´es en param`etres. La fonction affiche le gain
	//final obtenu (qui doit ˆetre au moins ´egal au gain pass´e en param`etre), le plus court chemin utilis´e
	//et la longueur de ce dernier

	public String plusCourtChemin(String depart, int gainVoulu){

		int marcheMinimale = Integer.MAX_VALUE;
		ArrayList<Sommet> plusCourtChemin = this.gainsSuperieurs(depart, depart, gainVoulu);

		for (int i = 0; i<sommets.size(); i++){
			ArrayList<Sommet> altChemin = this.gainsSuperieurs(depart, sommets.get(i).getIdentifiant(), gainVoulu);
			int altMarche = this.calculatePathLength(altChemin);
			if (altMarche < marcheMinimale && altChemin.get(altChemin.size()-1).getDikjstraValue() > gainVoulu){
				marcheMinimale = altMarche;
				plusCourtChemin = altChemin;
			}
		}
		String chemin = "";
		for (int i = 0; i< plusCourtChemin.size()-1; i++){
			chemin = chemin + plusCourtChemin.get(i).getIdentifiant() + " -> ";
		}

		chemin = chemin + plusCourtChemin.get(plusCourtChemin.size()-1).getIdentifiant();

		chemin = "On peut obtenir un gain de " + this.calculatePathLength(plusCourtChemin) + " en suivant ce chemin: \n" + chemin;

		return chemin;
	}

	public int calculatePathLength(ArrayList<Sommet> path){
		Sommet current ;
		int pathLength = 0;
		for (int i = 0; i<path.size()-1; i++){
			current = path.get(i);
			Sommet next = path.get(i+1);
			for (int j = 0; j<current.getArcs().size(); j++){
				if (current.getArcs().get(j).getVoisin().getIdentifiant().equals(next.getIdentifiant())){
					pathLength = pathLength + current.getArcs().get(j).getDistance();
				}
			}
		}
		for (int i = 0; i<path.size(); i++){
			System.out.print(path.get(i).getIdentifiant() + "->");
		}

		System.out.println(pathLength);
		System.out.println(path.get(path.size()-1).getDikjstraValue());
		return pathLength;
	}

	public int calculatePathPoints(ArrayList<Sommet> path){
		int pts = 0;
		for (int i = 0; i<path.size()-1; i++){
			pts = pts + path.get(i).getGain();

		}
		return pts;
	}

	public Boolean isPokemon(Sommet sommet){
		return sommet.getType().equals("pokemon");
	}
	public Boolean isPokestop(Sommet sommet){
		return sommet.getType().equals("pokestop");
	}
	public Boolean isArena(Sommet sommet){
		return sommet.getType().equals("arene");
	}

	// Nous modifions la version de Dikjstra qui trouve la distance minimale entre 2 pts
	// pour trouver un chemin qui donne un résultat juste supérieur au gain voulu
	// en cherchant à maximiser plutôt que de minimiser le chemin pris entre deux points & en utilisant les
	// valeurs de gains comme si elles étaient des poids sur les arcs.

	public ArrayList<Sommet> gainsSuperieurs(String depart, String fin, int gainVoulu){

		ArrayList<Sommet> visitable = new ArrayList<Sommet>(); //starts with all nodes

		ArrayList<Sommet> unvisitable = new ArrayList<Sommet>(); //starts with no nodes.

		ArrayList<Sommet> visited = new ArrayList<Sommet>(); //starts with no nodes.

		for (int i = 0; i< this.sommets.size(); i++){
			visitable.add(this.sommets.get(i));
		}
		for (int i = 0; i< visitable.size(); i++){
			visitable.get(i).setDikjstraValue(Integer.MIN_VALUE);
		}

		this.findByName(depart, visitable).setDikjstraValue(this.findByName(depart,visitable).getGain());

		Sommet biggest = biggestDikjstraValue(visitable);

		while (!this.nameIsInList(fin, visited) && biggest.getDikjstraValue() <= gainVoulu) {

			int  distanceAdded = 0;
			if (visited.size()>2) {
				Sommet last = visited.get(visited.size() - 1);
				Sommet beforeLast = visited.get(visited.size() - 2);
				for (int j =0; j<last.getArcs().size(); j++){
					if (last.getArcs().get(j).getVoisin().equals(beforeLast.getIdentifiant())){
						distanceAdded = last.getArcs().get(j).getDistance();
					}
				}
			}

			for (int i = 0; i<unvisitable.size(); i++){
				unvisitable.get(i).setDistanceSinceVisit(distanceAdded);
				if (unvisitable.get(i).getDistanceSinceVisit() >= 100){
					visitable.add(unvisitable.get(i));
					unvisitable.remove(unvisitable.get(i));
				}
			}

			biggest = biggestDikjstraValue(visitable);
			visited.add(biggest);

			if (this.isArena(biggest)) {
				unvisitable.add(biggest);
				visitable.remove(biggest);
			}

			if (this.isPokestop(biggest)) {
				biggest.setDistanceSinceVisit(0);
				unvisitable.add(biggest);
				visitable.remove(biggest);
			}

			//if it's an arena or pokestop remove it. if it's a pokemon don't remove
			//track distance walked
			// after certain distance add it back to list
			if (biggest.getIdentifiant().equals(fin))
				visitable.remove(biggest);

			for (int i = 0; i<biggest.getArcs().size(); i++){

				if (nameIsInList(biggest.getArcs().get(i).getVoisin().getIdentifiant(), visitable)){

						if (biggest.getDikjstraValue() + biggest.getArcs().get(i).getVoisin().getGain() > biggest.getArcs().get(i).getVoisin().getDikjstraValue()){

							int altDikjstaDistance = biggest.getDikjstraValue() + biggest.getArcs().get(i).getVoisin().getGain();
							biggest.getArcs().get(i).getVoisin().setDikjstraValue(altDikjstaDistance);

					}
				}
			}

		}
		return visited;
	}

	private Sommet biggestDikjstraValue(ArrayList<Sommet> sommets){
		int maximum = Integer.MIN_VALUE;
		Sommet sommetMax = null ;
		for (int i = 0; i<sommets.size(); i++){
			if (sommets.get(i).getDikjstraValue() >= maximum){
				maximum = sommets.get(i).getDikjstraValue();
				sommetMax = sommets.get(i);
			}
		}
		return sommetMax;
	}

	public int dikjstra(String depart, String fin) {


		ArrayList<Sommet> unvisited = new ArrayList<Sommet>(); //starts with all nodes

		ArrayList<Sommet> visited = new ArrayList<Sommet>(); //starts with no nodes

		for (int i = 0; i< this.sommets.size(); i++){
			unvisited.add(this.sommets.get(i));
		}
		for (int i = 0; i< unvisited.size(); i++){
			unvisited.get(i).setDikjstraValue(Integer.MAX_VALUE);
		}
		this.findByName(depart,unvisited).setDikjstraValue(0);


		while (!this.nameIsInList(fin, visited)) {
			Sommet smallest = smallestDijkstraDistance(unvisited);
			visited.add(smallest);
			unvisited.remove(smallest);


			for (int i = 0; i<smallest.getArcs().size(); i++){

				if (!nameIsInList(smallest.getArcs().get(i).getVoisin().getIdentifiant(), visited)){

					if (smallest.getDikjstraValue() + smallest.getArcs().get(i).getDistance() < smallest.getArcs().get(i).getVoisin().getDikjstraValue()){

						int altDikjstaDistance = smallest.getDikjstraValue() + smallest.getArcs().get(i).getDistance();
						smallest.getArcs().get(i).getVoisin().setDikjstraValue(altDikjstaDistance);
					}
				}
			}

		}

		return this.findByName(fin,visited).getDikjstraValue();
	}

	private Sommet findByName(String identifiant, ArrayList<Sommet> sommets) throws IllegalArgumentException{
		for (int i = 0; i< sommets.size(); i++){
			if (sommets.get(i).getIdentifiant().equals(identifiant)){
				return sommets.get(i);
			}
		}
		throw new IllegalArgumentException(" Nous n'avons pas trouver le sommet \"" + identifiant+ "\" dans le graphe. Vérifiez le format du nom et essayez de nouveau. Si vous n'avez pas créé de graphe, commencez par l");
	}

	private Boolean nameIsInList(String identifiant, ArrayList<Sommet> sommets) throws IllegalArgumentException{
		for (int i = 0; i< sommets.size(); i++){
			if (sommets.get(i).getIdentifiant().equals(identifiant)){
				return true;
			}
		}
		return false;
	}

	private Sommet smallestDijkstraDistance(ArrayList<Sommet> sommets){
		int minimum = Integer.MAX_VALUE;
		Sommet sommetMin = null ;
		for (int i = 0; i<sommets.size(); i++){
			if (sommets.get(i).getDikjstraValue() <= minimum){
				minimum = sommets.get(i).getDikjstraValue();
				sommetMin = sommets.get(i);
			}
		}
		return sommetMin;
	}

	public ArrayList<Sommet> getSommets() {
		return this.sommets;
	}

	public void setSommet(ArrayList<Sommet> sommets) {
		this.sommets = sommets;
	}

	public ArrayList<Arc> getArcs() {
		return this.arcs;
	}

	public void setArcs(ArrayList<Arc> arcs) {
		this.arcs = arcs;
	}

}
