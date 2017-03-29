package egingenierie.radioespace.model;

import java.io.Serializable;

import org.json.JSONObject;

public class Category implements Serializable {

    private int catId, published, imageId;
    private String updatedAt, principal, description, name, createdAt, parent, slug;

    public Category() {

    }

    public Category(JSONObject object) {
        try {
            this.catId = object.getInt("id");
        } catch (Exception e) {
        }
        try {
            this.published = object.getInt("published");
        } catch (Exception e) {
        }
        try {
            this.imageId = object.getInt("image_id");
        } catch (Exception e) {
        }
        try {
            this.updatedAt = object.getString("updated_at");
        } catch (Exception e) {
        }
        try {
            this.principal = object.getString("principal");
        } catch (Exception e) {
        }
        try {
            this.description = object.getString("description");
        } catch (Exception e) {
        }
        try {
            this.name = object.getString("name");
            this.name = name.toUpperCase();
        } catch (Exception e) {
        }
        try {
            this.createdAt = object.getString("created_at");
        } catch (Exception e) {
        }
        try {
            this.parent = object.getString("parent");
        } catch (Exception e) {
        }
        try {
            this.slug = object.getString("slug");
        } catch (Exception e) {
        }
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getPublished() {
        return published;
    }

    public void setPublished(int published) {
        this.published = published;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }


}
