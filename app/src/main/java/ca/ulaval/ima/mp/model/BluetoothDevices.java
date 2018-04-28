package ca.ulaval.ima.mp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Meh on 2018-04-25.
 */

public class BluetoothDevices {
    private String mNom;
    private String mAdresse;
    public static final List<BluetoothDevices.BluetoothItem> ITEMS = new ArrayList<>();
    public static final Map<String, BluetoothItem> ITEM_MAP = new HashMap<String, BluetoothItem>();
    public static final ArrayList<BluetoothDevices> bluetoothsDevices = new ArrayList<>();

    public BluetoothDevices(String pNom, String pAdresse){
        this.mNom = pNom;
        this.mAdresse = pAdresse;
    }


    public String getNom(){
        return mNom;
    }
    public String getAdresse(){
        return mAdresse;
    }
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
