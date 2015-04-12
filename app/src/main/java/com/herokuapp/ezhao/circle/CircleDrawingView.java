package com.herokuapp.ezhao.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class CircleDrawingView extends View {
    private Paint paint;
    private Paint guidePaint;
    private Path path;
    private ArrayList<Point> points;
    private float centerX;
    private float centerY;
    private float radius;

    public CircleDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        guidePaint = new Paint();
        guidePaint.setAntiAlias(true);
        guidePaint.setStrokeWidth(1);
        guidePaint.setStyle(Paint.Style.STROKE);
        guidePaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (path != null) {
            canvas.drawPath(path, paint);
        }
        if (radius > 0) {
            canvas.drawCircle(centerX, centerY, radius, guidePaint);
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

                radius = 0;
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX, pointY);
                points.add(new Point(Math.round(pointX), Math.round(pointY)));
                break;
            case MotionEvent.ACTION_UP:
                path.lineTo(pointX, pointY);
                points.add(new Point(Math.round(pointX), Math.round(pointY)));

                calculateCircle();
                break;
            default:
                return true;
        }

        postInvalidate();
        return true;
    }

    private void calculateCircle() {
        int totalX = 0;
        int totalY = 0;
        int totalArea = 0;
        Point point, nextPoint;
        int x, nextX, y, nextY;
        for (int i = 0; i < points.size()-1; i++) {
            point = points.get(i);
            x = point.x;
            y = point.y;

            nextPoint = points.get(i+1);
            nextX = nextPoint.x;
            nextY = nextPoint.y;

            totalX += (x + nextX) * (x * nextY - nextX * y);
            totalY += (y + nextY) * (x * nextY - nextX * y);

            totalArea += (x * nextY - nextX * y);
        }
        float area = 1.0f * totalArea / 2;
        centerX = 1.0f / 6.0f * totalX / area;
        centerY = 1.0f / 6.0f * totalY / area;

        float totalDistance = 0;
        for (int i = 0; i < points.size()-1; i++) {
            point = points.get(i);
            totalDistance += Math.sqrt(Math.pow((point.x - centerX), 2) + Math.pow((point.y - centerY), 2));
        }
        radius = totalDistance/points.size();
    }
}
