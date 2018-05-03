package ca.ulaval.ima.mp.model;

import android.util.Log;

import java.io.IOException;

/**
Le host envoie les informations par cette classe. La liste des appareils connectés se trouve dans la classe SelfUser. Elle est atteignable grâce a l'appel static.
 */

public class HostSendInformations {

    public void sendVideo() {
        String input = "lol";
        byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
        try {
            SelfUser.mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
        } catch (IOException e) {
            //if you cannot write, close the application
            Log.d("DEBUG BT", "UNABLE TO READ/WRITE " + e.toString());
            Log.d("BT SERVICE", "UNABLE TO READ/WRITE, STOPPING SERVICE");
        }
    }

    public void syncVideo(){
        //TODO : Pour chaque client, envoie le timmestamp du video
    }

    public void sendTimeStamp(int time){
        //TODO : Envoie le timestamp de la video a chaque client
    }

    public void stopVideo(){
        //TODO : Arrete le video pour chaque client
    }

    public void startVideo(){
        //TODO : Démarre le vidéo pour chaque client
    }
}
