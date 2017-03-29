package egingenierie.radioespace.radiostreeming;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.IBinder;

import egingenierie.radioespace.RadioHomePage;
import egingenierie.radioespace.utils.Constants;

public class RadioPlayer {
    private ServiceConnection connection;
    public PlaybackService player;
    private int playerProgress = 0;
    private int downloadProgress = 0;
    private int maxProgress = 900000;
    public String radioURL;
    private static RadioPlayer radioPlayer;
    private Intent serviceIntent;
    private BroadcastReceiver broadcastReceiver;
    public boolean isRadioError = true;

    public static RadioPlayer getRadioPlayer() {
        if (radioPlayer == null) {
            radioPlayer = new RadioPlayer();
        }
        return radioPlayer;
    }

    private void attachToPlaybackService() {
        serviceIntent = new Intent(Constants.APP_CONTEXT, PlaybackService.class);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                player = ((PlaybackService.ListenBinder) service).getService();
                broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        try {
                            Bundle bundle = intent.getExtras();
                            if (bundle.get(PlaybackService.EXTRA_POSITION) != null) {
                                playerProgress = Integer
                                        .parseInt(""
                                                + bundle.get(PlaybackService.EXTRA_POSITION));
                                downloadProgress = Integer
                                        .parseInt(""
                                                + bundle.get(PlaybackService.EXTRA_DOWNLOADED));
                                if (playerProgress >= maxProgress) {
                                    playerProgress = playerProgress
                                            % maxProgress;
                                    downloadProgress = downloadProgress
                                            % maxProgress;
                                }
                            }
                        } catch (Exception exp) {
                            exp.printStackTrace();
                        }
                    }
                };
                player.registerReceiver(broadcastReceiver, new IntentFilter(PlaybackService.SERVICE_UPDATE_NAME));
                player.setOnPreparedListener(new OnPreparedListener() {
                    public void onPrepared(MediaPlayer mp) {
                        isRadioError = true;
                        player.play();
                    }
                });

                player.setOnErrorListener(new OnErrorListener() {
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        if (isRadioError) {
                            RadioHomePage.getRadioHomePage().showErrorMessage();
                            isRadioError = false;
                        }
                        return true;
                    }
                });
                listen();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                player = null;
            }
        };
        Constants.APP_CONTEXT.startService(serviceIntent);
        Constants.APP_CONTEXT.bindService(serviceIntent, connection, 0);
    }

    public void startRadio(String radiourl) {
        radioURL = radiourl;
        stopRadio();
        attachToPlaybackService();
    }

    protected void listen() {
        if (player != null) {
            try {
                player.listen(radioURL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRadio() {
        if (player != null) {
            player.stop();
            Constants.APP_CONTEXT.stopService(serviceIntent);
            player.unregisterReceiver(broadcastReceiver);
            player = null;
            connection = null;
        }
    }

    public void playRadio() {
        if (player != null) {
            player.unMute();

        }
    }

    public void pauseRadio() {
        if (player != null) {
            player.mute();
        }
    }

}
