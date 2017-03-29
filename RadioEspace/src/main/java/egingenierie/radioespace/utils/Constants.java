package egingenierie.radioespace.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import android.content.Context;
import android.os.Environment;

public class Constants {
    public static String GOOGLE_ADDMOB_INTERSTITIAL_KEY = "ca-app-pub-1307374885636076/2139304347";
    public static String GOOGLE_ANALYTICS_KEY = "UA-26087717-7";

    public static String APP_LINK = "";
    public static String WEB_LINK = "";
    public static String FACEBOOK_PAGE_LINK = "";
    public static String TWITTER_LINK = "";
    public static String GPLUS_LINK = "";
    public static String INSTAGRAM_LINK = "";
    public static String MENTION_LEGAL_LINK = "";

    public static final String BASE_URL = "http://www.radioespace.com/api/";
    public static final String PUBLIC_KEY = "2c7347b710c5654dd5cab87aa1f6b960428e953bbfe81554c475241b73455e36";
    public static final String PRIVATE_KEY = "50091e04dddb6b0d54fb8d323aa426876a4abbf18972e7c4f68284ea90f8e5b6";
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.RadioEspace";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    public static Context APP_CONTEXT = null;
    public static Context ACTIVITY_CONTEXT = null;

    public static Context POPUPCONTEXT = null;
    public static boolean DONTTRPEATPOPUP = true;

    public static byte[] calculateRFC2104HMACBinary(String data, String key)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        return mac.doFinal(data.getBytes());
    }

    public static String base64Encode(byte bytes[]) {
        byte[] encodedBytes = Base64.encodeBase64(bytes);
        return new String(encodedBytes);
    }

}