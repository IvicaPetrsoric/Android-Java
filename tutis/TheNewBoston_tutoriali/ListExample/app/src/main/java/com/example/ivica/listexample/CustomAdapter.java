package com.example.ivica.listexample;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String>{

    // alt + intest, array adaper
    public CustomAdapter(Context context, String[] foods) {   // context- informacija,m pozadinska
        super(context, R.layout.custom_row,foods);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // inflate, prepare for rendering
        LayoutInflater mojInflater = LayoutInflater.from(getContext());
        View customView = mojInflater.inflate(R.layout.custom_row,parent,false);

        String singleFoodItem = getItem(position);
        TextView mojText = (TextView) customView.findViewById(R.id.textView_tekst);
        ImageView mojaSlika = (ImageView) customView.findViewById(R.id.imageView);

        mojText.setText(singleFoodItem);
        mojaSlika.setImageResource(R.drawable.progres_1);

        return customView;

    }
}
