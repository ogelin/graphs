
public class Arc {
	
	private int distance; 
	private Sommet voisin; 
	
	public Arc(int distance, Sommet voisin){
		this.distance = distance; 
		this.voisin = voisin; 
	}
	public int getDistance(){
		return this.distance; 
	}
	
	public void setDistance(int distance){
		this.distance = distance; 
	}
	public Sommet getVoisin(){
		return this.voisin; 
	}
	
	public void setVoisin(Sommet voisin){
		this.voisin = voisin; 
	}

	@Override
	public String toString(){
		return voisin.getIdentifiant() + "(" + distance + ")" ;
	}
}
