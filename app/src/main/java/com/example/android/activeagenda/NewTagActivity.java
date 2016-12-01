package com.example.android.activeagenda;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

public class NewTagActivity extends MenuBarActivity {

    private int redValue = 0;
    private int greenValue = 0;
    private int blueValue = 0;
    int color;
    long id;
    private DBHelper dbHelper;
    boolean editing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tag);

        dbHelper = new DBHelper(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final View colorRect = findViewById(R.id.colorView);
        EditText tagName = (EditText)findViewById(R.id.new_tag_tag_name_et);
        Button createNewTagBtn = (Button) findViewById(R.id.new_tag_create_tag_btn);

        if (getIntent() != null ) {
            Intent intent = getIntent();
            if (intent.getExtras() != null) {
                editing = true;
                Bundle extras = intent.getExtras();
                colorRect.setBackgroundColor(extras.getInt("COLOR"));
                color = extras.getInt("COLOR");
                //http://stackoverflow.com/questions/4801366/convert-rgb-values-to-integer
                redValue = (color >> 16) & 0xFF;
                greenValue = (color >> 8) & 0xFF;
                blueValue = color & 0xFF;
                tagName.setText(extras.getString("NAME"));
                createNewTagBtn.setText("SAVE");
                id = extras.getLong("ID");
            }
        }

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
                        color = Color.rgb(redValue, greenValue, blueValue);
                        colorRect.setBackgroundColor(color);
                        cp.dismiss();
                    }
                });
            }
        });

        createNewTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText tagNameET = (EditText) findViewById(R.id.new_tag_tag_name_et);
                String tagName = tagNameET.getText().toString();
                if (tagName == null || tagName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a tag name", Toast.LENGTH_SHORT).show();
                    return;
                }
                color = Color.rgb(redValue, greenValue, blueValue);

                if(!editing) {
                    dbHelper.addTag(tagName, color);
                } else {
                    TaskTag tag = new TaskTag(tagName, color);
                    tag.setId(id);
                    dbHelper.updateTag(tag);
                }

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


    }
}
