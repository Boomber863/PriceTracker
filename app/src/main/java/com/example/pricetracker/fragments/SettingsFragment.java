package com.example.pricetracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.CheckBox;

import com.example.pricetracker.R;
import com.example.pricetracker.components.SettingsModel;
import com.google.gson.Gson;

public class SettingsFragment extends Fragment {

    private CheckBox enableNotifications;
    private Spinner notificationCheckInterval;
    private SettingsModel _settings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadSettings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        this.enableNotifications = view.findViewById(R.id.checkbox_allow_notifications);
        this.notificationCheckInterval = view.findViewById(R.id.spinner);
        this.populateList(this.notificationCheckInterval);

        enableNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _settings.setEnableNotifications(enableNotifications.isChecked());
                saveSettings();
            }
        });
        this.notificationCheckInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String selected = notificationCheckInterval.getSelectedItem().toString();
                _settings.setNotificationInterval(_settings.getMinutesForOption(selected));
                saveSettings();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do nothing
            }

        });

        this.enableNotifications.setChecked(this._settings.areNotificationsEnabled());
        this.notificationCheckInterval.setSelection(this._settings.getCurrentOptionIndex());
        return view;
    }

    private void populateList(Spinner spinner) {
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, this._settings.getOptions());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void loadSettings() {
        SharedPreferences mPrefs = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        String json = mPrefs.getString("settings", null);

        if (json != null) {
            Gson gson = new Gson();
            this._settings = gson.fromJson(json, SettingsModel.class);
        } else {
            this._settings = new SettingsModel(true, 15);
        }
    }

    public void saveSettings(){
        SharedPreferences mPrefs = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(this._settings);

        prefsEditor.putString("settings", json);
        prefsEditor.apply();
    }
}