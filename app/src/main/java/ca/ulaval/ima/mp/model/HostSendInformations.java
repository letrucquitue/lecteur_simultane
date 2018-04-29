package ca.ulaval.ima.mp.model;

/**
Le host envoie les informations par cette classe. La liste des appareils connectés se trouve dans la classe SelfUser. Elle est atteignable grâce a l'appel static.
 */

public class HostSendInformations {

    public void sendVideo(BluetoothDevices device){
        //TODO : Envoyer le video au client
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
