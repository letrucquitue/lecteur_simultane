package ca.ulaval.ima.mp.model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Meh on 2018-04-28.
 */

public class SelfUser {
    public static BluetoothDevices mConnectedTo;
    public static boolean mIsHost;
    public static ArrayList<BluetoothDevices> mConnectedDevices;
    public static VideoModel mVideo;

    SelfUser(){
        mIsHost = false;
        mConnectedDevices = new ArrayList<BluetoothDevices>();
    }

    public static BluetoothDevices getHost(){
        return mConnectedTo;
    }
    public static void setIsHost(boolean isHost){
        mIsHost = isHost;
    }
    public static boolean getIsHost(){
        return mIsHost;
    }

    public static void setVideo(VideoModel pVideo){
        mVideo = pVideo;
    }
    public static VideoModel getVideo(){
        return mVideo;
    }
    public static ArrayList<BluetoothDevices> updateConnectedDevicesList(ArrayList<BluetoothDevices> devices){
        mConnectedDevices = devices;
        return mConnectedDevices;
    }

    public static void setmHost(BluetoothDevices device){
        if (!SelfUser.getIsHost()){
            mConnectedTo = device;
        }
        else{
            Log.d("Erreur", "Tentative dajouter un host lorsquon est soi-meme un host");
        }
    }

    public static ArrayList<BluetoothDevices> addConnectedDevices(BluetoothDevices device){
        if (mConnectedDevices == null)
            mConnectedDevices = new ArrayList<BluetoothDevices>();
        mConnectedDevices.add(device);
        return mConnectedDevices;
    }

    public static ArrayList<BluetoothDevices> removeConnectedDevices(BluetoothDevices device){
        mConnectedDevices.remove(device);
        return mConnectedDevices;
    }

    public static void print(){
        String str = "Liste des appareils connecte : " ;
        if (mConnectedDevices != null && mConnectedDevices.size() != 0){
            for (int i = 0 ; i < mConnectedDevices.size() ; i++){
                str += mConnectedDevices.get(i).getNom() + " , ";
            }
        }
        else{
            str ="Aucun appareil connectes";
        }
        Log.d("Appareils connectes", str);
    }



}
