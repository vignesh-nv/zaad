package com.zaad.zaad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zaad.zaad.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OnlineShopFilterAdapter extends ArrayAdapter<String> {

    private List<String> names;
    private List<Boolean> selections;

    public OnlineShopFilterAdapter(Context context, List<String> names) {
        super(context, 0, names);
        this.names = names;
        this.selections = new ArrayList<>(Collections.nCopies(names.size(), false));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.filter_item, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.name_text_view);
        CheckBox checkBox = convertView.findViewById(R.id.check_box);

        nameTextView.setText(names.get(position));
        checkBox.setChecked(selections.get(position));

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> selections.set(position, isChecked));

        return convertView;
    }

    public List<String> getSelectedNames() {
        List<String> selectedNames = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            if (selections.get(i)) {
                selectedNames.add(names.get(i));
            }
        }
        return selectedNames;
    }
}

