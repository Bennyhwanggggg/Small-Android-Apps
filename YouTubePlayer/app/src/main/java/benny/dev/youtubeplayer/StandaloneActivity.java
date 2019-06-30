package benny.dev.youtubeplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeStandalonePlayer;

public class StandaloneActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standalone);
        Button btnPlayVideo = (Button) findViewById(R.id.btnPlayVideo);
        Button btnPlayList = (Button) findViewById(R.id.btnPlayList);

        btnPlayVideo.setOnClickListener(this);
        btnPlayList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()){
            case R.id.btnPlayVideo:
                intent = YouTubeStandalonePlayer.createVideoIntent(this, YoutubeActivity.GOOGLE_API_KEY, YoutubeActivity.YOUTUTBE_VIDEO_ID, 0, true, false); // start time, autoplay, lightbox
                break;
            case R.id.btnPlayList:
                intent = YouTubeStandalonePlayer.createPlaylistIntent(this, YoutubeActivity.GOOGLE_API_KEY, YoutubeActivity.YOUTUBE_PLAYLIST, 0, 0, true, true); // start video, start time, autoplay, lightbox. see doc on web...
                break;
        }
        if(intent != null){
            startActivity(intent);
        }
    }


}
