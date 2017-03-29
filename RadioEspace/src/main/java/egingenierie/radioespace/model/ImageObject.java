package egingenierie.radioespace.model;

import java.io.Serializable;

import org.json.JSONObject;

public class ImageObject implements Serializable {

    private int imageId, allowComments, categoryId;
    private String title, updatedAt, file, description, createdAt;

    public ImageObject() {

    }

    public ImageObject(JSONObject object) {
        try {
            this.imageId = object.getInt("id");
        } catch (Exception e) {
        }
        try {
            this.allowComments = object.getInt("allow_comments");
        } catch (Exception e) {
        }
        try {
            this.categoryId = object.getInt("category_id");
        } catch (Exception e) {
        }
        try {
            this.title = object.getString("title");
        } catch (Exception e) {
        }
        try {
            this.updatedAt = object.getString("updated_at");
        } catch (Exception e) {
        }
        try {
            this.file = object.getString("file");
        } catch (Exception e) {
        }
        try {
            this.description = object.getString("description");
        } catch (Exception e) {
        }
        try {
            this.createdAt = object.getString("created_at");
        } catch (Exception e) {
        }

    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getAllowComments() {
        return allowComments;
    }

    public void setAllowComments(int allowComments) {
        this.allowComments = allowComments;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
