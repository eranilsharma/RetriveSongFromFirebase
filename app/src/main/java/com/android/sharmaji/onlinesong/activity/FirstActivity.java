package com.android.sharmaji.onlinesong.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.sharmaji.onlinesong.R;
import com.android.sharmaji.onlinesong.holder.SongViewHolder;
import com.android.sharmaji.onlinesong.model.Song;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirstActivity extends AppCompatActivity {
    long queueid;
    DownloadManager downloadManager;
    RecyclerView recyclerView;
    public DatabaseReference databaseReference, databaseReferenceLike;
    private static final int REQUEST_WRITE_PERMISSION = 123;
    private String songName, downloadurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        recyclerView = (RecyclerView) findViewById(R.id.recyelerViewActivity);
        recyclerView.setHasFixedSize(false);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(FirstActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference("Songs");
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(queueid);

                    Cursor cursor = downloadManager.query(query);
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                            String urlString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            Uri uri = Uri.parse(urlString);
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("audio/*");
                            share.putExtra(Intent.EXTRA_STREAM, uri);
                            startActivity(Intent.createChooser(share, "Share Sound File"));
                        }
                    }
                }
            }
        };
       registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    @Override
    public void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<Song, SongViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Song, SongViewHolder>(
                Song.class,
                R.layout.singlemusiclayout,
                SongViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(final SongViewHolder viewHolder, final Song model, final int position) {
                viewHolder.setSongName(model.getMusicName());
                viewHolder.setArtistName(model.getArtistName());
                viewHolder.songview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.progressBar.setVisibility(View.VISIBLE);
                        viewHolder.btnShare.setVisibility(View.GONE);
                        try {
                            viewHolder.playAudio(model.getSongurl());
                            viewHolder.progressBar.setVisibility(View.GONE);
                            viewHolder.btnShare.setVisibility(View.VISIBLE);
                            viewHolder.relativeLayout.setBackgroundColor(getResources().getColor(R.color.grey_800));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                viewHolder.btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadurl = model.getSongurl();
                        songName = model.getMusicName();
                        Toast.makeText(FirstActivity.this, songName + ": is shared, Please Wait...", Toast.LENGTH_LONG).show();
                        requestPermission();
                    }
                });
                viewHolder.btnLikes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        databaseReferenceLike = databaseReference.child(model.getSongUID()).child("Likes");
                        databaseReferenceLike.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    viewHolder.btnLikes.setBackgroundResource(R.drawable.ic_star_gold_24dp);
                                    Long totalLikes = (Long) dataSnapshot.getValue();
                                    databaseReferenceLike.setValue(totalLikes + 1);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }


        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SongViewHolder.killMediaPlayer();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            downloadFile();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            downloadFile();
        }
    }

    public void downloadFile() {
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadurl));
        queueid = downloadManager.enqueue(request);

    }
}