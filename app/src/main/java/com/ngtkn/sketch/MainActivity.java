package com.ngtkn.sketch;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import net.margaritov.preference.colorpicker.ColorPickerDialog;
import android.widget.Toast;

import com.ngtkn.sketch.view.SketchView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private SketchView sketchView;
    private AlertDialog.Builder currentAlertDialog;
    private ImageView widthImageView;
    private AlertDialog dialogLineWidth;
    private ColorPickerDialog pickerDialog;
    private int colorPicked;

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
                sketchView.saveToInternalStorage();
                break;
            case R.id.colorId:
                pickColor();
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
        final SeekBar widthSeekbar = view.findViewById(R.id.seekBarWidth);
        Button widthButton = view.findViewById(R.id.buttonWidthDialog);
        widthImageView = view.findViewById(R.id.imageViewId);

        widthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sketchView.setLineWidth(widthSeekbar.getProgress());
                dialogLineWidth.dismiss();
                currentAlertDialog = null;
            }
        });

        widthSeekbar.setOnSeekBarChangeListener(widthSeekBarChanged);
        widthSeekbar.setProgress(sketchView.getLineWidth());

        currentAlertDialog.setView(view);
        dialogLineWidth = currentAlertDialog.create();
        dialogLineWidth.setTitle("Set Line Width");
        dialogLineWidth.show();
    }

    // Change listener for seekbar
    private SeekBar.OnSeekBarChangeListener widthSeekBarChanged = new SeekBar.OnSeekBarChangeListener() {
        Bitmap bitmap = Bitmap.createBitmap(400, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            Paint p = new Paint();
            p.setColor(sketchView.getDrawingColor());
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setStrokeWidth(i);

            bitmap.eraseColor(Color.WHITE);
            canvas.drawLine(30, 50, 370, 50, p);
            widthImageView.setImageBitmap(bitmap);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    // change line color dialog
    private void pickColor(){
        colorPicked = sketchView.getDrawingColor();
        pickerDialog = new ColorPickerDialog(MainActivity.this, colorPicked);
        pickerDialog.setAlphaSliderVisible(true);
        pickerDialog.setTitle("Color Picker");

        pickerDialog.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
               sketchView.setDrawingColor(color);
            }
        });

        pickerDialog.show();
    }

    // TODO: implement method to reopen file
    // open file
    public void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(intent, 123);
    }
    // FIXME: Failure delivering result, java.lang.IllegalStateException
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if(requestCode == 123 && resultCode == Activity.RESULT_OK) {
            if(data != null) {
                Uri uri = data.getData();
                InputStream is = null;
                try {
                    is = getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(null != bitmap) {
                    sketchView.loadImageFromStorage(bitmap);
                }
                Toast.makeText(this, "uri " + uri, Toast.LENGTH_LONG).show();
            }
        }
    }
}
