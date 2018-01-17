package com.example.ivica.surfaceview;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class topFragment extends Fragment{

/*
    private static EditText topTextInput;
    private static EditText bottomTextInput;

    TopSectionListener activityCommander;

    Bitmap ball;
    float x,y;
    OurView v;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);   -NE KORISTITI
        View view = inflater.inflate(R.layout.top_fragment,container,false);

        topTextInput = (EditText)view.findViewById(R.id.topTextInput);
        bottomTextInput =(EditText)view.findViewById(R.id.bottonTextInput);

        v = new OurView(View view);  // novi objekt
        x = y = 0;
        setContentView(v);


        ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);


        //final Button button = (Button) view.findViewById(R.id.button);

      /*  button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked(v);
            }
        });*/
/*

        // return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }
    // calls this when the botton is clicked
    public void buttonClicked(View v){
        activityCommander.createMeme(topTextInput.getText().toString(),bottomTextInput.getText().toString());
    }


    public class OurView extends SurfaceView implements Runnable{

        Thread t = null;
        SurfaceHolder holder;    //dopusta promjenu prikaza, pixela itd, manage ove clase
        boolean isItOk = false;

        public OurView(Context context){
            super(context);
            holder = getHolder();   // set content iz glavne clase, surface
            setWillNotDraw(false);

        }

        @Override
        public void run() {
            while (isItOk == true){ // invalidate method
                // perform canvas drawing
                if (!holder.getSurface().isValid()){ // provjera surface i ako nije dostupan
                    continue;
                }
                Canvas c = holder.lockCanvas();
                //  c.restore();
                //c.drawARGB(255,0,0,0);
                // c.getDrawFilter();
                c.drawColor(Color.BLACK);
                // invalidate();
                c.drawBitmap(ball,x - (ball.getWidth()/2) ,y - (ball.getHeight()/2),null);  // centriranje
                holder.unlockCanvasAndPost(c);
                // invalidate();

                x++;
                y++;
            }
        }

        public void pause(){
            isItOk = false;
            while(true){
                try {
                    t.join();   // prekida thread
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                break;
            }
            t = null;
        }

        public void resume(){
            isItOk = true;
            t = new Thread(this);   // ova metoda run, jer smo implementirali vec runnable , trazin run metodu!
            t.start();
        }
    }
*/
}
