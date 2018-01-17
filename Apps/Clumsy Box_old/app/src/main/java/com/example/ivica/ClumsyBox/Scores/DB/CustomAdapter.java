package com.example.ivica.ClumsyBox.Scores.DB;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ivica.ClumsyBox.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {

    public static final String TAG = "com.example.ivica.smartclicker";

    ArrayList<Integer> scoreList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();
    boolean saServera;
    String vrijemeUsporedbe;

    public CustomAdapter(Context context, ArrayList name,ArrayList score,ArrayList date, boolean saServera, String vrijeme) {
        super(context, R.layout.list_item_layout,name);
        this.scoreList = score;
        this.dateList = date;
        this.saServera = saServera;
        this.vrijemeUsporedbe = vrijeme;
    }

    private String prilagodbaStringa(String rijec){

        return rijec.replaceAll("%20"," ");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mojInflater = LayoutInflater.from(getContext());
        View customView = mojInflater.inflate(R.layout.list_item_layout,parent,false);

        String brojacUkupnog = getItem(position);

        TextView redniBroj = (TextView)customView.findViewById(R.id.redniBroj);
        TextView ime = (TextView) customView.findViewById(R.id.txt_name);
        TextView score = (TextView)customView.findViewById(R.id.txt_score);
        TextView date = (TextView)customView.findViewById(R.id.txt_dateOfPlaying);
        ImageView mojaSlika = (ImageView) customView.findViewById(R.id.imagePokal);

        if (saServera){
            if (position == 0){
                mojaSlika.setImageResource(R.drawable.server_prvo);
            }
            else if (position == 1){
                mojaSlika.setImageResource(R.drawable.server_drugo);
            }
            else if (position == 2){
                mojaSlika.setImageResource(R.drawable.server_trece);
            }
            else{
                mojaSlika.setImageResource(R.drawable.server_local_all);
            }
        }
        else {

            if (position == 0){
                mojaSlika.setImageResource(R.drawable.local_prvo);
            }
            else if (position == 1){
                mojaSlika.setImageResource(R.drawable.local_drugo);
            }
            else if (position == 2){
                mojaSlika.setImageResource(R.drawable.local_trece);
            }
            else{
                mojaSlika.setImageResource(R.drawable.server_local_all);
            }
        }

        String trenutnoVrijeme = dateList.get(position);

        System.out.println("Trenutno: " + trenutnoVrijeme);
        System.out.println("Za usporedbu: " + vrijemeUsporedbe);
        //Log.i(TAG,"ISTINA");
        if (vrijemeUsporedbe == (trenutnoVrijeme)){
            System.out.println("ISTINA!");
        }

        ime.setText(prilagodbaStringa(brojacUkupnog));
        score.setText(""+scoreList.get(position));
        date.setText(prilagodbaStringa(dateList.get(position)));
        redniBroj.setText("" + (position + 1) +"#");

        return customView;
    }
}
