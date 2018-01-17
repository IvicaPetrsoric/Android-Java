package com.example.ivica.ClumsyBox.Worlds_lvl;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.ivica.ClumsyBox.R;

public class Worlds_players_adapter extends ArrayAdapter<String> {

    //public static final String TAG = "com.example.ivica.smartclicker";

    boolean kupljenPopcorn_1;

    public Worlds_players_adapter(Context context, String[] brojIgraca, boolean Popcorn_1) {
        super(context, R.layout.list_item_wolrds_players, brojIgraca);
        this.kupljenPopcorn_1 = Popcorn_1;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mojInflater = LayoutInflater.from(getContext());
        View customView = mojInflater.inflate(R.layout.list_item_wolrds_players, parent, false);

        ImageView mojaSlika = (ImageView) customView.findViewById(R.id.slikaIgraca);

        switch (position){

            case 0:
                mojaSlika.setImageResource(R.drawable.kutija_igrac_c);
                break;

            case 1:
                mojaSlika.setImageResource(R.drawable.kutija_igrac_p);
                break;

            case 2:
                mojaSlika.setImageResource(R.drawable.kutija_igrac_z);
                break;

            case 3:
                if (kupljenPopcorn_1){
                    mojaSlika.setImageResource(R.drawable.kokica_1);
                }
                else{
                    mojaSlika.setImageResource(R.drawable.zakljucan_igrac);
                }

                break;

            /*
            case 4:
                mojaSlika.setImageResource(R.drawable.kokica_2);
                break;
                */

            default:
                mojaSlika.setImageResource(R.drawable.zakljucan_igrac);
                break;
        }

        return customView;
    }
}
