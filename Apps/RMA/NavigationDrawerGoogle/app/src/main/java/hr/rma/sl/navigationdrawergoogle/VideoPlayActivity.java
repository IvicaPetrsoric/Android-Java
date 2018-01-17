package hr.rma.sl.navigationdrawergoogle;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoPlayActivity extends Activity {

    String videoLocation;
    VideoView myVV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        TextView myTV= (TextView)findViewById(R.id.textViewVideoPlay);
        myVV = (VideoView)findViewById(R.id.videoView);

        Intent intent_in = getIntent();
        Bundle myBundle = intent_in.getExtras();

        if(myBundle != null)
        {
            String videoTitle = (String)myBundle.get("VIDEO_DISPLAYNAME");
            videoLocation = (String)myBundle.get("VIDEO_LOCATION");
            myTV.setText("Now playing:\n" + videoTitle);

            // Video location (URI):
            myVV.setVideoURI(Uri.parse(videoLocation));

            // Let's assign player controls:
            MediaController controller = new MediaController(this);
            controller.setAnchorView(myVV);
            controller.setMediaPlayer(myVV);
            myVV.setMediaController(controller);


            myVV.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    finish();
                }
            });

            myVV.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });

            // Start playing:
            myVV.start();
        }

    }
}
