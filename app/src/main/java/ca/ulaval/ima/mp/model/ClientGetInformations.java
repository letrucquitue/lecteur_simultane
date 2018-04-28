package ca.ulaval.ima.mp.model;

/**
 * Created by Meh on 2018-04-28.
 */

public class ClientGetInformations {

    private boolean mHostAccepted;

    public boolean getHostAccepted(){
        return mHostAccepted;
    }
    public void setHostAccepted(boolean accepted){
        this.mHostAccepted = accepted;
    }

    public boolean HostAcceptedInvitation(){
        //TODO : Retourne true ou false si lhost refuse linvitation
        return mHostAccepted;
    }

    public void syncVideo(String timeStamp){
        //TODO : Set le video a ce timeStamp;
    }

}
