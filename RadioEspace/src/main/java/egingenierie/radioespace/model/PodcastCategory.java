package egingenierie.radioespace.model;

import java.io.Serializable;

import org.json.JSONObject;

public class PodcastCategory implements Serializable{
	private String title, picture;
	private int showId, count;

	public PodcastCategory() {

	}

	public PodcastCategory(JSONObject object) {
		try {
			showId = object.getInt("podcast_category_id");
		} catch (Exception e) {
		}
		try {
			count = object.getInt("count");
		} catch (Exception e) {
		}
		try {
			title = object.getString("title");
		} catch (Exception e) {
		}
		try {
			picture = object.getString("picture");
		} catch (Exception e) {
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public int getShowId() {
		return showId;
	}

	public void setShowId(int showId) {
		this.showId = showId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}