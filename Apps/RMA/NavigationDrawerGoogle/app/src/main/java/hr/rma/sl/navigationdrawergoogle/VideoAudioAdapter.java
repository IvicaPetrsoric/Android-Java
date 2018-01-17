package hr.rma.sl.navigationdrawergoogle;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sandi on 21.4.2016..
 */
public class VideoAudioAdapter extends BaseAdapter {

    private Context context;
    ArrayList mediaData;
    int mediaType; // 0 == thumbs/images; 1 == video; 2 == audio

    public VideoAudioAdapter(Context context, ArrayList mediaData, int mediaType) {
        this.context = context;
        this.mediaData = new ArrayList(mediaData);
        this.mediaType = mediaType;
    }


    public int getCount() {
        return mediaData.size();
    }


    public Object getItem(int position) {
        return position;
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout myView;

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(context);
            myView = (RelativeLayout)inflater.inflate(R.layout.list_item_videoaudio, null);
        } else {
            myView = (RelativeLayout)convertView;
        }


        TextView tt1 = (TextView) myView.findViewById(R.id.txt_first);
        TextView tt2 = (TextView) myView.findViewById(R.id.txt_second);
        TextView tt3 = (TextView) myView.findViewById(R.id.txt_third);

        String[] mediaInfo = (String[]) mediaData.get(position);

        if (mediaType == 1) {   // VIDEOS
            if (tt1 != null) {
                tt1.setText(mediaInfo[0]);
            }

            if (tt2 != null) {
                String filesize = mediaInfo[2];
                long fileSizeLong = Long.valueOf(filesize);

                String dateTaken = mediaInfo[1];
                long dateTakenLong = Long.valueOf(dateTaken);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String dateTakenString = formatter.format(new Date(dateTakenLong));
                tt2.setText("Taken: " + dateTakenString + "; Size: " +
                        Formatter.formatShortFileSize(context, fileSizeLong));
            }

            if (tt3 != null) {
                String duration = mediaInfo[4];
                long durationLong = Long.valueOf(duration);
                SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
                String durationString = formatter.format(new Date(durationLong));
                tt3.setText(mediaInfo[3] + " [" + durationString + "]");
            }

            return myView;

        } else if (mediaType == 2) {   // AUDIOS
            if (tt1 != null) {
                // title
                tt1.setText(mediaInfo[0]);
            }

            if (tt2 != null) {
                // duration and artist:
                String duration = mediaInfo[3];
                long durationLong = Long.valueOf(duration);
                SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
                String durationString = formatter.format(new Date(durationLong));

                tt2.setText("[" + durationString + "] " + mediaInfo[1]);
            }

            if (tt3 != null) {
                // size
                String filesize = mediaInfo[2];
                long fileSizeLong = Long.valueOf(filesize);

                tt3.setText("Size: " + Formatter.formatShortFileSize(context, fileSizeLong));
            }

            return myView;
        }

        // shouldn't happen:
        return null;
    }
}
