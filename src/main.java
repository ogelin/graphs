import java.util.*;

public class main {


	 public static void main(String[] args){

				 String fichier = "src/data_pokemon.txt";

				 Graphe graph = new Graphe();
				 graph.creerGraphe(fichier);
				 graph.lireGraphe(graph);
				 System.out.println(graph.plusCourtChemin("p2", 1000));
			 }

	 }




