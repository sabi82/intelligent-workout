package com.projects.p8.intelligent_workout;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Chronometer;

import java.io.IOException;

public class ServiceManager extends Service {
    private IBinder mBinder = new BinderInterface();
    MediaPlayer player;
    MediaPlayer sound_player;
    int musicpos = 0;
    boolean allow_music;
    boolean allow_sound;
    SharedPreferences sharedpref;

    public ServiceManager() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Service Life","onCreate()");
        player = new MediaPlayer();
        sound_player = new MediaPlayer();

        /*player = new MediaPlayer();
        try {
            AssetFileDescriptor afd = getAssets().openFd("elev_music.mp3");
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
            player.start();*/
        }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Service Life","onStartCommand()");
        sharedpref = getSharedPreferences(getString(R.string.sharedpref), MODE_PRIVATE);


        allow_music= sharedpref.getBoolean(getString(R.string.music_allowance),true);
        allow_sound= sharedpref.getBoolean(getString(R.string.sound_allowance),true);
        player = new MediaPlayer();
        sound_player = new MediaPlayer();
        player.setLooping(true);
        try {
            AssetFileDescriptor afd = getAssets().openFd("pop.mp3");
            sound_player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            sound_player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    public void setAllow_music(boolean allowance){
        Log.e("Service life","setting music allowance");
        System.out.println("Allowance is setting up to " + !allowance);
        allow_music = !allowance;
    }

    public boolean getAllow_music(){
        return allow_music;
    }


    public void setAllow_sound(boolean allowance){
        Log.e("Service life","setting music allowance");
        System.out.println("Allowance is setting up to " + !allowance);
        allow_sound = !allowance;
    }

    public boolean getAllow_sound(){
        return allow_sound;
    }

    public void setNewMusic(String musicname){
        Log.e("Service Life","Setting new music");
        try {
            AssetFileDescriptor afd = getAssets().openFd(musicname);
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            Log.e("Service Life","Music DataSource set");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Log.e("Service Life","Preparing player");
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playMusic(){
        Log.e("Service Life","PlayMusic()");
        player.start();
    }

    public void pauseMusic(){
        Log.e("Service Life","pauseMusic()");
        player.pause();
        musicpos = player.getCurrentPosition();
    }

    public void reloadmusic(){
        Log.e("Service Life","reloadmusic()");
        player.seekTo(musicpos);
        player.start();
    }

    public void stopMusic(){
        Log.e("Service Life","stopMusic()");
        player.release();
    }

    public void playsound(){
        sound_player.start();
    }

    @Override
    public void onDestroy() {
        Log.e("Service Manager","Before destroy");
        super.onDestroy();
        Log.e("Service Manager","Destroyed");
        //player.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v("Binder_Test", "in onBind");
        return mBinder;
    }


@Override
    public boolean onUnbind(Intent intent) {
    Log.v("Binder_Test", "in onUnbind");
    return true;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.e("Service Life","OnRebind");
        super.onRebind(intent);
        Log.e("Service Life","OnRebind Done");
    }

    public class BinderInterface extends Binder

    {
        ServiceManager getService() {
            return ServiceManager.this;
        }
    }
}
