package egingenierie.radioespace.model;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class Featured implements Serializable {

    private BaseURL baseURL;
    private String category_id, sub_category, image_id, video_id,
            podcast_id, author_id, on_home, on_slider, on_slider_title,
            on_slider_hierarchy, on_live, on_itcl, title, title_alt, headline,
            embed_html, slug, image_position, visits, last_visit, sticky,
            allow_comments, hide_ads, contributed, contributor_id,
            contributor_name, contributor_email, publication_date, published,
            premium, created_at, updated_at, modifiedDate;

    private ImageObject image;
    private Category category;
    private Date date, onlyMonth;
    private boolean isSection = false;
    private int isFavourite, id;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Featured() {

    }

    public Featured(String titleOfObj) {
        title = titleOfObj;
    }

    public Featured(JSONObject object, BaseURL lBaseURL) {
        this.baseURL = lBaseURL;
        try {
            id = object.getInt("id");
        } catch (Exception ex) {

        }
        try {
            category_id = object.getString("category_id");
        } catch (Exception ex) {

        }
        try {
            sub_category = object.getString("sub_category");
        } catch (Exception ex) {

        }
        try {
            image_id = object.getString("image_id");
        } catch (Exception ex) {

        }
        try {
            video_id = object.getString("video_id");
        } catch (Exception ex) {

        }
        try {
            podcast_id = object.getString("podcast_id");
        } catch (Exception ex) {

        }
        try {
            author_id = object.getString("author_id");
        } catch (Exception ex) {

        }
        try {
            on_home = object.getString("on_home");
        } catch (Exception ex) {

        }
        try {
            on_slider = object.getString("on_slider");
        } catch (Exception ex) {

        }
        try {
            on_slider_title = object.getString("on_slider_title");
        } catch (Exception ex) {

        }
        try {
            on_slider_hierarchy = object.getString("on_slider_hierarchy");
        } catch (Exception ex) {

        }
        try {
            on_live = object.getString("on_live");
        } catch (Exception ex) {

        }
        try {
            on_itcl = object.getString("on_itcl");
        } catch (Exception ex) {

        }
        try {
            title = object.getString("title");
        } catch (Exception ex) {

        }
        try {
            title_alt = object.getString("title_alt");
        } catch (Exception ex) {

        }
        try {
            headline = object.getString("headline");
        } catch (Exception ex) {

        }
        try {
            embed_html = object.getString("embed_html");
        } catch (Exception ex) {

        }
        try {
            slug = object.getString("slug");
        } catch (Exception ex) {

        }
        try {
            image_position = object.getString("image_position");
        } catch (Exception ex) {

        }
        try {
            visits = object.getString("visits");
        } catch (Exception ex) {

        }
        try {
            last_visit = object.getString("last_visit");
        } catch (Exception ex) {

        }
        try {
            sticky = object.getString("sticky");
        } catch (Exception ex) {

        }
        try {
            allow_comments = object.getString("allow_comments");
        } catch (Exception ex) {

        }
        try {
            hide_ads = object.getString("hide_ads");
        } catch (Exception ex) {

        }
        try {
            contributed = object.getString("contributed");
        } catch (Exception ex) {

        }
        try {
            contributor_id = object.getString("contributor_id");
        } catch (Exception ex) {

        }
        try {
            contributor_name = object.getString("contributor_name");
        } catch (Exception ex) {

        }

        try {
            contributor_email = object.getString("contributor_email");
        } catch (Exception ex) {

        }
        try {
            publication_date = object.getString("publication_date");
        } catch (Exception ex) {

        }
        try {
            published = object.getString("published");
        } catch (Exception ex) {

        }
        try {
            premium = object.getString("premium");
        } catch (Exception ex) {

        }
        try {

            created_at = object.getString("created_at");
            SimpleDateFormat inputFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = inputFormat1.parse(created_at);
            String[] temp = created_at.split(" ");
            modifiedDate = temp[0];
            //SimpleDateFormat inputFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            //onlyMonth = inputFormat2.parse(created_at);
            //System.out.println("");
        } catch (Exception ex) {

        }
        try {
            updated_at = object.getString("updated_at");
        } catch (Exception ex) {

        }
        try {
            image = new ImageObject(object.getJSONObject("image"));
        } catch (Exception ex) {
        }
        try {
            category = new Category(object.getJSONObject("category"));
        } catch (Exception ex) {
        }


    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BaseURL getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(BaseURL baseURL) {
        this.baseURL = baseURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getOnlyMonth() {
        return onlyMonth;
    }

    public void setOnlyMonth(Date onlyMonth) {
        this.onlyMonth = onlyMonth;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getImage_id() {
        return image_id;
    }

    public boolean isSection() {
        return isSection;
    }

    public void setSection(boolean isSection) {
        this.isSection = isSection;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getPodcast_id() {
        return podcast_id;
    }

    public void setPodcast_id(String podcast_id) {
        this.podcast_id = podcast_id;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getOn_home() {
        return on_home;
    }

    public void setOn_home(String on_home) {
        this.on_home = on_home;
    }

    public String getOn_slider() {
        return on_slider;
    }

    public void setOn_slider(String on_slider) {
        this.on_slider = on_slider;
    }

    public String getOn_slider_title() {
        return on_slider_title;
    }

    public void setOn_slider_title(String on_slider_title) {
        this.on_slider_title = on_slider_title;
    }

    public String getOn_slider_hierarchy() {
        return on_slider_hierarchy;
    }

    public void setOn_slider_hierarchy(String on_slider_hierarchy) {
        this.on_slider_hierarchy = on_slider_hierarchy;
    }

    public String getOn_live() {
        return on_live;
    }

    public void setOn_live(String on_live) {
        this.on_live = on_live;
    }

    public String getOn_itcl() {
        return on_itcl;
    }

    public void setOn_itcl(String on_itcl) {
        this.on_itcl = on_itcl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_alt() {
        return title_alt;
    }

    public void setTitle_alt(String title_alt) {
        this.title_alt = title_alt;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getEmbed_html() {
        return embed_html;
    }

    public void setEmbed_html(String embed_html) {
        this.embed_html = embed_html;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImage_position() {
        return image_position;
    }

    public void setImage_position(String image_position) {
        this.image_position = image_position;
    }

    public String getVisits() {
        return visits;
    }

    public void setVisits(String visits) {
        this.visits = visits;
    }

    public String getLast_visit() {
        return last_visit;
    }

    public void setLast_visit(String last_visit) {
        this.last_visit = last_visit;
    }

    public String getSticky() {
        return sticky;
    }

    public void setSticky(String sticky) {
        this.sticky = sticky;
    }

    public String getAllow_comments() {
        return allow_comments;
    }

    public void setAllow_comments(String allow_comments) {
        this.allow_comments = allow_comments;
    }

    public String getHide_ads() {
        return hide_ads;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setHide_ads(String hide_ads) {
        this.hide_ads = hide_ads;
    }

    public String getContributed() {
        return contributed;
    }

    public void setContributed(String contributed) {
        this.contributed = contributed;
    }

    public String getContributor_id() {
        return contributor_id;
    }

    public void setContributor_id(String contributor_id) {
        this.contributor_id = contributor_id;
    }

    public String getContributor_name() {
        return contributor_name;
    }

    public void setContributor_name(String contributor_name) {
        this.contributor_name = contributor_name;
    }

    public String getContributor_email() {
        return contributor_email;
    }

    public void setContributor_email(String contributor_email) {
        this.contributor_email = contributor_email;
    }

    public String getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(String publication_date) {
        this.publication_date = publication_date;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
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

    public int getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }

    public ImageObject getImage() {
        return image;
    }

    public void setImage(ImageObject image) {
        this.image = image;
    }


}
