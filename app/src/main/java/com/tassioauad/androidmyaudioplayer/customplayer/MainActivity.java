package com.tassioauad.androidmyaudioplayer.customplayer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.tassioauad.androidmyaudioplayer.R;
import com.tassioauad.myaudioplayer.AudioEntity;
import com.tassioauad.myaudioplayer.view.PlayerViewComponent;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private PlayerViewComponent player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player = (PlayerViewComponent) findViewById(R.id.player);

        List<AudioEntity> audioEntityList = new ArrayList<>();
        audioEntityList.add(new AudioEntity() {
            @Override
            public String getImageUrl() {
                return "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcTenAaMaDZOQ8SOeyfwRkETYXqs4q2npb4RCCW2yu1SOWblKFy_";
            }

            @Override
            public String getTitle() {
                return "Tuesday at Mona's";
            }

            @Override
            public String getSubTitle() {
                return "It's my audio";
            }

            @Override
            public String getAudioUrl() {
                return "http://upload.wikimedia.org/wikipedia/commons/1/1d/Demo_chorus.ogg";
            }
        });
        audioEntityList.add(new AudioEntity() {
            @Override
            public String getImageUrl() {
                return "http://mediad.publicbroadcasting.net/p/kcur/files/201309/waitwaitnew.jpg";
            }

            @Override
            public String getTitle() {
                return "NPR Wait Wait";
            }

            @Override
            public String getSubTitle() {
                return "podcast episode n";
            }

            @Override
            public String getAudioUrl() {
                return "http://podcastdownload.npr.org/anon.npr-podcasts/podcast/344098539/372126500/npr_372126500.mp3";
            }
        });
        player.setAudioEntityList(audioEntityList);

        View view = getLayoutInflater().inflate(R.layout.customplayer, null);
        player.setCustomView(view);

        player.show();
    }
}
