package com.android.sharmaji.onlinesong.holder;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.sharmaji.onlinesong.R;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class SongViewHolder extends RecyclerView.ViewHolder {
    public View songview;
    public Button btnShare;
    public Button btnLikes;
    public CardView cardView;
    public RelativeLayout relativeLayout;
    public ProgressBar progressBar;
    private static MediaPlayer mediaPlayer;


    public SongViewHolder(View itemView) {
        super(itemView);
        songview = itemView;
        btnLikes = (Button) songview.findViewById(R.id.btnlike);
        btnShare = (Button) songview.findViewById(R.id.btnshare);
        cardView=(CardView)songview.findViewById(R.id.cardview);
        relativeLayout=(RelativeLayout)songview.findViewById(R.id.relativelayout);
        progressBar=(ProgressBar)songview.findViewById(R.id.progressBar);
    }

    public void setSongName(String songName) {
        TextView txtSongName = (TextView) songview.findViewById(R.id.txtsongname);
        txtSongName.setText(songName);
    }

    public void setArtistName(String artistName) {
        TextView txtArtistName = (TextView) songview.findViewById(R.id.txtArtistName);
        txtArtistName.setText(artistName);
    }

    public void playAudio(final String url) throws Exception {
        killMediaPlayer();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    public static void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer=null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
