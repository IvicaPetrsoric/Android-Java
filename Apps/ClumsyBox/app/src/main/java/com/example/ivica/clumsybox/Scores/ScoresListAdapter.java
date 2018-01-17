package com.example.ivica.clumsybox.Scores;
import com.example.ivica.clumsybox.Data.Constants;
import com.example.ivica.clumsybox.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Ivica on 2.5.2017..
 */

public class ScoresListAdapter extends ArrayAdapter<String> {


    private ArrayList<Integer> playerPosition = new ArrayList<>();
    private ArrayList<String> playerFlag = new ArrayList<>();
    private ArrayList<String> playerName = new ArrayList<>();
    private ArrayList<Integer> playerScore = new ArrayList<>();

    private boolean showLocal;

    private int totalCells = 3;

    int rowHeight = (int)((Constants.SCREEN_HEIGHT * 0.5f) / totalCells);

    public ScoresListAdapter(Context context, ArrayList playerPosition, ArrayList playerFlag,
                             ArrayList playerName, ArrayList playerScore, boolean showLocal) {

        super(context, R.layout.activity_scores_list_cell, playerName );
        this.playerPosition = playerPosition;
        this.playerFlag = playerFlag;
        this.playerName = playerName;
        this.playerScore = playerScore;
        this.showLocal = showLocal;

        System.out.println(playerFlag);
    }

    private String prilagodbaStringa(String rijec){
        return rijec.replaceAll("%20"," ");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mojInflater = LayoutInflater.from(getContext());
        View customView = mojInflater.inflate(R.layout.activity_scores_list_cell, parent, false);

        ViewGroup.LayoutParams params = customView.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rowHeight);
        } else {
            params.height = rowHeight;

            if (position == 3){

            }
        }
        customView.setLayoutParams(params);

        String brojacUkupnog = getItem(position);

        TextView playerPosition = (TextView)customView.findViewById(R.id.playerPosition);
        TextView playerFlag = (TextView)customView.findViewById(R.id.playerFlag);

        TextView playerName = (TextView) customView.findViewById(R.id.playerName);
        TextView playerScore = (TextView)customView.findViewById(R.id.playerScore);

        ImageView playerImg = (ImageView) customView.findViewById(R.id.playerImg);

        if (!showLocal){
            if (position == 0){
                playerImg.setImageResource(R.drawable.global_gold);
            }
            else if (position == 1){
                playerImg.setImageResource(R.drawable.global_silver);
            }
            else if (position == 2){
                playerImg.setImageResource(R.drawable.global_bronze);
            }
            else{
                playerImg.setImageResource(R.drawable.local_overall);
            }
        }
        else {

            if (position == 0){
                playerImg.setImageResource(R.drawable.local_gold);
            }
            else if (position == 1){
                playerImg.setImageResource(R.drawable.local_silver);
            }
            else if (position == 2){
                playerImg.setImageResource(R.drawable.local_bronze);
            }
            else{
                playerImg.setImageResource(R.drawable.local_overall);
            }
        }

        //playerFlag.setText("" + this.playerFlag.get(position));
        //System.out.println(localeToEmoji((this.playerFlag.get(position)));
        //playerFlag.setText("" + Locale(localeToEmoji(this.playerFlag.get(position))));
        //StringEscapeUtils.unescapeJava(serverResponse);


        playerName.setText(prilagodbaStringa(brojacUkupnog));

        playerScore.setText("" + this.playerScore.get(position));

        playerPosition.setText("#" + (this.playerPosition.get(position)));

        return customView;
    }

    private String localeToEmoji(Locale locale) {
        String countryCode = locale.getCountry();
        int firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }

}
