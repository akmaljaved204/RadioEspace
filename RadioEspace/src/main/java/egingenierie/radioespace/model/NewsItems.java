package egingenierie.radioespace.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

public class NewsItems implements Serializable {

    private int newsId, hits, hits_today, hits_yesterday, allow_comments,
            published, hide_ads;
    private String type, category, title, subtitle, text, slug, picture,
            hits_reset, start_date, end_date, created_at, updated_at, mediaUrl,
            agendaTitle = "", fbLShares = "2", twtrShares = "1", gogleShrs = "4", dateToDisplay = "";
    private ArrayList<DetailMedia> detailMedia = new ArrayList<DetailMedia>();
    private Date agendaDate;
    private String sharedTitle = "", sharedDescription = "";
    private int isFavourite;
    private boolean isExpanded = false;

    public NewsItems() {

    }

    public NewsItems(JSONObject jsonObject) {
        try {
            this.newsId = jsonObject.getInt("id");
        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            this.hits = jsonObject.getInt("hits");
        } catch (Exception e) {
        }
        try {
            this.hits_today = jsonObject.getInt("hits_today");
        } catch (Exception e) {
        }
        try {
            this.hits_yesterday = jsonObject.getInt("hits_yesterday");
        } catch (Exception e) {
        }
        try {
            this.allow_comments = jsonObject.getInt("allow_comments");
        } catch (Exception e) {
        }
        try {
            this.published = jsonObject.getInt("published");
        } catch (Exception e) {
        }
        try {
            this.hide_ads = jsonObject.getInt("hide_ads");
        } catch (Exception e) {
        }
        try {
            this.type = jsonObject.getString("type");
        } catch (Exception e) {
        }
        try {
            this.category = jsonObject.getString("category");
        } catch (Exception e) {
        }
        try {
            this.title = jsonObject.getString("title").trim();
        } catch (Exception e) {
        }
        try {
            this.sharedTitle = jsonObject.getString("title").trim();
        } catch (Exception e) {
        }

        try {
            this.sharedDescription = jsonObject.getString("subtitle").trim();
        } catch (Exception e) {
        }
        try {
            this.subtitle = jsonObject.getString("subtitle").trim();
        } catch (Exception e) {
        }
        try {
            this.text = jsonObject.getString("text").trim();
        } catch (Exception e) {
        }
        try {
            this.slug = jsonObject.getString("slug");
        } catch (Exception e) {
        }
        try {
            this.picture = jsonObject.getString("picture");
        } catch (Exception e) {
        }
        try {
            this.hits_reset = jsonObject.getString("hits_reset");
        } catch (Exception e) {
        }
        try {
            this.start_date = jsonObject.getString("start_date");
        } catch (Exception e) {
        }
        try {
            this.end_date = jsonObject.getString("end_date");
        } catch (Exception e) {
        }
        try {
            this.created_at = jsonObject.getString("created_at");
            dateToDisplay = getFormattedDate(created_at);
        } catch (Exception e) {
            try {
                dateToDisplay = getFormattedDate(jsonObject.getString("start_date") + " " + jsonObject.getString("start_hour"));
            } catch (Exception exe) {
            }
        }
        try {
            this.updated_at = jsonObject.getString("updated_at");
        } catch (Exception e) {
        }
        try {
            this.mediaUrl = jsonObject.getString("media_url");
        } catch (Exception e) {
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("files");
            for (int i = 0; i < jsonArray.length(); i++) {
                this.detailMedia.add(new DetailMedia(jsonArray.getJSONObject(i), mediaUrl));
            }
        } catch (Exception e) {
        }

        try {
            this.start_date = convertDate(this.created_at);
        } catch (Exception ex) {


        }
        try {
            this.fbLShares = jsonObject.getString("share_facebook");
        } catch (Exception e) {
            System.out.println("sad");
        }

        try {
            this.twtrShares = jsonObject.getString("share_twitter");
        } catch (Exception e) {
            System.out.println("sad");
        }

        try {
            this.gogleShrs = jsonObject.getString("share_googleplus");
        } catch (Exception e) {
            System.out.println("sad");
        }

    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getSharedTitle() {
        return sharedTitle;
    }

    public void setSharedTitle(String sharedTitle) {
        this.sharedTitle = sharedTitle;
    }

    public String getSharedDescription() {
        return sharedDescription;
    }

    public void setSharedDescription(String sharedDescription) {
        this.sharedDescription = sharedDescription;
    }

    private String convertDate(String strDate) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "EEEE dd MMMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = "";

        try {
            date = inputFormat.parse(strDate);
            str = outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            str = "";
        }
        return str;
    }

