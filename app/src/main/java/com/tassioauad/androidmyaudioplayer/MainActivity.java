package com.tassioauad.androidmyaudioplayer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tassioauad.library.AudioEntity;
import com.tassioauad.library.view.PlayerViewComponent;

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

        player.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
