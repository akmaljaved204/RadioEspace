package egingenierie.radioespace.model;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by AkmalJaved on 11/15/2016.
 */
public class Podcast {
    private String audioFile,text,media_url,type,picture,title,category,updated_at,subtitle,created_at,slug,start_date,currentTime="00:00",totalTime="00:00",dateToDisplay;
    private int id,currentProgress=0,totalProgress=0;
    private boolean isPlaying=false,isLoading=false;
    public Podcast(){}
    public Podcast(JSONObject object){
        try {
            JSONObject fileObject=object.getJSONArray("files").getJSONObject(0);
            audioFile=fileObject.getString("file");
        }catch (Exception ex){}
        try {
            text=object.getString("text");
        }catch (Exception ex){}
        try {
            media_url=object.getString("media_url");
        }catch (Exception ex){}
        try {
            type=object.getString("type");
        }catch (Exception ex){}
        try {
            picture=object.getString("picture");
        }catch (Exception ex){}
        try {
            title=object.getString("title");
        }catch (Exception ex){}
        try {
            category=object.getString("category");
        }catch (Exception ex){}
        try {
            updated_at=object.getString("updated_at");
        }catch (Exception ex){}
        try {
            subtitle=object.getString("subtitle");
        }catch (Exception ex){}
        try {
            created_at=object.getString("created_at");
            dateToDisplay=convertDate(created_at);
        }catch (Exception ex){}
        try {
            slug=object.getString("slug");
        }catch (Exception ex){}
        try {
            start_date=object.getString("start_date");
        }catch (Exception ex){}
        try {
            id=object.getInt("id");
        }catch (Exception ex){}
    }
    private String convertDate(String strDate) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "EEEE dd MMMM yyyy HH:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.FRANCE);

        Date date = null;
        String str = "";

        try {
            date = inputFormat.parse(strDate);
            str = outputFormat.format(date);
            str=str.replace(":","h");
        } catch (Exception e) {
            e.printStackTrace();
            str="";
        }
        return str;
    }
    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getDateToDisplay() {
        return dateToDisplay;
    }

    public void setDateToDisplay(String dateToDisplay) {
        this.dateToDisplay = dateToDisplay;
    }

    public int getTotalProgress() {
        return totalProgress;
    }

    public void setTotalProgress(int totalProgress) {
        this.totalProgress = totalProgress;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