    public String getFormattedDate(String strDate) {
        try {
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            String outputPattern = "dd/MM/yyyy HH:mm";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.FRANCE);

            Date date = null;
            String str = "";

            try {
                date = inputFormat.parse(strDate);
                str = outputFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
                str = "";
            }

            Calendar smsTime = Calendar.getInstance();
            smsTime.setTimeInMillis(date.getTime());
            Calendar now = Calendar.getInstance();
            if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
                String[] temp = str.split(" ");
                String formatedDate = str;
                if (temp.length == 2) {
                    formatedDate = "Aujourd'hui à " + temp[1].replace(":", "h");
                    formatedDate = formatedDate.replace(":", "h");
                }
                return formatedDate;
            } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
                String[] temp = str.split(" ");
                String formatedDate = str;
                if (temp.length == 2) {
                    formatedDate = "Hier à " + temp[1];
                    formatedDate = formatedDate.replace(":", "h");
                }
                return formatedDate;
            } else {
                str = str.replace(" ", " à ");
                str = str.replace(":", "h");
                return "Le " + str;
            }
        } catch (Exception ex) {
            return strDate;
        }
    }

    public String getDateToDisplay() {
        return dateToDisplay;
    }

    public void setDateToDisplay(String dateToDisplay) {
        this.dateToDisplay = dateToDisplay;
    }

    public String getAgendaTitle() {
        return agendaTitle;
    }

    public void setAgendaTitle(String agendaTitle) {
        this.agendaTitle = agendaTitle;
    }

    public Date getAgendaDate() {
        return agendaDate;
    }

    public void setAgendaDate(Date agendaDate) {
        this.agendaDate = agendaDate;
    }

    public ArrayList<DetailMedia> getDetailMedia() {
        return detailMedia;
    }

    public void setDetailMedia(DetailMedia detailMedia) {
        this.detailMedia.add(detailMedia);
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getHits_today() {
        return hits_today;
    }

    public void setHits_today(int hits_today) {
        this.hits_today = hits_today;
    }

    public int getHits_yesterday() {
        return hits_yesterday;
    }

    public void setHits_yesterday(int hits_yesterday) {
        this.hits_yesterday = hits_yesterday;
    }

    public int getAllow_comments() {
        return allow_comments;
    }

    public void setAllow_comments(int allow_comments) {
        this.allow_comments = allow_comments;
    }

    public int getPublished() {
        return published;
    }

    public void setPublished(int published) {
        this.published = published;
    }

    public int getHide_ads() {
        return hide_ads;
    }

    public void setHide_ads(int hide_ads) {
        this.hide_ads = hide_ads;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getHits_reset() {
        return hits_reset;
    }

    public void setHits_reset(String hits_reset) {
        this.hits_reset = hits_reset;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
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

    public String getFbLShares() {
        return fbLShares;
    }


    public void setFbLShares(String fbLShares) {
        this.fbLShares = fbLShares;
    }


    public String getTwtrShares() {
        return twtrShares;
    }


    public void setTwtrShares(String twtrShares) {
        this.twtrShares = twtrShares;
    }


    public String getGogleShrs() {
        return gogleShrs;
    }


    public void setGogleShrs(String gogleShrs) {
        this.gogleShrs = gogleShrs;
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
}
