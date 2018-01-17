package com.example.ivica.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class TopSectionFragment extends Fragment{

    private static EditText topTextInput;
    private static EditText bottomTextInput;

    TopSectionListener activityCommander;


    //  kako bi pricali s top section moramo ovo implementirati

    public interface TopSectionListener{
        void createMeme( String top, String bottom);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            activityCommander = (TopSectionListener) context;  // tu ga kreira
        }   catch (ClassCastException e){
            throw new ClassCastException(context.toString());
        }
    }


    //  alt + insers+ onCreateView,prije destroy
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);   -NE KORISTITI
        View view = inflater.inflate(R.layout.top_section_fragment,container,false);

        topTextInput = (EditText)view.findViewById(R.id.topTextInput);
        bottomTextInput =(EditText)view.findViewById(R.id.bottonTextInput);
        final Button button = (Button) view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked(v);
            }
        });


       // return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    // calls this when the botton is clicked
    public void buttonClicked(View v){
        activityCommander.createMeme(topTextInput.getText().toString(),bottomTextInput.getText().toString());
    }
}
