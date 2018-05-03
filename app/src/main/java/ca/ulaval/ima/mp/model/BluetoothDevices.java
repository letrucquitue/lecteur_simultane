package ca.ulaval.ima.mp.model;

import android.bluetooth.BluetoothDevice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe assez simple qui gère des appareils. Chaque fois qu'on trouve un appareil proche, on crée un objet pour cet appareil
 * Effectue des actions semblabes à BluetoothDevice fourni par Android mais beaucoup plus simple
 */

public class BluetoothDevices {
    private String mNom;
    private String mAdresse;
    private BluetoothDevice mDevice;
    public static final List<BluetoothDevices.BluetoothItem> ITEMS = new ArrayList<>();  //Simplement pour les adapter
    public static final Map<String, BluetoothItem> ITEM_MAP = new HashMap<String, BluetoothItem>(); //Simplement pour les adapters
    public static final ArrayList<BluetoothDevices> bluetoothsDevices = new ArrayList<>(); //Liste de tous les appareils, pairés ou non.

    public BluetoothDevices(String pNom, String pAdresse, BluetoothDevice device){
        this.mNom = pNom;
        this.mAdresse = pAdresse;
        this.mDevice = device;
    }


    public String getNom(){
        return mNom;
    }
    public String getAdresse(){
        return mAdresse;
    }
    public BluetoothDevice getDevice(){
        return mDevice;
    }


    //Début de la classe pour les adapteurs

    public static class BluetoothItem {
        public final String id;
        public final String content;
        final String details;

        BluetoothItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        private static void addItem(BluetoothItem item) {
            ITEMS.add(item);
            ITEM_MAP.put(item.id, item);
        }

        private static BluetoothItem createItem(int position, String marque) throws IOException {
            return new BluetoothItem(String.valueOf(position), marque, marque);
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
