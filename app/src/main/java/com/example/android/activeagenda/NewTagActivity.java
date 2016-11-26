package com.example.android.activeagenda;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

public class NewTagActivity extends MenuBarActivity {

    private int redValue = 0;
    private int greenValue = 0;
    private int blueValue = 0;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tag);

        dbHelper = new DBHelper(this);

        /* Color Picker code from: http://stackoverflow.com/questions/6980906/android-color-picker */
        final ColorPicker cp = new ColorPicker(NewTagActivity.this, redValue, greenValue, blueValue);

        /* OnClickListener for the choose color button */
        Button chooseColorBtn = (Button) findViewById(R.id.new_tag_choose_color_btn);
        chooseColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cp.show();

                /* OnClickListener for the color picker dialog */
                Button selectColorBtn = (Button)cp.findViewById(R.id.okColorButton);
                selectColorBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /* Get the values for each color (value 0-255) */
                        redValue = cp.getRed();
                        greenValue = cp.getGreen();
                        blueValue = cp.getBlue();
                        System.out.println("Color chosen: red " + redValue + ", green " + greenValue + ", blue " + blueValue);

                        cp.dismiss();
                    }
                });
            }
        });

        Button createNewTagBtn = (Button) findViewById(R.id.new_tag_create_tag_btn);
        createNewTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText tagNameET = (EditText) findViewById(R.id.new_tag_tag_name_et);
                String tagName = tagNameET.getText().toString();
                int color = Color.rgb(redValue, greenValue, blueValue);

                dbHelper.addTag(tagName, color);

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


    }
}
