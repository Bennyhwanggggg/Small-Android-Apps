package android.bennydev.appstoretop10;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FeedAdaptor extends ArrayAdapter {
    public static final String TAG = "FeedAdaptor";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<FeedEntry> applications;

    public FeedAdaptor(@NonNull Context context, int resource, List<FeedEntry> applications) {
        super(context, resource);
        this.applications = applications;
        this.layoutInflater = LayoutInflater.from(context);
        this.layoutResource = resource;
    }

    @Override
    public int getCount() {
        return applications.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FeedEntry currentApp = applications.get(position);

        viewHolder.tvName.setText(currentApp.getName());
        viewHolder.tvArtist.setText(currentApp.getArtist());
        viewHolder.tvSummary.setText(currentApp.getSummary());
        return convertView;
    }

    private class ViewHolder{
        final TextView tvName;
        final TextView tvArtist;
        final TextView tvSummary;

        ViewHolder(View v){
            this.tvName = v.findViewById(R.id.tvName);
            this.tvArtist = v.findViewById(R.id.tvArtist);
            this.tvSummary = v.findViewById(R.id.tvSummary);
        }
    }
}
