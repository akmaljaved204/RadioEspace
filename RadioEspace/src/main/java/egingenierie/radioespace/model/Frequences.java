package egingenierie.radioespace.model;

import org.json.JSONObject;

public class Frequences {
	private int id,departement_id;
	private String ville,frequence,departement_nom,region_nom,gps,updated_at,created_at;
	
	public Frequences(){
		
	}
	public Frequences(JSONObject jsonObject){
		try {
			this.id = jsonObject.getInt("id");
		} catch (Exception e) {
		}
		try {
			this.departement_id =jsonObject.getInt("departement_id");
		} catch (Exception e) {
		}
		try {
			this.ville = jsonObject.getString("ville");
		} catch (Exception e) {
		}
		try {
			this.frequence =jsonObject.getString("frequence");
		} catch (Exception e) {
		}
		try {
			this.departement_nom = jsonObject.getString("departement_nom");
		} catch (Exception e) {
		}
		try {
			this.region_nom =jsonObject.getString("region_nom");
		} catch (Exception e) {
		}
		try {
			this.gps = jsonObject.getString("gps");
		} catch (Exception e) {
		}
		try {
			this.updated_at = jsonObject.getString("updated_at");
		} catch (Exception e) {
		}
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDepartement_id() {
		return departement_id;
	}
	public void setDepartement_id(int departement_id) {
		this.departement_id = departement_id;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public String getFrequence() {
		return frequence;
	}
	public void setFrequence(String frequence) {
		this.frequence = frequence;
	}
	public String getDepartement_nom() {
		return departement_nom;
	}
	public void setDepartement_nom(String departement_nom) {
		this.departement_nom = departement_nom;
	}
	public String getRegion_nom() {
		return region_nom;
	}
	public void setRegion_nom(String region_nom) {
		this.region_nom = region_nom;
	}
	public String getGps() {
		return gps;
	}
	public void setGps(String gps) {
		this.gps = gps;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	
	
}
