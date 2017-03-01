import java.util.*; 
public class Sommet {
	
	private String identifiant = ""; 
	private String type = "";
	private int gain = 0; 
	private ArrayList<Arc> arcs = new ArrayList<Arc>();
	private int dikjstraDistance = Integer.MAX_VALUE;

	private int distanceSinceVisit = 0;
	
	public Sommet(String identifiant, String type, int gain, ArrayList<Arc> arcs, int dikjstraDistance){
		this.identifiant = identifiant; 
		this.type = type; 
		this.gain = gain; 
		this.arcs = arcs;
		this.distanceSinceVisit = 0;
		this.dikjstraDistance = dikjstraDistance;
	}
	public void addArc(Arc arc){
		this.arcs.add(arc);
	}

	public int getDistanceSinceVisit() {
		return distanceSinceVisit;
	}

	public void setDistanceSinceVisit(int distanceSinceVisit) {
		this.distanceSinceVisit = distanceSinceVisit;
	}
	
	public ArrayList<Arc> getArcs(){
		return this.arcs; 
	}
	public void setArcs(ArrayList<Arc> arcs){
		this.arcs = arcs; 
	}
	
	public String getIdentifiant(){
		return this.identifiant; 
	}
	public void setIdentifiant(String identifiant){
		this.identifiant = identifiant; 
	}
	
	public String getType(){
		return this.type; 
	}
	public void setType(String type){
		this.type = type; 
	}

	public int getGain(){
		return this.gain; 
	}
	public void setGain(int gain){
		this.gain = gain; 
	}

	public void setDikjstraValue(int dikjstraDistance){ this.dikjstraDistance = dikjstraDistance; }
	public int getDikjstraValue() { return this.dikjstraDistance; }

	@Override
	public String toString(){
		return "{IDENTIFIANT}: " + identifiant + " {TYPE}: " + type  + " {GAIN}: " + gain +
		 " {VOISINS}: " + arcs.toString()  ;
	}
}
