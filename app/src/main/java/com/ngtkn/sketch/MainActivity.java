package com.ngtkn.sketch;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.ngtkn.sketch.view.SketchView;

public class MainActivity extends AppCompatActivity {

    private SketchView sketchView;
    private AlertDialog.Builder currentAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sketchView = findViewById(R.id.view);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.clearId:
                sketchView.clear();
                break;
            case R.id.saveId:
                break;
            case R.id.colorId:
                break;
            case R.id.lineWidth:
                showLineWidthDialog();
                break;
            case R.id.erase:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    void showLineWidthDialog() {
        currentAlertDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.width_dialog, null);
        SeekBar widthSeekbar = view.findViewById(R.id.seekBarWidth);
        Button widthButton = view.findViewById(R.id.buttonWidthDialog);

        widthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show();
            }
        });

        currentAlertDialog.setView(view);
        currentAlertDialog.create();
        currentAlertDialog.show();
    }
}
