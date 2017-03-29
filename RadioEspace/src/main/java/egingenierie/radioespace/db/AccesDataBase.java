package egingenierie.radioespace.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import egingenierie.radioespace.model.BaseURL;
import egingenierie.radioespace.model.Category;
import egingenierie.radioespace.model.Featured;
import egingenierie.radioespace.model.ImageObject;
import egingenierie.radioespace.model.NewsItems;
import egingenierie.radioespace.model.Radio;
import egingenierie.radioespace.utils.Constants;

public class AccesDataBase {

    private static AccesDataBase accesDataBase = null;
    private DatabaseHelper db;

    private AccesDataBase() {
        db = new DatabaseHelper(Constants.APP_CONTEXT);
    }

    public static AccesDataBase getDataBaseAcces() {
        if (accesDataBase == null)
            accesDataBase = new AccesDataBase();
        return accesDataBase;
    }

    public List<Radio> getFavoritesRadio() {
        String query = "Select * from favorite ORDER BY id DESC";
        return getFavoritesRadio(query);
    }

    private List<Radio> getFavoritesRadio(String query) {
        Cursor cursor;
        List<Radio> radioList = new ArrayList<Radio>();
        try {
            db.open();
            cursor = db.executeSelect(query);
            if (cursor.moveToFirst())
                do {
                    Radio radio = new Radio();
                    radio.setId(cursor.getInt(0));
                    radio.setHighlight(cursor.getInt(1));
                    radio.setPublished(cursor.getInt(2));
                    radio.setTitle(cursor.getString(3));
                    radio.setSubtitle(cursor.getString(4));
                    radio.setCategory(cursor.getString(5));
                    radio.setFile(cursor.getString(6));
                    radio.setPicture(cursor.getString(7));
                    radio.setThumb_smartphone(cursor.getString(8));
                    radio.setBackground(cursor.getString(9));
                    radio.setBorder(cursor.getString(10));
                    radio.setSlug(cursor.getString(11));
                    radio.setFlux_aac(cursor.getString(12));
                    radio.setFlux_mp3(cursor.getString(13));
                    radio.setUrl_player(cursor.getString(14));
                    radio.setUrl_win8(cursor.getString(15));
                    radio.setPlaylist(cursor.getString(16));
                    radio.setPubs(cursor.getString(17));
                    radio.setCreated_at(cursor.getString(18));
                    radio.setUpdated_at(cursor.getString(19));
                    radio.setStreamImage(cursor.getString(20));
                    radio.setArtistName(cursor.getString(21));
                    radio.setSongName(cursor.getString(22));
                    int temp = cursor.getInt(23);
                    if (temp == 0) {
                        radio.setRadioPlaying(false);
                    } else {
                        radio.setRadioPlaying(true);
                    }
                    int temp2 = cursor.getInt(24);
                    if (temp2 == 0) {
                        radio.setFavorite(false);
                    } else {
                        radio.setFavorite(true);
                    }
                    radioList.add(radio);
                } while (cursor.moveToNext());
            cursor.close();
            db.close();
        } catch (Exception e) {
            System.out.println("Exception====" + e);
        } finally {
            db.close();
        }
        return radioList;

    }

    public void makeRadioFavorite(Radio object) {
        insertRadioInDB(object);
    }

    public void removeFavorite(int id) {
        db.open();
        db.deleteFromTable(id);
        db.close();
    }

    public void removeFavoriteArticle(int id) {
        db.open();
        db.deleteFromTableArticle(id);
        db.close();
    }

