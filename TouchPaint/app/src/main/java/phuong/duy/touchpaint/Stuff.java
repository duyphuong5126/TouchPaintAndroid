package phuong.duy.touchpaint;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Phuong on 16/07/2015.
 */
public class Stuff {
    private Paint paint;
    private Path path;
    public Stuff(){
        paint = new Paint();
        path = new Path();
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Paint getPaint() {
        return paint;
    }

    public Path getPath() {
        return path;
    }
}
