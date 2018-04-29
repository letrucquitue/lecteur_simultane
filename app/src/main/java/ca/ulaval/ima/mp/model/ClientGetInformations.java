package ca.ulaval.ima.mp.model;

/*
On fait le traitements sur les vidéos ici. Le host envoie les informations par la classe HostSendInformation et le client les recoit ici.
 */

public class ClientGetInformations {

    private boolean mHostAccepted;

    public boolean getHostAccepted(){
        return mHostAccepted;
    }
    public void setHostAccepted(boolean accepted){
        this.mHostAccepted = accepted;
    }

    //Retourne true ou false si lhost refuse linvitation
    public boolean HostAcceptedInvitation(boolean accepted){
        return mHostAccepted = accepted;
    }

    //A chaque intervalle, l'host envoie le timeStamp du video. On set ce temps pour chaque client.
    public void syncVideo(int timeStamp){
        //TODO : Set le video a ce timeStamp;
    }

    public void stopVideo(){
        //TODO : L'host demande de mettre en pause la vidéo
    }

    public void startVideo(){
        //TODO : L'host demande de démarrer la vidéo.
    }

    public void changeVideo(VideoModel video){
        //TODO : Set le nouveau vidéo a jouer
    }

}
