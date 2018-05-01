package ca.ulaval.ima.mp.listener;


import ca.ulaval.ima.mp.model.VideoModel;

/**
 * Created by LEOBL on 16/04/2018.
 */

public interface OnItemClickListener {
    void onItemClick(VideoModel item);
    void onItemClick(String item);
}
