package egingenierie.radioespace.model;

import java.io.Serializable;

public class BaseURL implements Serializable {
    private String podcastUrl, imageUrl, diaporamaUrl, cinemaUrl;


    public String getCinemaUrl() {
        return cinemaUrl;
    }

    public void setCinemaUrl(String cinemaUrl) {
        this.cinemaUrl = cinemaUrl;
    }

    public String getPodcastUrl() {
        return podcastUrl;
    }

    public void setPodcastUrl(String podcastUrl) {
        this.podcastUrl = podcastUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDiaporamaUrl() {
        return diaporamaUrl;
    }

    public void setDiaporamaUrl(String diaporamaUrl) {
        this.diaporamaUrl = diaporamaUrl;
    }

}
