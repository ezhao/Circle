package com.herokuapp.ezhao.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class CircleDrawingView extends View {
    private Paint paint;
    private Path path;
    private ArrayList<Point> points;

    public CircleDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (path != null) {
            canvas.drawPath(path, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path = new Path();
                points = new ArrayList<>();

                path.moveTo(pointX, pointY);
                points.add(new Point(Math.round(pointX), Math.round(pointY)));

                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX, pointY);
                points.add(new Point(Math.round(pointX), Math.round(pointY)));
                break;
            case MotionEvent.ACTION_UP:
                path.lineTo(pointX, pointY);
                points.add(new Point(Math.round(pointX), Math.round(pointY)));

                // Close the loop, TODO(emily) consider removing
                if (points.size() > 2) {
                    Point firstPoint = points.get(0);
                    path.lineTo(firstPoint.x, firstPoint.y);
                }
                break;
            default:
                return true;
        }

        postInvalidate();
        return true;
    }
}
