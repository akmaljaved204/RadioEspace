package egingenierie.radioespace.radiostreeming;

import java.io.IOException;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PlaybackService extends Service implements OnPreparedListener,
        OnBufferingUpdateListener, OnErrorListener,
        OnInfoListener {

    private static final String LOG_TAG = PlaybackService.class.getName();

    private static final String SERVICE_PREFIX = "com.fortsolution.generations.radiostreeming.";
    public static final String SERVICE_CHANGE_NAME = SERVICE_PREFIX + "CHANGE";
    public static final String SERVICE_CLOSE_NAME = SERVICE_PREFIX + "CLOSE";
    public static final String SERVICE_UPDATE_NAME = SERVICE_PREFIX + "UPDATE";
    public static final String EXTRA_TITLE = "titolo";
    public static final String EXTRA_DOWNLOADED = "downloaded";
    public static final String EXTRA_DURATION = "durata";
    public static final String EXTRA_POSITION = "posizione";

    private MediaPlayer mediaPlayer;
    private boolean isPrepared = false;

    private StreamProxy proxy;
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 1;
    private int bindCount = 0;

    private TelephonyManager telephonyManager;
    private PhoneStateListener listener;
    private boolean isPausedInCall = false;
    private Intent lastChangeBroadcast;
    private Intent lastUpdateBroadcast;
    private int lastBufferPercent = 0;
    private Thread updateProgressThread;


    // Amount of time to rewind playback when resuming after call
    private final static int RESUME_REWIND_TIME = 3000;

    @Override
    public void onCreate() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnPreparedListener(this);

        notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        //  System.out.println("LOG_TAG Playback service created");

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        // Create a PhoneStateListener to watch for offhook and idle events
        listener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        // Phone going offhook or ringing, pause the player.
                        if (isPlaying()) {
                            mute();
                            isPausedInCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        // Phone idle. Rewind a couple of seconds and start playing.
                        if (isPausedInCall) {
                            unMute();
                        }
                        break;
                }
            }
        };

        // Register the listener with the telephony manager.
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        bindCount++;
//    Log.d(LOG_TAG, "Bound PlaybackService, count " + bindCount);
        return new ListenBinder();
    }

    @Override
    public boolean onUnbind(Intent arg0) {
        bindCount--;
        //   Log.d(LOG_TAG, "Unbinding PlaybackService, count " + bindCount);
        if (!isPlaying() && bindCount == 0) {
            //     Log.w(LOG_TAG, "Will stop self");
            stopSelf();
        } else {
            //   Log.d(LOG_TAG, "Will not stop self");
        }
        return false;
    }

    synchronized public boolean isPlaying() {
        if (isPrepared) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    synchronized public int getPosition() {
        if (isPrepared) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    synchronized public int getDuration() {
        if (isPrepared) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    synchronized public int getCurrentPosition() {
        if (isPrepared) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    synchronized public void seekTo(int pos) {
        if (isPrepared) {
            mediaPlayer.seekTo(pos);
        }
    }

    synchronized public void play() {
        if (!isPrepared) {
            //   Log.e(LOG_TAG, "play - not prepared");
            return;
        }
        //  Log.d(LOG_TAG, "play " );
        mediaPlayer.start();

        // Change broadcasts are sticky, so when a new receiver connects, it will
        // have the data without polling.
        if (lastChangeBroadcast != null) {
            getApplicationContext().removeStickyBroadcast(lastChangeBroadcast);
        }
        lastChangeBroadcast = new Intent(SERVICE_CHANGE_NAME);
        lastChangeBroadcast.putExtra(EXTRA_TITLE, "titolo");
        getApplicationContext().sendStickyBroadcast(lastChangeBroadcast);
    }

    synchronized public void pause() {
        //   Log.d(LOG_TAG, "pause");
        if (isPrepared) {
            mediaPlayer.pause();
        }
        notificationManager.cancel(NOTIFICATION_ID);
    }

    synchronized public void mute() {
        if (isPrepared) {
            mediaPlayer.setVolume(0, 0);
        }
    }

    synchronized public void unMute() {
        if (isPrepared) {
            mediaPlayer.setVolume(1, 1);
        }
    }

    synchronized public void stop() {
        //  Log.d(LOG_TAG, "stop");
        if (isPrepared) {
            if (proxy != null) {
                proxy.stop();
                proxy = null;
            }
            mediaPlayer.stop();
            isPrepared = false;
        }
        cleanup();
    }


    /**
     * Start listening to the given URL.
     */
    public void listen(String url)
            throws IllegalArgumentException, IllegalStateException, IOException {

        // return if playing.
        if (isPlaying()) {
            return;
        }

        // From 2.2 on (SDK ver 8), the local mediaplayer can handle Shoutcast
        // streams natively. Let's detect that, and not proxy.
        //Log.d(LOG_TAG, "SDK Version " + Build.VERSION.SDK);

        int sdkVersion = 0;
        try {
            sdkVersion = Integer.parseInt(Build.VERSION.SDK);
        } catch (NumberFormatException e) {
        }

        // Log.d(LOG_TAG, "listening to " + url );
        String playUrl = url;

        synchronized (this) {
            //    Log.d(LOG_TAG, "reset");
            mediaPlayer.reset();

            if (sdkVersion < 8) {
                if (proxy == null) {
                    proxy = new StreamProxy();
                    proxy.init();
                    proxy.start();
                }
                String proxyUrl = String.format("http://127.0.0.1:%d/%s",
                        proxy.getPort(), url);
                playUrl = proxyUrl;

            } else {
                String content = FileUtils.readFile("/system/build.prop");

                if (content.contains("ro.product.brand=samsung")) {
                    if (proxy == null) {
                        proxy = new StreamProxy();
                        proxy.init();
                        proxy.start();
                    }
                    String proxyUrl = String.format("http://127.0.0.1:%d/%s",
                            proxy.getPort(), url);
                    playUrl = proxyUrl;
                    Log.d(LOG_TAG, "media.stagefright.enable-player is enables. buffering the content ");
                }

            }

            mediaPlayer.setDataSource(playUrl);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //  Log.d(LOG_TAG, "Preparing: " + playUrl);
            mediaPlayer.prepareAsync();
            // Log.d(LOG_TAG, "Waiting for prepare");
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // Log.d(LOG_TAG, "Prepared");
        synchronized (this) {
            if (mediaPlayer != null) {
                isPrepared = true;

            }
        }

        if (onPreparedListener != null) {
            onPreparedListener.onPrepared(mp);
        }

        updateProgressThread = new Thread(new Runnable() {
            public void run() {
                // Initially, don't send any updates, since it takes a while for the
                // media player to settle down.
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    return;
                }
                while (true) {
                    updateProgress();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        updateProgressThread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.w(LOG_TAG, "Service exiting");

        if (updateProgressThread != null) {
            updateProgressThread.interrupt();
            try {
                updateProgressThread.join(3000);
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, "", e);
            }
        }

        stop();
        synchronized (this) {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }

        telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
    }

    public class ListenBinder extends Binder {

        public PlaybackService getService() {
            return PlaybackService.this;
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int progress) {
        if (isPrepared) {
            lastBufferPercent = progress;
            updateProgress();
        }
    }

    /**
     * Sends an UPDATE broadcast with the latest info.
     */
    private void updateProgress() {
        if (isPrepared && mediaPlayer != null && mediaPlayer.isPlaying()) {
            // Update broadcasts are sticky, so when a new receiver connects, it will
            // have the data without polling.
            if (lastUpdateBroadcast != null) {
                getApplicationContext().removeStickyBroadcast(lastUpdateBroadcast);
            }

            lastUpdateBroadcast = new Intent(SERVICE_UPDATE_NAME);
            lastUpdateBroadcast.putExtra(EXTRA_DURATION, mediaPlayer.getDuration());
            lastUpdateBroadcast.putExtra(EXTRA_DOWNLOADED,
                    (int) ((lastBufferPercent / 100.0) * mediaPlayer.getDuration()));
            lastUpdateBroadcast.putExtra(EXTRA_POSITION,
                    mediaPlayer.getCurrentPosition());
            getApplicationContext().sendStickyBroadcast(lastUpdateBroadcast);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // Log.w(LOG_TAG, "onError(" + what + ", " + extra + ")");
        synchronized (this) {
            if (!isPrepared) {
                // This file was not good and MediaPlayer quit
                //  Log.w(LOG_TAG,
                //  "MediaPlayer refused to play current item. Bailing on prepare.");
            }


        }

        if (onErrorListener != null) {
            onErrorListener.onError(mp, what, extra);
        }

        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
        // Log.w(LOG_TAG, "onInfo(" + arg1 + ", " + arg2 + ")");
        return false;
    }

    /**
     * Remove all intents and notifications about the last media.
     */
    private void cleanup() {
        notificationManager.cancel(NOTIFICATION_ID);
        if (lastChangeBroadcast != null) {
            getApplicationContext().removeStickyBroadcast(lastChangeBroadcast);
        }
        if (lastUpdateBroadcast != null) {
            getApplicationContext().removeStickyBroadcast(lastUpdateBroadcast);
        }
        getApplicationContext().sendBroadcast(new Intent(SERVICE_CLOSE_NAME));
    }

    // -----------
    // Some stuff added for inspection when testing

    private OnCompletionListener onCompletionListener;

    /**
     * Allows a class to be notified when the currently playing track is
     * completed. Mostly used for testing the service
     *
     * @param listener
     */
    public void setOnCompletionListener(OnCompletionListener listener) {
        onCompletionListener = listener;
    }

    private OnPreparedListener onPreparedListener;
    private OnErrorListener onErrorListener;

    /**
     * Allows a class to be notified when the currently selected track has been
     * prepared to start playing. Mostly used for testing.
     *
     * @param listener
     */
    public void setOnPreparedListener(OnPreparedListener listener) {
        onPreparedListener = listener;
    }

    public void setOnErrorListener(OnErrorListener listener) {
        onErrorListener = listener;
    }
}
