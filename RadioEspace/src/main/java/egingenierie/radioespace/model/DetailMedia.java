package egingenierie.radioespace.model;

import java.io.Serializable;

import org.json.JSONObject;

public class DetailMedia implements Serializable {
    private int id, own_player, media_id;
    private String title, file, type, created_at, updated_at, content_id,
            label, media_url;
    private boolean isPlaying = false;

    public DetailMedia() {

    }

    public DetailMedia(JSONObject object, String media_url) {
        try {
            id = object.getInt("id");
        } catch (Exception e) {
        }
        try {
            own_player = object.getInt("own_player");
        } catch (Exception e) {
        }
        try {
            media_id = object.getInt("media_id");
        } catch (Exception e) {
        }
        try {
            title = object.getString("title");
        } catch (Exception e) {
        }
        try {
            file = object.getString("file");
        } catch (Exception e) {
        }
        try {
            type = object.getString("type");
        } catch (Exception e) {
        }
        try {
            created_at = object.getString("created_at");
        } catch (Exception e) {
        }
        try {
            updated_at = object.getString("updated_at");
        } catch (Exception e) {
        }
        try {
            content_id = object.getString("content_id");
        } catch (Exception e) {
        }
        try {
            label = object.getString("label");
        } catch (Exception e) {
        }
        try {
            this.media_url = object.getString("media_url");
        } catch (Exception e) {
            this.media_url = media_url;
        }
        // {"id":"7468",
        // "title":"The James Hunter Six - (Baby) hold on",
        // "content_id":"28501",
        // "updated_at":"0000-00-00 00:00:00",
        // "media_url":"http:\/\/www.jazzradio.fr\/media\/son\/",
        // "file":"the-james-hunter-six-baby-hold-on.mp3",
        // "media_id":"7468",
        // "created_at":"2015-12-01 11:48:15",
        // "label":"",
        // "own_player":"0",
        // "type":"son"}
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwn_player() {
        return own_player;
    }

    public void setOwn_player(int own_player) {
        this.own_player = own_player;
    }

    public int getMedia_id() {
        return media_id;
    }

    public void setMedia_id(int media_id) {
        this.media_id = media_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }
}
