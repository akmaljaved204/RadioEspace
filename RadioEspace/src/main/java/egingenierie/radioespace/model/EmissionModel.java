package egingenierie.radioespace.model;

import org.json.JSONObject;


public class EmissionModel {
	private int id,lundi,mardi,mercredi,jeudi,vendredi,samedi,dimanche,published ;
	private String title , subtitle , picture , text , start_hour,end_hour,created_at,updated_at,schedual="" ;
	private boolean openOrNot=false,isToday,isWeekend;
	
	public EmissionModel(){
		
	}
	
	public EmissionModel(JSONObject jsonObject){
		try {
			this.id = jsonObject.getInt("id");
		} catch (Exception e) {}
		try {
			this.lundi =jsonObject.getInt("lundi");
		} catch (Exception e) {}
		try {
			this.mardi = jsonObject.getInt("mardi");
		} catch (Exception e) {}
		try {
			this.mercredi =jsonObject.getInt("mercredi");
		} catch (Exception e) {}
		try {
			this.jeudi =jsonObject.getInt("jeudi");
		} catch (Exception e) {}
		try {
			this.vendredi =jsonObject.getInt("vendredi");
		} catch (Exception e) {}
		try {
			this.samedi =jsonObject.getInt("samedi");
		} catch (Exception e) {}
		try {
			this.dimanche = jsonObject.getInt("dimanche");
		} catch (Exception e) {}
		try {
			this.published = jsonObject.getInt("published");
		} catch (Exception e) {}
		try {
			this.title =jsonObject.getString("title");
		} catch (Exception e) {
		}
		try {
			this.subtitle = jsonObject.getString("subtitle");
		} catch (Exception e) {
		}
		try {
			this.picture = jsonObject.getString("picture");
		} catch (Exception e) {
		}
		try {
			this.text = jsonObject.getString("text");
		} catch (Exception e) {
		}
		try {
			this.start_hour = jsonObject.getString("start_hour");
		} catch (Exception e) {
		}
		try {
			this.end_hour = jsonObject.getString("end_hour");
		} catch (Exception e) {
		}
		try {
			this.created_at = jsonObject.getString("created_at");
		} catch (Exception e) {
		}
		try {
			this.updated_at = jsonObject.getString("updated_at");
		} catch (Exception e) {
		}
	}
	
	
	public boolean isWeekend() {
		return isWeekend;
	}

	public void setWeekend(boolean isWeekend) {
		this.isWeekend = isWeekend;
	}

	public boolean isToday() {
		return isToday;
	}

	public void setToday(boolean isToday) {
		this.isToday = isToday;
	}
	
	public String getSchedual() {
		return schedual;
	}

	public void setSchedual(String schedual) {
		this.schedual = schedual;
	}

	public boolean isOpenOrNot() {
		return openOrNot;
	}

	public void setOpenOrNot(boolean openOrNot) {
		this.openOrNot = openOrNot;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getStart_hour() {
		return start_hour;
	}
	public void setStart_hour(String start_hour) {
		this.start_hour = start_hour;
	}
	public String getEnd_hour() {
		return end_hour;
	}
	public void setEnd_hour(String end_hour) {
		this.end_hour = end_hour;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLundi() {
		return lundi;
	}
	public void setLundi(int lundi) {
		this.lundi = lundi;
	}
	public int getMardi() {
		return mardi;
	}
	public void setMardi(int mardi) {
		this.mardi = mardi;
	}
	public int getMercredi() {
		return mercredi;
	}
	public void setMercredi(int mercredi) {
		this.mercredi = mercredi;
	}
	public int getJeudi() {
		return jeudi;
	}
	public void setJeudi(int jeudi) {
		this.jeudi = jeudi;
	}
	public int getVendredi() {
		return vendredi;
	}
	public void setVendredi(int vendredi) {
		this.vendredi = vendredi;
	}
	public int getSamedi() {
		return samedi;
	}
	public void setSamedi(int samedi) {
		this.samedi = samedi;
	}
	public int getDimanche() {
		return dimanche;
	}
	public void setDimanche(int dimanche) {
		this.dimanche = dimanche;
	}
	public int getPublished() {
		return published;
	}
	public void setPublished(int published) {
		this.published = published;
	} 	
}
