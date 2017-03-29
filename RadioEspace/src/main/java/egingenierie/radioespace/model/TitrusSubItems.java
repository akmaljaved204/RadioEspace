package egingenierie.radioespace.model;

import org.json.JSONObject;


public class TitrusSubItems {

    String name, title, image, startDate;

    public TitrusSubItems() {

    }

    public TitrusSubItems(JSONObject jsonObject) {
        try {
            this.name = jsonObject.getString("artist");
        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            this.title = jsonObject.getString("title");
        } catch (Exception e) {
        }
        try {
            this.image = jsonObject.getString("picture");
        } catch (Exception e) {
        }
        try {
            this.startDate = jsonObject.getString("start_date");
        } catch (Exception e) {
        }

    }

    public TitrusSubItems(JSONObject jsonObject, int temp) {
        try {
            this.name = jsonObject.getString("artist");
        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            this.title = jsonObject.getString("title");
        } catch (Exception e) {
        }
        try {
            this.image = jsonObject.getString("cover");
        } catch (Exception e) {
        }
        try {
            this.startDate = jsonObject.getString("start_date");
        } catch (Exception e) {
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

}
