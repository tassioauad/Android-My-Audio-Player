package com.tassioauad.myaudioplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tassioauad.myaudioplayer.AudioEntity;
import com.tassioauad.myaudioplayer.AudioPlayer;
import com.tassioauad.myaudioplayer.AudioPlayerListener;
import com.tassioauad.myaudioplayer.R;

import java.io.IOException;
import java.util.List;

/**
 * Created by TássioAuad on 27/12/2014.
 */
public class PlayerViewComponent extends LinearLayout {

    //UI Elements
    private ImageView imageViewAudioImage;
    private TextView textViewAudioTitle;
    private TextView textViewAudioSubTitle;
    private ImageButton imageButtonPrevious;
    private ImageButton imageButtonPlayPause;
    private ImageButton imageButtonNext;
    private LinearLayout linearLayoutPlayerButtons;
    private LinearLayout linearLayoutPlayerLoading;
    private static AudioPlayer audioPlayer;
    private static View view;

    private AudioPlayerListener audioPlayerListener = new AudioPlayerListener() {
        @Override
        public void onPlay(AudioEntity audioEntity) {
            imageButtonPlayPause.setImageDrawable(getContext().getResources().getDrawable(android.R.drawable.ic_media_pause));
            linearLayoutPlayerLoading.setVisibility(GONE);
            linearLayoutPlayerButtons.setVisibility(VISIBLE);
        }

        @Override
        public void onPause(AudioEntity audioEntity) {
            imageButtonPlayPause.setImageDrawable(getContext().getResources().getDrawable(android.R.drawable.ic_media_play));
        }

        @Override
        public void onError(AudioEntity audioEntity, Exception exception) {
            Toast.makeText(getContext(), "OPS! " + exception.getMessage(), Toast.LENGTH_LONG).show();
            linearLayoutPlayerLoading.setVisibility(GONE);
            linearLayoutPlayerButtons.setVisibility(VISIBLE);
        }

        @Override
        public void onComplete(AudioEntity audioEntity) {
            imageButtonPlayPause.setImageDrawable(getContext().getResources().getDrawable(android.R.drawable.ic_media_play));
        }

        @Override
        public void onLoading(AudioEntity audioEntity) {
            linearLayoutPlayerButtons.setVisibility(GONE);
            linearLayoutPlayerLoading.setVisibility(VISIBLE);
            textViewAudioTitle.setText(audioEntity.getTitle());
            textViewAudioSubTitle.setText(audioEntity.getSubTitle());
            Picasso.with(getContext()).load(audioEntity.getImageUrl()).into(imageViewAudioImage);

        }

        @Override
        public void thereIsNoNextAudio() {
            imageButtonNext.setAlpha(0.3f);
            imageButtonNext.setClickable(false);
        }

        @Override
        public void thereIsNoPreviousAudio() {
            imageButtonPrevious.setAlpha(0.3f);
            imageButtonPrevious.setClickable(false);
        }

        @Override
        public void thereIsPreviousAudio() {
            imageButtonPrevious.setAlpha(1f);
            imageButtonPrevious.setClickable(true);
        }

        @Override
        public void thereIsNextAudio() {
            imageButtonNext.setAlpha(1f);
            imageButtonNext.setClickable(true);
        }
    };

    public PlayerViewComponent(Context context) {
        super(context);
    }

    public PlayerViewComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerViewComponent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void show() {
        inflateLayout(getContext());
    }

    public void hide() {
        removeAllViews();
    }

    public void setAudioEntityList(List<AudioEntity> audioEntityList) {
        if(audioEntityList != null && audioEntityList.size() > 0) {
            audioPlayer.setAudioEntityList(audioEntityList);
            if(textViewAudioTitle != null) {
                audioPlayerListener.thereIsNoPreviousAudio();
                textViewAudioTitle.setText(audioEntityList.get(0).getTitle());
                textViewAudioSubTitle.setText(audioEntityList.get(0).getSubTitle());
                Picasso.with(getContext()).load(audioEntityList.get(0).getImageUrl()).into(imageViewAudioImage);
            }
        }
    }

    public void addListener(AudioPlayerListener listener) {
        audioPlayer.addListener(listener);
    }

    public void setCustomView(View view) {
        this.view = view;
    }

    public void play(int track) throws IOException {
        audioPlayer.play(track);
    }

    private void inflateLayout(final Context context) {

        audioPlayer = AudioPlayer.getInstance();
        audioPlayer.addListener(audioPlayerListener);

        removeAllViews();

        //Getting UI Elements
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null) {
            view = layoutInflater.inflate(R.layout.playerviewcomponent, null);
        }

        view.setLayoutParams(getLayoutParams());
        imageViewAudioImage = (ImageView) view.findViewById(R.id.audioImage);
        linearLayoutPlayerButtons = (LinearLayout) view.findViewById(R.id.playerButtons);
        linearLayoutPlayerLoading = (LinearLayout) view.findViewById(R.id.playerLoading);
        textViewAudioTitle = (TextView) view.findViewById(R.id.audioTitle);
        textViewAudioSubTitle = (TextView) view.findViewById(R.id.audioSubTitle);
        imageButtonPlayPause = (ImageButton) view.findViewById(R.id.btnPlay);
        imageButtonPrevious = (ImageButton) view.findViewById(R.id.btnPrevious);
        imageButtonNext = (ImageButton) view.findViewById(R.id.btnNext);

        //Showing depending on current state of the player
        AudioEntity currentAudioEntity = audioPlayer.getCurrentAudioEntity();
        if(currentAudioEntity != null) {
            textViewAudioTitle.setText(currentAudioEntity.getTitle());
            textViewAudioSubTitle.setText(currentAudioEntity.getSubTitle());
            Picasso.with(getContext()).load(currentAudioEntity.getImageUrl()).into(imageViewAudioImage);
        }
        if(!audioPlayer.thereIsPreviousAudio()) {
            audioPlayerListener.thereIsNoPreviousAudio();
        }
        if(!audioPlayer.thereIsNextAudio()) {
            audioPlayerListener.thereIsNoNextAudio();
        }
        if(audioPlayer.isLoading()) {
            linearLayoutPlayerLoading.setVisibility(VISIBLE);
            linearLayoutPlayerButtons.setVisibility(GONE);
        } else {
            linearLayoutPlayerLoading.setVisibility(GONE);
            linearLayoutPlayerButtons.setVisibility(VISIBLE);
        }
        if(audioPlayer.isPlaying()) {
            imageButtonPlayPause.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_media_pause));
        } else {
            imageButtonPlayPause.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_media_play));
        }

        //Buttons Action
        imageButtonPrevious.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    audioPlayer.playPrevious();
                } catch (IOException ex) {
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        imageButtonPlayPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (audioPlayer.isPlaying()) {
                        audioPlayer.pause();
                    } else {
                        audioPlayer.playCurrent();
                    }
                } catch (IOException ex) {
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        imageButtonNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    audioPlayer.playNext();
                } catch (IOException ex) {
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        addView(view);
    }
}
