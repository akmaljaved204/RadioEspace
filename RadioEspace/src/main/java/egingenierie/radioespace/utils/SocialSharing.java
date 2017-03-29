package egingenierie.radioespace.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.facebook.widget.FacebookDialog;
import com.google.android.gms.plus.PlusShare;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import egingenierie.radioespace.R;
import egingenierie.radioespace.adapter.ShareIntentListAdapter;

public class SocialSharing {

    private String tileOfArticle = "";
    private static SocialSharing socialSharing;

    private String response = "";
    String fileName;
    String from;
    private ProgressDialog progressDialog = null;
    String titleOfArtcle;
    String texttoEmail;
    String urlForImage;

    public static SocialSharing getSocialSharing() {
        if (socialSharing == null)
            socialSharing = new SocialSharing();
        return socialSharing;
    }

    public class AsyncTaskForTwitter extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                tileOfArticle = params[0];
                String subtitle = params[1];
                String urlString = params[2];
                fileName = params[3];
                from = params[4];
                titleOfArtcle = params[5];
                texttoEmail = params[6];

                int count;
                URL url = new URL(urlString);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                File dir = new File(Constants.SDCARD_PATH);
                dir.mkdirs();

                File file = new File(dir, fileName);
                if (!file.exists()) {
                    InputStream input = new BufferedInputStream(
                            url.openStream());
                    OutputStream output = new FileOutputStream(file);
                    long total = 0;
                    byte data[] = new byte[1024];

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();

                }

            } catch (Exception e) {
                from = null;
            }

            return from;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                hideLoading();
                if (from.equals("twitter")) {
                    twitterMenuShare(fileName);
                } else {
                    shareOnTEmail(fileName, titleOfArtcle, texttoEmail);
                }
            } catch (Exception e) {

            }

        }
    }

    Context context;

    public void downloadVideoFile(Context contextL, final String titleOfEmail,
                                  final String title, final String subTitile, final String urlString,
                                  final String fileName, final String from, final String textOfEmail)
            throws Exception {
        try {
            context = contextL;
            /*new AsyncTaskForTwitter().execute(title, subTitile, urlString,
					fileName, from, titleOfEmail, textOfEmail);*/
            urlForImage = urlString;
			/*new AsyncTaskForTwitter().execute(title, subTitile, urlString,
					fileName, from, titleOfEmail, textOfEmail);*/
            tileOfArticle = title;
            this.fileName = fileName;
            this.from = from;
            this.titleOfArtcle = titleOfEmail;
            this.texttoEmail = textOfEmail;

            if (from.equals("twitter")) {
                twitterMenuShare(fileName);
            } else {
                shareOnTEmail(fileName, titleOfArtcle, texttoEmail);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }

    }


    public Uri getLocalBitmapUri(Bitmap bmp, String fileName) {
        Uri bmpUri = null;
        try {
            File file = new File(Constants.ACTIVITY_CONTEXT.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 70, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void twitterMenuShare(final String imageName) {

        try {

            Picasso.with(Constants.ACTIVITY_CONTEXT.getApplicationContext()).load(urlForImage).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
					/*Intent i = new Intent(Intent.ACTION_SEND);
					i.setType("image*//*");
					i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
					Constants.ACTIVITY_CONTEXT.startActivity(Intent.createChooser(i, "Share Image"));*/

                    Intent tweetIntent = new Intent();
                    tweetIntent.setType("image/*");
                    tweetIntent.setAction(Intent.ACTION_SEND);
                    tweetIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, imageName));
                    tweetIntent.putExtra(Intent.EXTRA_TEXT, texttoEmail);
                    tweetIntent.putExtra(Intent.EXTRA_SUBJECT, "JazzRadio");
                    final PackageManager packageManager = Constants.ACTIVITY_CONTEXT
                            .getPackageManager();
                    List<ResolveInfo> list = packageManager.queryIntentActivities(
                            tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    boolean resolved = false;
                    for (ResolveInfo resolveInfo : list) {
                        String p = resolveInfo.activityInfo.packageName;
                        if (p != null && p.startsWith("com.twitter.android")) {// if

                            tweetIntent.setPackage(p);
                            resolved = true;
					/* Constants.ACTIVITY_CONTEXT.startActivity(tweetIntent); */
                        }
                    }
                    if (resolved) {
                        Constants.ACTIVITY_CONTEXT.startActivity(tweetIntent);
                    } else {
                        Toast.makeText(Constants.ACTIVITY_CONTEXT,
                                "Twitter app isn't found", Toast.LENGTH_LONG).show();
                    }
                    texttoEmail = "";


                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });


        } catch (Exception ex) {

        }


    }

    // Share On Email
    public void shareOnTEmail(final String filrName, final String subjectOfEmail,
                              final String texttoEmail2) {

        try {

            Picasso.with(Constants.ACTIVITY_CONTEXT.getApplicationContext()).load(urlForImage).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    String toEmail = "";
                    String subjectEmail = subjectOfEmail;
                    String messageEmail = texttoEmail2 + "\n " + tileOfArticle;

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                            Uri.fromParts("mailto", toEmail, null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subjectEmail);
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                            messageEmail);
                    emailIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, fileName));
                    Constants.ACTIVITY_CONTEXT.startActivity(Intent.createChooser(
                            emailIntent, "Choose an Email client"));

                    tileOfArticle = "";

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Share on Facebook Method
    public void shareOnFacebook(Context context, String titleOfShare, String Title,
                                String SubTitle, String imageUrl, String FileName) {

        try {
            Activity activity = (Activity) context;
            if (FacebookDialog.canPresentShareDialog(
                    Constants.ACTIVITY_CONTEXT,
                    FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {

                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(
                        activity).setName(Title).setLink(imageUrl)
                        .setCaption(titleOfShare).setDescription(FileName)
                        .setPicture(imageUrl).build();

                shareDialog.present();
                // uiHelper.trackPendingDialogCall(shareDialog.present());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void shareOnFacebook(Context context, String titleOfShare, String Title, String SubTitle, String imageUrl, String FileName, String urlOfArticle) {
        try {
            Activity activity = (Activity) context;
            if (FacebookDialog.canPresentShareDialog(
                    context,
                    FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(
                        activity).setName(Title).setLink(urlOfArticle)
                        .setCaption(SubTitle)
                        .setDescription(titleOfShare + Title)
                        .setPicture(imageUrl).build();
                shareDialog.present();
                // uiHelper.trackPendingDialogCall(shareDialog.present());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void shareOnFacebookRadio(Context context, String titleOfShare, String Title,
                                     String SubTitle, String imageUrl, String FileName,
                                     String urlOfArticle) {
        try {
            Activity activity = (Activity) context;
            if (FacebookDialog.canPresentShareDialog(
                    context,
                    FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {

                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(
                        activity).setName(Title).setLink(urlOfArticle)
                        .setCaption(SubTitle)
                        .setDescription(titleOfShare + SubTitle)
                        .setPicture(imageUrl).build();

                shareDialog.present();
                // uiHelper.trackPendingDialogCall(shareDialog.present());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void shareOnGooglePlus(Context context, String fileName, String textToShare) {
        Activity activity = (Activity) context;
        Intent shareIntent = new PlusShare.Builder(Constants.ACTIVITY_CONTEXT)
                .setType("text/plain").setText(textToShare)
                .setContentUrl(Uri.parse(fileName)).getIntent();
        activity.startActivityForResult(shareIntent, 0);
		/*
		 * Intent shareIntent = ShareCompat.IntentBuilder.from(activity)
		 * .setType("image/*") .setS(Uri.parse(fileName)) .getIntent()
		 * .setPackage("com.google.android.apps.plus");
		 * activity.startActivity(shareIntent);
		 */
		/*
		 * Intent shareIntent = ShareCompat.IntentBuilder.from(activity)
		 * .setText("Hello from Google+!") .setType("image/*")
		 * .setStream(Uri.fromFile(new File(Constants.SDCARD_PATH, fileName)))
		 * .getIntent() .setPackage("com.google.android.apps.plus");
		 * activity.startActivity(shareIntent);
		 */

    }

    public void shareonSocialMedia(final Context context, final String artestName, final String songName, final String streamImage) {

        Picasso.with(Constants.ACTIVITY_CONTEXT.getApplicationContext()).load(streamImage).into(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                sharingOnMedia(context, artestName, songName, streamImage, bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                final Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
                sharingOnMedia(context, artestName, songName, streamImage, largeIcon);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    public void shareonSocialMediaArticle(final Context context, final String artestName, final String streamImage, final String urlOfArticle) {

        Picasso.with(Constants.ACTIVITY_CONTEXT.getApplicationContext()).load(streamImage).into(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                sharingOnMediaArticle(context, artestName, bitmap, urlOfArticle);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                final Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
                sharingOnMediaArticle(context, artestName, largeIcon, urlOfArticle);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    private void sharingOnMediaArticle(final Context context, final String articleTitle, final Bitmap bitmap, final String urlOfArticle) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        final Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        sharingIntent.setType("text/plain");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
        List<String> activityNames = new ArrayList<String>();
        for (int i = 0; i < activityList.size(); i++) {
            String name = activityList.get(i).activityInfo.targetActivity;
            if (name == null) {
                name = activityList.get(i).activityInfo.parentActivityName;
            }
            if (name != null) {
                if (name.contains("com.facebook.saved.intentfilter.ExternalSaveActivity")) {
                    activityNames.add("Save To Facebook");
                } else if (name.contains("com.twitter.android.RootDMActivity")) {
                    activityNames.add("Direct Message");
                } else {
                    activityNames.add(activityList.get(i).activityInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
                }
            } else {
                activityNames.add(activityList.get(i).activityInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
            }
        }
        final ShareIntentListAdapter objShareIntentListAdapter = new ShareIntentListAdapter((Activity) context, activityList.toArray(), activityNames);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle("Compartir Via");
        builder.setAdapter(objShareIntentListAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                ResolveInfo info = (ResolveInfo) objShareIntentListAdapter.getItem(item);
                if (info.activityInfo.packageName.contains("mms")
                        || info.activityInfo.packageName.contains("conversation")) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.TEXT", articleTitle + "\n" + context.getResources().getString(R.string.titleString) + "\n" + urlOfArticle + "\n" + "\n" + "");
                    context.startActivity(intent);
                } else if (info.activityInfo.packageName.contains("facebook")) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.TEXT", urlOfArticle);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "ODS");
                    intent.putExtra("android.intent.extra.TEXT", articleTitle + "\n" + context.getResources().getString(R.string.titleString) + "\n" + urlOfArticle + "\n" + "\n" + "");
                    intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, "image"));
                    context.startActivity(intent);
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void sharingPodcastOnMedia(final Context context, final String textToShare) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
        List<String> activityNames = new ArrayList<String>();
        for (int i = 0; i < activityList.size(); i++) {
            String name = activityList.get(i).activityInfo.targetActivity;
            if (name == null) {
                name = activityList.get(i).activityInfo.parentActivityName;
            }
            if (name != null) {
                if (name.contains("com.facebook.saved.intentfilter.ExternalSaveActivity")) {
                    activityNames.add("Save To Facebook");
                } else if (name.contains("com.twitter.android.RootDMActivity")) {
                    activityNames.add("Direct Message");
                } else {
                    activityNames.add(activityList.get(i).activityInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
                }
            } else {
                activityNames.add(activityList.get(i).activityInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
            }
        }
        final ShareIntentListAdapter objShareIntentListAdapter = new ShareIntentListAdapter((Activity) context, activityList.toArray(), activityNames);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle("Compartir Via");
        builder.setAdapter(objShareIntentListAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                ResolveInfo info = (ResolveInfo) objShareIntentListAdapter.getItem(item);
                if (info.activityInfo.packageName.contains("mms")
                        || info.activityInfo.packageName.contains("conversation")) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.TEXT", textToShare);
                    context.startActivity(intent);
                } else if (info.activityInfo.packageName.contains("facebook")) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.TEXT", textToShare);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "ODS");
                    intent.putExtra("android.intent.extra.TEXT", textToShare);
                    context.startActivity(intent);
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void sharingOnMedia(final Context context, final String artestName, final String songName, final String streamImage, final Bitmap bitmap) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        final Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        sharingIntent.setType("text/plain");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
        List<String> activityNames = new ArrayList<String>();
        for (int i = 0; i < activityList.size(); i++) {
            String name = activityList.get(i).activityInfo.targetActivity;
            if (name == null) {
                name = activityList.get(i).activityInfo.parentActivityName;
            }
            if (name != null) {
                if (name.contains("com.facebook.saved.intentfilter.ExternalSaveActivity")) {
                    activityNames.add("Save To Facebook");
                } else if (name.contains("com.twitter.android.RootDMActivity")) {
                    activityNames.add("Direct Message");
                } else {
                    activityNames.add(activityList.get(i).activityInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
                }
            } else {
                activityNames.add(activityList.get(i).activityInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
            }
        }
        final ShareIntentListAdapter objShareIntentListAdapter = new ShareIntentListAdapter((Activity) context, activityList.toArray(), activityNames);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle("Compartir Via");
        builder.setAdapter(objShareIntentListAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                ResolveInfo info = (ResolveInfo) objShareIntentListAdapter.getItem(item);
                if (info.activityInfo.packageName.contains("mms")
                        || info.activityInfo.packageName.contains("conversation")) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.TEXT", context.getResources().getString(R.string.radioTitle) + "\n" + artestName + "\n" + songName + "\n" + context.getResources().getString(R.string.radioDetail) + "\n" + "\n" + "");
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "ODS");
                    intent.putExtra("android.intent.extra.TEXT", context.getResources().getString(R.string.radioTitle) + "\n" + artestName + "\n" + songName + "\n" + context.getResources().getString(R.string.radioDetail) + "\n" + "\n" + "");
                    intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, "image"));
                    context.startActivity(intent);
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showLoading() {
        progressDialog = ProgressDialog.show(context, "",
                "Loading...", true, true);
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

    }

    public void hideLoading() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
        }
    }
}
