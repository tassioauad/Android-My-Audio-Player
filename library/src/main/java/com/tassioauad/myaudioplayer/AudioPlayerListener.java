package com.tassioauad.myaudioplayer;

/**
 * Created by TássioAuad on 27/12/2014.
 */
public interface AudioPlayerListener {

    public void onPlay(AudioEntity audioEntity);

    public void onPause(AudioEntity audioEntity);

    public void onError(AudioEntity audioEntity, Exception exception);

    public void onComplete(AudioEntity audioEntity);

    public void onLoading(AudioEntity audioEntity);

    public void thereIsNoNextAudio();

    public void thereIsNoPreviousAudio();

    public void thereIsPreviousAudio();

    public void thereIsNextAudio();
}
