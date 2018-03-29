package com.example.android.musicappudacitykaralius005;

import android.content.Context;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<SoundObject> {


    public CustomAdapter(Context context, ArrayList<SoundObject> soundObjects) {

        super(context, 0, soundObjects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        SoundObject pos = getItem(position);

        TextView nameTextView = convertView.findViewById(R.id.Title);
        nameTextView.setText(pos.getmTitle());

        return convertView;
    }
}