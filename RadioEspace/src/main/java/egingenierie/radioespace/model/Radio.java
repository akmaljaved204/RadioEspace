package egingenierie.radioespace.model;

import org.json.JSONObject;

import java.util.ArrayList;

public class Radio {

    private int id, highlight, published, playlist_id;
    private String title, subtitle, category, file, picture, thumb_smartphone, background, border, slug, flux_aac, flux_mp3, url_player, url_win8, playlist, pubs, created_at, updated_at,
            streamImage = "", artistName = "", songName = "", type, media_url;
    private boolean isRadioPlaying = false, isFavorite = false, isSelected = false, isLogedIn = false;
    private ArrayList<TitrusSubItems> children = new ArrayList<TitrusSubItems>();
    private NewsItems item = null;


    public Radio() {

    }

    public Radio(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
        } catch (Exception e) {}
        try {
            this.type = jsonObject.getString("type");
        } catch (Exception e) { }
        try {
            this.highlight = jsonObject.getInt("highlight");
        } catch (Exception e) {
        }
        try {
            this.published = jsonObject.getInt("published");
        } catch (Exception e) {
        }
        try {
            this.title = jsonObject.getString("title");
        } catch (Exception e) {
        }
        try {
            this.subtitle = jsonObject.getString("subtitle");
        } catch (Exception e) {
        }
        try {
            this.category = jsonObject.getString("category");
        } catch (Exception e) {
        }
        try {
            this.file = jsonObject.getString("file");
        } catch (Exception e) {
        }
        try {
            this.picture = jsonObject.getString("picture");
        } catch (Exception e) {
        }
        try {
            this.thumb_smartphone = jsonObject.getString("thumb_smartphone");
        } catch (Exception e) {
        }
        try {
            this.background = jsonObject.getString("background");
        } catch (Exception e) {
        }
        try {
            this.border = jsonObject.getString("border");
        } catch (Exception e) {
        }
        try {
            this.slug = jsonObject.getString("slug");
        } catch (Exception e) {
        }
        try {
            this.flux_aac = jsonObject.getString("flux_aac");
        } catch (Exception e) {
        }
        try {
            this.flux_mp3 = jsonObject.getString("flux_mp3");
        } catch (Exception e) {
        }
        try {
            this.url_player = jsonObject.getString("url_player");
        } catch (Exception e) {
        }
        try {
            this.url_win8 = jsonObject.getString("url_win8");
        } catch (Exception e) {
        }
        try {
            this.playlist = jsonObject.getString("playlist");
        } catch (Exception e) {
        }
        try {
            this.pubs = jsonObject.getString("pubs");
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
        try {
            this.playlist_id = jsonObject.getInt("playlist_id");
        } catch (Exception e) {
        }
        try {
            this.media_url = jsonObject.getString("media_url");
        } catch (Exception e) {
        }
    }


    public boolean isLogedIn() {
        return isLogedIn;
    }

    public void setLogedIn(boolean isLogedIn) {
        this.isLogedIn = isLogedIn;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public ArrayList<TitrusSubItems> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<TitrusSubItems> children) {
        this.children = children;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public NewsItems getItem() {
        return item;
    }

    public void setItem(NewsItems item) {
        this.item = item;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPlaylist_id() {
        return playlist_id;
    }

    public void setPlaylist_id(int playlist_id) {
        this.playlist_id = playlist_id;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public boolean isRadioPlaying() {
        return isRadioPlaying;
    }

    public void setRadioPlaying(boolean isRadioPlaying) {
        this.isRadioPlaying = isRadioPlaying;
    }

    public String getStreamImage() {
        return streamImage;
    }

    public void setStreamImage(String streamImage) {
        this.streamImage = streamImage;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHighlight() {
        return highlight;
    }

    public void setHighlight(int highlight) {
        this.highlight = highlight;
    }

    public int getPublished() {
        return published;
    }

    public void setPublished(int published) {
        this.published = published;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getThumb_smartphone() {
        return thumb_smartphone;
    }

    public void setThumb_smartphone(String thumb_smartphone) {
        this.thumb_smartphone = thumb_smartphone;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getFlux_aac() {
        return flux_aac;
    }

    public void setFlux_aac(String flux_aac) {
        this.flux_aac = flux_aac;
    }

    public String getFlux_mp3() {
        return flux_mp3;
    }

    public void setFlux_mp3(String flux_mp3) {
        this.flux_mp3 = flux_mp3;
    }

    public String getUrl_player() {
        return url_player;
    }

    public void setUrl_player(String url_player) {
        this.url_player = url_player;
    }

    public String getUrl_win8() {
        return url_win8;
    }

    public void setUrl_win8(String url_win8) {
        this.url_win8 = url_win8;
    }

    public String getPlaylist() {
        return playlist;
    }

    public void setPlaylist(String playlist) {
        this.playlist = playlist;
    }

    public String getPubs() {
        return pubs;
    }

    public void setPubs(String pubs) {
        this.pubs = pubs;
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

}