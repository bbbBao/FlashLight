package com.example.flashlight;

import androidx.appcompat.app.AppCompatActivity;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;
    Switch flashSwitch;

    CameraManager cameraManager;
    String cameraID;
    EditText actionBox;
    GestureDetector gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashSwitch = (Switch) findViewById(R.id.onOff);
        actionBox = (EditText) findViewById(R.id.action);
        gestureDetector = new GestureDetector(this);

        flashSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                flash(b);
            }
        });

        actionBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    setActionBox();
                    handled = true;
                }
                return handled;
            }
        });

    }

    private void flash(boolean b) {

        try {
            cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
            cameraID = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraID, b);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setActionBox() {
        String input = actionBox.getText().toString();
        if (input.equals("ON") || input.equals("On") || input.equals("on")) {
            flash(true);
            flashSwitch.setChecked(true);
            actionBox.setText("");
            Toast.makeText(this, "Flash Light On", Toast.LENGTH_SHORT).show();
        } else if (input.equals("OFF") || input.equals("Off") || input.equals("off")) {
            flash(false);
            flashSwitch.setChecked(false);
            actionBox.setText("");
            Toast.makeText(this, "Flash Light Off", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float vX, float vY) {
        boolean result = false;
        float diffY = moveEvent.getY() - downEvent.getY();
        float diffX = moveEvent.getX() - downEvent.getX();

        if (Math.abs(diffX) < Math.abs(diffY)) {

            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(vY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    if (flashSwitch.isChecked()) {
                        flash(false);
                        flashSwitch.setChecked(false);
                        Toast.makeText(this, "Swipe Down Flash Light Off", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!flashSwitch.isChecked()){
                        flash(true);
                        flashSwitch.setChecked(true);
                        Toast.makeText(this, "Swipe Up Flash Light On", Toast.LENGTH_SHORT).show();
                    }
                    
                }
                result = true;
            }


        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}