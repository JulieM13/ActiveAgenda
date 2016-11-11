package com.example.android.activeagenda;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

public class NewTagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tag);

        /* Color Picker code from: http://stackoverflow.com/questions/6980906/android-color-picker */
        int defaultColorR = 0;
        int defaultColorG = 0;
        int defaultColorB = 0;
        final ColorPicker cp = new ColorPicker(NewTagActivity.this, defaultColorR, defaultColorG, defaultColorB);

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

                        /* You can get single channel (value 0-255) */
                        int selectedColorR = cp.getRed();
                        int selectedColorG = cp.getGreen();
                        int selectedColorB = cp.getBlue();

                        /* Or the android RGB Color (see the android Color class reference) */
                        int selectedColorRGB = cp.getColor();

                        // TODO: Do some stuff with the color values here
                        System.out.println("Color chosen: red " + selectedColorR + ", green " + selectedColorG + ", blue " + selectedColorB);
                        System.out.println("Selected color in one call: " + selectedColorRGB);

                        cp.dismiss();
                    }
                });
            }
        });




    }
}
