package egingenierie.radioespace.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by AkmalJaved on 5/3/2017.
 */
public class Weather implements Serializable {
    /*"id": "212",
        "date": "03 Mai",
        "tempe_morning": "9.6",
        "namepictos_morning": "Nuageux",
        "pictos_morning": "nuageux.png",
        "tempe_noon": "14.5",
        "namepictos_noon": "Nuageux",
        "pictos_noon": "nuageux.png",
        "tempe_afternoon": "17.4",
        "namepictos_afternoon": "Nuageux",
        "pictos_afternoon": "nuageux.png",
        "tempe_evening": "15.5",
        "namepictos_evening": "Dégagé",
        "pictos_evening": "soleil.png",
        "created_at": "2012-05-01 03:00:00",
        "updated_at": "2017-05-02 03:05:03"*/
    private String dayName,temperatureDate,morningWeather,morningWeatherImage,morningTemperature,
            noonWeather,noonWeatherImage,noonTemperature,
            afterNoonWeather,afterNoonWeatherImage,afterNoonTemperature,
            eveningWeather,eveningWeatherImage,eveningTemperature,mediaUrl;

    public Weather(JSONObject object,String dayNameL,String MediaURL) {
        dayName=dayNameL;
        mediaUrl=MediaURL;
      try {
          temperatureDate=object.getString("date");
      }catch (Exception ex){}
        try {
            morningWeather=object.getString("namepictos_morning");
        }catch (Exception ex){}
        try {
            morningWeatherImage=object.getString("pictos_morning");
        }catch (Exception ex){}
        try {
            morningTemperature=object.getString("tempe_morning");
        }catch (Exception ex){}
        try {
            noonWeather=object.getString("namepictos_noon");
        }catch (Exception ex){}
        try {
            noonWeatherImage=object.getString("pictos_noon");
        }catch (Exception ex){}
        try {
            noonTemperature=object.getString("tempe_noon");
        }catch (Exception ex){}
        try {
            afterNoonTemperature=object.getString("tempe_afternoon");
        }catch (Exception ex){}
        try {
            afterNoonWeather=object.getString("namepictos_afternoon");
        }catch (Exception ex){}
        try {
            afterNoonWeatherImage=object.getString("pictos_afternoon");
        }catch (Exception ex){}
        try {
            eveningTemperature=object.getString("tempe_evening");
        }catch (Exception ex){}
        try {
            eveningWeather=object.getString("namepictos_evening");
        }catch (Exception ex){}
        try {
            temperatureDate=object.getString("");
        }catch (Exception ex){}

        try {
            eveningWeatherImage=object.getString("pictos_evening");
        }catch (Exception ex){}
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getTemperatureDate() {
        return temperatureDate;
    }

    public void setTemperatureDate(String temperatureDate) {
        this.temperatureDate = temperatureDate;
    }

    public String getMorningWeather() {
        return morningWeather;
    }

    public void setMorningWeather(String morningWeather) {
        this.morningWeather = morningWeather;
    }

    public String getMorningWeatherImage() {
        return morningWeatherImage;
    }

    public void setMorningWeatherImage(String morningWeatherImage) {
        this.morningWeatherImage = morningWeatherImage;
    }

    public String getMorningTemperature() {
        return morningTemperature;
    }

    public void setMorningTemperature(String morningTemperature) {
        this.morningTemperature = morningTemperature;
    }

    public String getNoonWeather() {
        return noonWeather;
    }

    public void setNoonWeather(String noonWeather) {
        this.noonWeather = noonWeather;
    }

    public String getNoonWeatherImage() {
        return noonWeatherImage;
    }

    public void setNoonWeatherImage(String noonWeatherImage) {
        this.noonWeatherImage = noonWeatherImage;
    }

    public String getNoonTemperature() {
        return noonTemperature;
    }

    public void setNoonTemperature(String noonTemperature) {
        this.noonTemperature = noonTemperature;
    }

    public String getAfterNoonWeather() {
        return afterNoonWeather;
    }

    public void setAfterNoonWeather(String afternoonWeather) {
        this.afterNoonWeather = afternoonWeather;
    }

    public String getAfterNoonWeatherImage() {
        return afterNoonWeatherImage;
    }

    public void setAfterNoonWeatherImage(String afternoonWeatherImage) {
        this.afterNoonWeatherImage = afternoonWeatherImage;
    }

    public String getAfterNoonTemperature() {
        return afterNoonTemperature;
    }

    public void setAfterNoonTemperature(String afternoonTemperature) {
        this.afterNoonTemperature = afternoonTemperature;
    }

    public String getEveningWeather() {
        return eveningWeather;
    }

    public void setEveningWeather(String eveningWeather) {
        this.eveningWeather = eveningWeather;
    }

    public String getEveningWeatherImage() {
        return eveningWeatherImage;
    }

    public void setEveningWeatherImage(String eveningWeatherImage) {
        this.eveningWeatherImage = eveningWeatherImage;
    }

    public String getEveningTemperature() {
        return eveningTemperature;
    }

    public void setEveningTemperature(String eveningTemperature) {
        this.eveningTemperature = eveningTemperature;
    }
}
