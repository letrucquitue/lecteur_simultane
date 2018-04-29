package ca.ulaval.ima.mp.model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Classe qui gère l'utilisateur courant. Les autres users sont considérés comme des devices et non des users
 * Si on est host : On trouve la liste des devices connectés
 *                  On peut choisir une vidéo
 *                  On peut ajouter et supprimer des appareils
 * Si on est client : On peut recevoir des vidéos et trouver son host
 *
 * Les appareils serveurs sont fait dans les classes ClientGetInformations et HostSendInformations
 * Mais on traite les résultats ici
 */

public class SelfUser {
    public static BluetoothDevices mConnectedTo; // L'host du client (si client)
    public static boolean mIsHost; //Si on est un host a ce moment ou un invité. On peut switcher entre les deux à tout moment
    public static ArrayList<BluetoothDevices> mConnectedDevices; //Les appareils connetés à nous (Si hote)
    public static VideoModel mVideo; //La vidéo courante

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

    //Set notre nouvel host (Si client)
    public static void setmHost(BluetoothDevices device){
        if (!SelfUser.getIsHost()){
            mConnectedTo = device;
        }
        else{
            Log.d("Erreur", "Tentative dajouter un host lorsqu'on est soi-meme un host");
    }
    }

    //Ajoute un appareils(Si hote)
    public static ArrayList<BluetoothDevices> addConnectedDevices(BluetoothDevices device){
        if (mConnectedDevices == null)
            mConnectedDevices = new ArrayList<BluetoothDevices>();
        mConnectedDevices.add(device);
        return mConnectedDevices;
    }

    //Delete un appareil(Si hote)
    public static ArrayList<BluetoothDevices> removeConnectedDevices(BluetoothDevices device){
        mConnectedDevices.remove(device);
        return mConnectedDevices;
    }

    //Print simplement les appreils
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
