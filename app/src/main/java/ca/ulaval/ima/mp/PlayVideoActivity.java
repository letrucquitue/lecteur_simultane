package ca.ulaval.ima.mp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.IOException;

import ca.ulaval.ima.mp.fragment.ChooseVideoFragment;
import ca.ulaval.ima.mp.fragment.ConnectionFragment;
import ca.ulaval.ima.mp.fragment.PropertiesFragment;
import ca.ulaval.ima.mp.model.HostSendInformations;
import ca.ulaval.ima.mp.model.SelfUser;

public class PlayVideoActivity extends YouTubeBaseActivity {

    private static String GOOGLE_YOUTUBE_API_KEY = "AIzaSyBfdzOTVomBllyKzi3GROReQFtO9PrYGLs";

    private YouTubePlayerView youtube_player_view;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private final String DEFAULT_VIDEO = "Ri7GzCUTC5s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        //IF CLIENT
        Intent intent = getIntent();
        String video_id = intent.getStringExtra("video_id");
        if(video_id != null){
            Context context = getApplicationContext();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor=prefs.edit();
            editor.putString("video_id", video_id);
            editor.commit();
        }

        youtube_player_view = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                //VIDEO ID FROM PREFERENCES
                Context context = getApplicationContext();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                String video_id = prefs.getString("video_id", DEFAULT_VIDEO);
                //on lance la vidéo en plein écran
                youTubePlayer.setFullscreen(true);
                youTubePlayer.loadVideo(video_id);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.e("FAIL","Erreur lors du chargement vidéo");
            }
        };

        //init youtube player
        youtube_player_view.initialize(GOOGLE_YOUTUBE_API_KEY,onInitializedListener);

        //si l'appareil est hote
        if(SelfUser.getIsHost()) {
            try {
                //VIDEO ID FROM PREFERENCES
                Context context = getApplicationContext();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                video_id = prefs.getString("video_id", DEFAULT_VIDEO);
                String msg = "video:" + video_id;
                //envoi video id au client
                SelfUser.mmOutStream.write(msg.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
