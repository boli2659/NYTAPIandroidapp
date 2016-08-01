package com.codepath.nytimesapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.codepath.nytimesapp.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void onApply (View v) {
        Spinner sort = (Spinner) findViewById(R.id.spinnerSort);
        CheckBox foreign = (CheckBox) findViewById(R.id.cbFashion);
        CheckBox sports = (CheckBox) findViewById(R.id.cbSports);
        CheckBox education = (CheckBox) findViewById(R.id.cbEducation);
        CheckBox business = (CheckBox) findViewById(R.id.cbBusiness);
        CheckBox art = (CheckBox) findViewById(R.id.cbArts);
        CheckBox cars = (CheckBox) findViewById(R.id.cbCars);
        CheckBox dining = (CheckBox) findViewById(R.id.cbDining);
        CheckBox tech = (CheckBox) findViewById(R.id.cbTech);
        CheckBox jobs = (CheckBox) findViewById(R.id.cbJobs);
        Spinner sourceSpinner = (Spinner) findViewById(R.id.spinnerSource);
        String sorted = sort.getSelectedItem().toString();
        boolean f = foreign.isChecked();
        boolean s = sports.isChecked();
        boolean e = education.isChecked();
        boolean b = business.isChecked();
        boolean a = art.isChecked();
        boolean c = cars.isChecked();
        boolean d = dining.isChecked();
        boolean t = tech.isChecked();
        boolean j = jobs.isChecked();
        boolean source = sourceSpinner.getSelectedItem().toString().equals("New York Times only");

        Intent i = new Intent();
        i.putExtra("f", f);
        i.putExtra("a", a);
        i.putExtra("s", s);
        i.putExtra("e", e);
        i.putExtra("b", b);
        i.putExtra("c", c);
        i.putExtra("d", d);
        i.putExtra("t", t);
        i.putExtra("j", j);
        i.putExtra("source", source);
        setResult(5, i);
        finish();


    }
}
