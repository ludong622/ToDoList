package comp5216.sydney.edu.au.comp5216hw1;

import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;


public class UsersAdapter extends ArrayAdapter<User> {
    public UsersAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }
        // Lookup view for data population
        TextView itemTitle = (TextView) convertView.findViewById(R.id.itemTitle);
        TextView itemTime = (TextView) convertView.findViewById(R.id.itemTime);
        // Populate the data into the template view using the data object
        itemTitle.setText(user.title);
        itemTime.setText(user.time);
        // Return the completed view to render on screen
        return convertView;
    }
}
