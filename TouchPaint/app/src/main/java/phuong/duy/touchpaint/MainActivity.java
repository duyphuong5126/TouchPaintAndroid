package phuong.duy.touchpaint;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity implements SeekBar.OnSeekBarChangeListener,
        View.OnClickListener
{
    private DrawingView drawingView;
    private LinearLayout areaColorView;

    private ArrayList<ImageButton> listFunctionButton;
    private ArrayList<SeekBar> listColorSeekbar;

    private long colorTimeInterval = 0;

    private static final int SELECT_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listFunctionButton = new ArrayList<ImageButton>();
        listColorSeekbar = new ArrayList<SeekBar>();

        drawingView = (DrawingView) findViewById(R.id.DrawArea);

        listFunctionButton.add((ImageButton) findViewById(R.id.buttonLoadImageSDCard));
        listFunctionButton.add((ImageButton) findViewById(R.id.buttonSaveImage));
        listFunctionButton.add((ImageButton) findViewById(R.id.buttonErase));
        listFunctionButton.add((ImageButton) findViewById(R.id.buttonCamera));

        areaColorView = (LinearLayout) findViewById(R.id.ColorShow);

        listColorSeekbar.add((SeekBar) findViewById(R.id.seekbarRed));
        listColorSeekbar.add((SeekBar) findViewById(R.id.seekbarGreen));
        listColorSeekbar.add((SeekBar) findViewById(R.id.seekbarBlue));

        for (SeekBar seekBar:listColorSeekbar){
            seekBar.setOnSeekBarChangeListener(this);
        }

        areaColorView.setBackgroundColor(drawingView.getCurrentPaintColor());

        for(ImageButton imageButton:listFunctionButton){
            imageButton.setOnClickListener(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == SELECT_IMAGE){
            Uri imageUri = data.getData();
            String imagePath = getPath(imageUri);
            imagePath.getBytes();
            drawingView.setBitmap(BitmapFactory.decodeFile(imagePath));
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        drawingView.ActiveEraser(false);
        switch(seekBar.getId()){
            case R.id.seekbarRed:
                drawingView.changeColor(progress, Color.RED);
                break;
            case R.id.seekbarGreen:
                drawingView.changeColor(progress, Color.GREEN);
                break;
            case R.id.seekbarBlue:
                drawingView.changeColor(progress, Color.BLUE);
                break;
        }

        areaColorView.setBackgroundColor(drawingView.getCurrentPaintColor());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Toast.makeText(getApplicationContext(), "Current paint's color: "
                +drawingView.getCurrentPaintColorUtil().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        for (ImageButton imageButton:listFunctionButton){
            imageButton.setBackgroundColor((imageButton.getId()==v.getId())?Color.GRAY:Color.TRANSPARENT);
            colorTimeInterval = System.nanoTime()/1000000;
        }
        drawingView.ActiveEraser(false);
        switch (v.getId()){
            case R.id.buttonLoadImageSDCard:
                Intent intentLoadImageSDCard = new Intent();
                intentLoadImageSDCard.setType("image/*");
                intentLoadImageSDCard.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentLoadImageSDCard, "Select image"), SELECT_IMAGE);
                break;
            case R.id.buttonCamera:
                break;
            case R.id.buttonSaveImage:
                drawingView.saveImageIntoSDCard();
                break;
            case R.id.buttonErase:
                drawingView.ActiveEraser(true);
                break;
        }
    }
}
