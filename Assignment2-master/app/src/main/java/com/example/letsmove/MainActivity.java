package com.example.letsmove;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CheckBox checkBoxSightseeing, checkBoxHiking, checkBoxShopping;
    private RadioGroup radioGroup;
    private RatingBar ratingBar;
    private SeekBar seekBarBudget;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchNotifications;
    private TextView textViewBudget, selectedOptionsText, selectedBudgetText;
    private final ArrayList<String> selectedActivities = new ArrayList<>();
    private final ArrayList<String> summaryResults = new ArrayList<>();
    private AlertDialog.Builder builder;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBoxSightseeing = findViewById(R.id.checkbox_sightseeing);
        checkBoxHiking = findViewById(R.id.checkbox_hiking);
        checkBoxShopping = findViewById(R.id.checkbox_shopping);
        radioGroup = findViewById(R.id.radioGroup);
        findViewById(R.id.radio_budget);
        findViewById(R.id.radio_luxury);
        findViewById(R.id.radio_adventure);
        ratingBar = findViewById(R.id.rating_bar);
        seekBarBudget = findViewById(R.id.seekbar_budget);
        switchNotifications = findViewById(R.id.switch_notifications);
        textViewBudget = findViewById(R.id.textview_budget);
        selectedOptionsText = findViewById(R.id.selected_options_text);
        selectedBudgetText = findViewById(R.id.selected_budget_text);
        Button showSummaryButton = findViewById(R.id.show_summary_button);
        builder = new AlertDialog.Builder(this);

        seekBarBudget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewBudget.setText("Budget: $" + progress);
                selectedBudgetText.setText("Selected Budget: $" + progress);

                if (switchNotifications.isChecked() && progress > 100) {
                    Toast.makeText(MainActivity.this, "Warning: You are exceeding the $100 budget limit!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        checkBoxSightseeing.setOnCheckedChangeListener((buttonView, isChecked) -> updateSelectedActivities());
        checkBoxHiking.setOnCheckedChangeListener((buttonView, isChecked) -> updateSelectedActivities());
        checkBoxShopping.setOnCheckedChangeListener((buttonView, isChecked) -> updateSelectedActivities());

        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> Toast.makeText(this, "Rating: " + rating, Toast.LENGTH_SHORT).show());

        showSummaryButton.setOnClickListener(v -> showSummaryDialog());
    }

    @SuppressLint("SetTextI18n")
    private void updateSelectedActivities() {
        selectedActivities.clear();
        if (checkBoxSightseeing.isChecked()) selectedActivities.add("Sightseeing");
        if (checkBoxHiking.isChecked()) selectedActivities.add("Hiking");
        if (checkBoxShopping.isChecked()) selectedActivities.add("Shopping");
        selectedOptionsText.setText("Selected Activities: " + selectedActivities);
    }

    private void showSummaryDialog() {
        summaryResults.clear();

        if (selectedActivities.isEmpty()) {
            Toast.makeText(this, "Please select at least one activity.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a travel preference.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ratingBar.getRating() == 0) {
            Toast.makeText(this, "Please provide a rating.", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedBudget = seekBarBudget.getProgress();
        summaryResults.add("Activities: " + selectedActivities);
        summaryResults.add("Budget: $" + selectedBudget);
        String radioValue = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
        summaryResults.add("Preference: " + radioValue);
        summaryResults.add("Rating: " + ratingBar.getRating());

        StringBuilder summaryMessage = new StringBuilder("Travel Summary:\n");
        for (String result : summaryResults) {
            summaryMessage.append(result).append("\n");
        }

        builder.setTitle("Travel Summary")
                .setMessage(summaryMessage.toString())
                .setCancelable(false)
                .setPositiveButton("Okay", (dialog, which) -> {
                    dialog.cancel();
                    Toast.makeText(getApplicationContext(), "Summary shown!", Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}
