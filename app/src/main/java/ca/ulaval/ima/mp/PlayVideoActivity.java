package ca.ulaval.ima.mp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
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
import ca.ulaval.ima.mp.fragment.PlayFragment;
import ca.ulaval.ima.mp.fragment.PropertiesFragment;
import ca.ulaval.ima.mp.model.HostSendInformations;
import ca.ulaval.ima.mp.model.SelfUser;

public class PlayVideoActivity extends YouTubeBaseActivity {

    private static String GOOGLE_YOUTUBE_API_KEY = "AIzaSyBfdzOTVomBllyKzi3GROReQFtO9PrYGLs";

    private YouTubePlayerView youtube_player_view;
    private YouTubePlayer.OnInitializedListener onInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        youtube_player_view = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo("Ri7GzCUTC5s");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.e("FAIL","C est casse");
            }
        };

        youtube_player_view.initialize(GOOGLE_YOUTUBE_API_KEY,onInitializedListener);
        String str = "lol";
        try {
            SelfUser.mmOutStream.write(str.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