    private void insertRadioInDB(Radio radio) {
        try {
            int playing = 0, favorite = 0;
            if (radio.isRadioPlaying()) {
                playing = 1;
            }
            if (radio.isFavorite()) {
                favorite = 1;
            }
            String query = "INSERT INTO favorite (id,highlight,published,title,subtitle,category,file,picture,thumb_smartphone,background,border,slug,flux_aac,flux_mp3,url_player,url_win8,playlist,pubs,created_at,updated_at,streamImage,artistName,songName,isRadioPlaying,isFavorite) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            String[] valuesImages = {"" + radio.getId(), "" + radio.getHighlight(), "" + radio.getPublished(), radio.getTitle(), radio.getSubtitle(),
                    radio.getCategory(), radio.getFile(), radio.getPicture(), radio.getThumb_smartphone(), radio.getBackground(), radio.getBorder(), radio.getSlug(), radio.getFlux_aac(), radio.getFlux_mp3(), radio.getUrl_player(), radio.getUrl_win8(), radio.getPlaylist(), radio.getPubs(), radio.getCreated_at(), radio.getStreamImage(), radio.getArtistName(), radio.getSongName(), "" + playing, "" + favorite};
            db.open();
            db.executeInsert(query, valuesImages);
            db.close();
        } catch (Exception e) {
            db.close();
        }
    }

    public void insertArticleInDB(NewsItems featured) {
        try {
            int fetr = featured.getIsFavourite();

            db.open();
            String insertQuery = "INSERT INTO favouritearticle (id,category,title,imageUrl,media,isFavourite,file,temp2,temp3,temp4) values (?,?,?,?,?,?,?,?,?,?)";
            String[] valuesImages = {"" + featured.getNewsId(), "" + featured.getType(), featured.getTitle(), featured.getMediaUrl() + featured.getPicture(), "", "" + featured.getIsFavourite(), "", "", "", ""};

            db.executeInsert(insertQuery, valuesImages);
            db.close();

        } catch (Exception e) {
            db.close();
        }
    }

    public void insertArticleInDBFeatured(Featured featured) {
        try {
            int fetr = featured.getIsFavourite();

            db.open();
            String insertQuery = "INSERT INTO favouritearticle (id,category,title,imageUrl,media,isFavourite,file,temp2,temp3,temp4) values (?,?,?,?,?,?,?,?,?,?)";
            String[] valuesImages = {"" + featured.getId(), "" + featured.getCategory().getName(), featured.getTitle(), featured.getBaseURL().getImageUrl(), "", "" + featured.getIsFavourite(), featured.getImage().getFile(), featured.getImage_id(), featured.getPodcast_id(), featured.getVideo_id()};

            db.executeInsert(insertQuery, valuesImages);
            db.close();

        } catch (Exception e) {
            db.close();
        }
    }

    public int getParticularFavouriteData(int id) {
        String query = "Select * from favouritearticle WHERE id=" + id;
        return getFavouriteParticular(query);
    }

    private int getFavouriteParticular(String query) {
        Cursor cursor;
        int isFavourite = 0;

        try {
            db.open();
            cursor = db.executeSelect(query);
            if (cursor.moveToFirst())
                do {
                    NewsItems category = new NewsItems();
                    category.setIsFavourite(cursor.getInt(5));
                    if (category.getIsFavourite() == 0) {
                        isFavourite = 0;
                    } else if (category.getIsFavourite() == 1) {
                        isFavourite = 1;
                    }
                } while (cursor.moveToNext());
            cursor.close();
            db.close();
        } catch (Exception e) {
            System.out.println("Exception====" + e);
        } finally {
            db.close();
        }
        return isFavourite;
    }

    public List<Featured> getFavouriteData() {
        String query = "Select * from favouritearticle";// ORDER BY id DESC
        return getFavourite(query);
    }

    private List<Featured> getFavourite(String query) {
        Cursor cursor;
        List<Featured> Categories = new ArrayList<Featured>();
        try {
            db.open();
            cursor = db.executeSelect(query);
            if (cursor.moveToFirst())
                do {
                    Category category = new Category();
                    BaseURL baseUrl = new BaseURL();
                    Featured featured = new Featured();
                    ImageObject imgObject = new ImageObject();
                    featured.setId(cursor.getInt(0));
                    category.setName(cursor.getString(1));
                    featured.setCategory(category);
                    featured.setTitle(cursor.getString(2));
                    baseUrl.setImageUrl(cursor.getString(3));
                    featured.setBaseURL(baseUrl);
                    featured.setIsFavourite(cursor.getInt(5));
                    imgObject.setFile(cursor.getString(6));
                    featured.setImage(imgObject);
                    featured.setIsFavourite(cursor.getInt(2));
                    featured.setImage_id(cursor.getString(7));
                    featured.setPodcast_id(cursor.getString(8));
                    featured.setVideo_id(cursor.getString(9));

                    Categories.add(featured);
                } while (cursor.moveToNext());
            cursor.close();
            db.close();
        } catch (Exception e) {
            System.out.println("Exception====" + e);
        } finally {
            db.close();
        }
        return Categories;
    }

}
