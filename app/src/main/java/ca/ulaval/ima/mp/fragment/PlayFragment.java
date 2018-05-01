package ca.ulaval.ima.mp.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import ca.ulaval.ima.mp.R;

/**
 * Created by LEOBL on 16/04/2018.
 */

public class PlayFragment extends Fragment{
    private static String GOOGLE_YOUTUBE_API_KEY = "AIzaSyBfdzOTVomBllyKzi3GROReQFtO9PrYGLs";

    private YouTubePlayerFragment youtube_player_fragment;
    private YouTubePlayer youtube_player;
    private YouTubePlayer.OnInitializedListener onInitializedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        //youtube_player_fragment = (YouTubePlayerFragment) getActivity().getSupportFragmentManager()
        //        .findFragmentById(R.id.youtube_player_fragment);

        youtube_player_fragment.initialize(GOOGLE_YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {
                if (!wasRestored) {
                    youtube_player = player;

                    //set the player style default
                    youtube_player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                    //AH
                    youtube_player.cueVideo("Ri7GzCUTC5s");
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {

                //print or show error if initialization failed
                Log.e("FAIL", "Youtube Player View initialization failed");
            }
        });

        return view;
    }
}
