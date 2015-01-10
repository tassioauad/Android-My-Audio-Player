package com.tassioauad.myaudioplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TássioAuad on 27/12/2014.
 */
public class AudioPlayer implements MediaPlayer.OnCompletionListener {

    private static MediaPlayer mediaPlayer;
    private static int currentTrackPosition;
    private static List<AudioEntity> audioEntityList;
    private static List<AudioPlayerListener> listenerList = new ArrayList<AudioPlayerListener>();
    private static boolean loading = false;

    private static AudioPlayer instance;

    private AudioPlayer() {
    }

    public static AudioPlayer getInstance() {
        if (instance == null) {
            instance = new AudioPlayer();
        }

        return instance;
    }

    public int getCurrentTrackPosition() {
        return currentTrackPosition;
    }

    public void setCurrentTrackPosition(int currentTrackPosition) {
        this.currentTrackPosition = currentTrackPosition;
    }

    public List<AudioEntity> getAudioEntityList() {
        return audioEntityList;
    }

    public static void setAudioEntityList(List<AudioEntity> audioEntityList) {
        AudioPlayer.audioEntityList = audioEntityList;
    }

    public AudioEntity getCurrentAudioEntity() {
        return audioEntityList == null ? null : audioEntityList.get(currentTrackPosition);
    }

    public void pause() {
        mediaPlayer.pause();
        warnAudioPaused();
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public void play(final int position) throws IOException {
        warnAudioLoading(position);
        if (position == 0) {
            warnNoPreviousAudio();
        } else {
            warnThereIsPreviousAudio();
        }
        if (position == audioEntityList.size() - 1) {
            warnNoNextAudio();
        } else {
            warnThereIsNextAudio();
        }
        loading = true;

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {

                try {
                    if (mediaPlayer == null) {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setOnCompletionListener(AudioPlayer.this);
                        currentTrackPosition = position;
                        mediaPlayer.setDataSource(audioEntityList.get(currentTrackPosition).getAudioUrl());
                        mediaPlayer.prepare();
                    } else if (currentTrackPosition != position || mediaPlayer.isPlaying()) {
                        currentTrackPosition = position;
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setOnCompletionListener(AudioPlayer.this);
                        mediaPlayer.setDataSource(audioEntityList.get(currentTrackPosition).getAudioUrl());
                        mediaPlayer.prepare();
                    }

                    mediaPlayer.start();
                } catch (Exception e) {
                    mediaPlayer.stop();
                    return false;
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    warnAudioPlayed();

                } else {
                    warnError(new Exception("Error on loading the audio"));
                }
                loading = false;
            }
        }.execute();

    }

    public void playCurrent() throws IOException {
        play(currentTrackPosition);
    }

    public void playNext() throws IOException {
        play(currentTrackPosition + 1);
    }

    public void playPrevious() throws IOException {
        play(currentTrackPosition - 1);
    }

    public void addListener(AudioPlayerListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        warnAudioCompleted();
    }

    private void warnAudioPlayed() {
        for (AudioPlayerListener listener : listenerList) {
            listener.onPlay(audioEntityList.get(currentTrackPosition));
        }
    }

    private void warnAudioCompleted() {
        for (AudioPlayerListener listener : listenerList) {
            listener.onComplete(audioEntityList.get(currentTrackPosition));
        }
    }

    private void warnError(Exception exception) {
        for (AudioPlayerListener listener : listenerList) {
            listener.onError(audioEntityList.get(currentTrackPosition), exception);
        }
    }

    private void warnAudioLoading(int position) {
        for (AudioPlayerListener listener : listenerList) {
            listener.onLoading(audioEntityList.get(position));
        }
    }

    private void warnAudioPaused() {
        for (AudioPlayerListener listener : listenerList) {
            listener.onPause(audioEntityList.get(currentTrackPosition));
        }
    }

    private void warnNoPreviousAudio() {
        for (AudioPlayerListener listener : listenerList) {
            listener.thereIsNoPreviousAudio();
        }
    }

    private void warnNoNextAudio() {
        for (AudioPlayerListener listener : listenerList) {
            listener.thereIsNoNextAudio();
        }
    }

    private void warnThereIsNextAudio() {
        for (AudioPlayerListener listener : listenerList) {
            listener.thereIsNextAudio();
        }
    }

    private void warnThereIsPreviousAudio() {
        for (AudioPlayerListener listener : listenerList) {
            listener.thereIsPreviousAudio();
        }
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean thereIsNextAudio() {
        if(getAudioEntityList() != null && getCurrentTrackPosition() == getAudioEntityList().size() - 1) {
            return false;
        }

        return true;
    }

    public boolean thereIsPreviousAudio() {
        if(getCurrentTrackPosition() == 0) {
            return false;
        }

        return true;
    }
}
