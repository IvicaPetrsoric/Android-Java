package hr.rma.sl.mydbexample;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Sandi on 14.4.2016..
 */
public class TrainingsListAdapter extends ArrayAdapter<TrainingsTrack> {

    private Context context;
    List<TrainingsTrack> trainings;
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");


    public TrainingsListAdapter(Context context, List<TrainingsTrack> trainings) {
        super(context, R.layout.list_item_layout_2, trainings);
        this.context = context;
        this.trainings = trainings;
    }


    private class ViewHolder {
        TextView date_txt;
        TextView time_txt;
        TextView players_txt;
    }


    @Override
    public int getCount() {
        return trainings.size();
    }


    @Override
    public TrainingsTrack getItem(int position) {
        return trainings.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_layout_2, null);

            holder = new ViewHolder();
            //holder.id_txt = (TextView) convertView.findViewById(R.id.txt_id);
            holder.date_txt = (TextView) convertView.findViewById(R.id.date_view);
            holder.time_txt = (TextView) convertView.findViewById(R.id.time_view);
            holder.players_txt = (TextView) convertView.findViewById(R.id.players_view);
            //holder.date_text = (TextView) convertView.findViewById(R.id.txt_dateofbirth);
            //holder.mark_text = (TextView) convertView.findViewById(R.id.txt_mark);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TrainingsTrack mTrack = (TrainingsTrack) getItem(position);
        //holder.id_txt.setText(mStudent.getId() + "");
        holder.date_txt.setText(dateFormatter.format(mTrack.getDate()));
        holder.time_txt.setText(mTrack.getTtime());
        holder.players_txt.setText(mTrack.getPlayers());
        //holder.mark_text.setText("RMA grade: " + mStudent.getRmaMark() + "");
        //holder.date_text.setText(dateFormatter.format(mStudent.getDateOfBirth()));

        return convertView;
    }


    @Override
    public void add(TrainingsTrack inTrainings) {
        trainings.add(inTrainings);
        notifyDataSetChanged();
        super.add(inTrainings);
    }

    @Override
    public void remove(TrainingsTrack inTrainings) {
        trainings.remove(inTrainings);
        notifyDataSetChanged();
        super.remove(inTrainings);
    }
}
