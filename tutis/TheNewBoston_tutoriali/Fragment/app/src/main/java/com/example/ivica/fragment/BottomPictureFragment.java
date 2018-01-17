package com.example.ivica.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BottomPictureFragment extends Fragment{


    private static TextView topMemeText;
    private static TextView bottomMemeText;

    //  alt + insers+ onCreateView,prije destroy
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_picture_fragment,container,false);

        topMemeText = (TextView) view.findViewById(R.id.topTextInput);
        bottomMemeText = (TextView) view.findViewById(R.id.bottonTextInput);

        return view;
      //  return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setMemeText(String top, String bottom){
        topMemeText.setText(top);
        bottomMemeText.setText(bottom);
    }

}