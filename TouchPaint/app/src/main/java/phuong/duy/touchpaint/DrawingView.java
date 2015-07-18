package phuong.duy.touchpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by Phuong on 15/07/2015.
 */
public class DrawingView extends View {
    private Paint drawBitMapPaint;
    private ArrayList<Stuff> pairDrawers;
    private Bitmap bitmap, cacheBitmap;
    private Path path;
    private Paint paint;
    private Stuff tempStuff;
    private boolean EraseMode = false;

    private Canvas canvasCache;

    private ColorUtil currentPaintColor;
    private float currentWidth = 6f;


    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawBitMapPaint = new Paint();

        pairDrawers = new ArrayList<Stuff>();

        drawBitMapPaint.setColor(Color.RED);


        path = new Path();
        paint = new Paint();
        tempStuff = new Stuff();

        cacheBitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);

        canvasCache = new Canvas(cacheBitmap);

        bitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.defaultbitmap));

        currentPaintColor = new ColorUtil();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(bitmap != null) canvas.drawBitmap(bitmap, 0, 0, null);
        for(Stuff stuff:pairDrawers){
            canvasCache.drawPath(stuff.getPath(), stuff.getPaint());
        }
        canvas.drawBitmap(cacheBitmap, 0, 0, null);
    }

    public void setBitmap(Bitmap temp){
        bitmap = temp;
        invalidate();
    }

    private Bitmap loadImageFromSDCard(String imageDirectory, String imageName, String imageExtension){
        Bitmap tempBitmap;
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath()+imageDirectory);
        File tempFile = new File(directory, imageName+imageExtension);
        FileInputStream streamIn = null;
        try {
            streamIn = new FileInputStream(tempFile);
        } catch (FileNotFoundException e) {
            Log.d("Error", "File error");
        }
        tempBitmap = BitmapFactory.decodeStream(streamIn);
        return tempBitmap;
    }

    public void saveImageIntoSDCard() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                tempStuff = new Stuff();
                paint = new Paint();
                paint = (EraseMode)?createEraser():createPaint();
                path = new Path();
                pairDrawers.add(tempStuff);
                path.moveTo(event.getX(), event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(), event.getY());
                break;
        }
        tempStuff.setPath(path);
        tempStuff.setPaint(paint);
        invalidate();
        return super.onTouchEvent(event);
    }

    private Paint createPaint(){
        Paint tempPaint = new Paint();
        tempPaint.setColor(currentPaintColor.getRGB());
        tempPaint.setStyle(Paint.Style.STROKE);
        tempPaint.setStrokeCap(Paint.Cap.ROUND);
        tempPaint.setStrokeJoin(Paint.Join.ROUND);
        tempPaint.setAntiAlias(true);
        tempPaint.setDither(true);
        tempPaint.setStrokeWidth(currentWidth);
        return tempPaint;
    }

    private Paint createEraser(){
        Log.d("Eraser", "abc");
        Paint eraser = new Paint();
        eraser.setAntiAlias(true);
        eraser.setAlpha(0xFF);
        eraser.setColor(Color.RED);
        eraser.setStyle(Paint.Style.STROKE);
        eraser.setStrokeWidth(currentWidth);
        eraser.setStrokeJoin(Paint.Join.ROUND);
        eraser.setXfermode(null);
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        return eraser;
    }

    public void ActiveEraser(boolean mode){
        EraseMode = mode;
    }

    public int changeColor(int value, int channel){
        switch (channel){
            case Color.RED:
                currentPaintColor.setRedChannel(value);
                break;
            case Color.GREEN:
                currentPaintColor.setGreenChannel(value);
                break;
            case Color.BLUE:
                currentPaintColor.setBlueChannel(value);
                break;
        }
        return currentPaintColor.getRGB();
    }

    public int getCurrentPaintColor(){
        return currentPaintColor.getRGB();
    }

    public ColorUtil getCurrentPaintColorUtil(){
        return this.currentPaintColor;
    }
}
