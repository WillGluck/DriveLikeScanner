package com.brufstudios.drivelikescanner.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.brufstudios.drivelikescanner.R;

import java.util.Arrays;

public class PolygonCanvas extends FrameLayout {

    private ImageView v1;
    private ImageView v12;
    private ImageView v2;
    private ImageView v23;
    private ImageView v3;
    private ImageView v34;
    private ImageView v4;
    private ImageView v41;
    private Paint edges;

    public PolygonCanvas(Context context) {
        super(context);
        config();
    }

    public PolygonCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        config();
    }

    public PolygonCanvas(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        config();
    }

    private void config() {

        v1 = createCornerVertexInPosition(0, 0);
        v2 = createCornerVertexInPosition(0, getHeight());
        v3 = createCornerVertexInPosition(getWidth(), getHeight());
        v4 = createCornerVertexInPosition(getWidth(), 0);

        v12 = createMidVertexInPosition(0, getHeight() / 2, v1, v2);
        v23 = createMidVertexInPosition(getWidth() / 2, getHeight(), v2, v3);
        v34 = createMidVertexInPosition(getWidth(), getHeight() / 2, v3, v4);
        v41 = createMidVertexInPosition(getWidth() / 2, 0, v4, v1);

        for (ImageView vertex : Arrays.asList(v1, v2, v3, v4, v12, v23, v34, v41)) {
            addView(vertex);
        }

        configEdges();
    }

    private void configEdges() {
        edges = new Paint();
        edges.setStrokeWidth(1);
        edges.setAntiAlias(true);
    }

    private ImageView createCornerVertexInPosition(Integer x, Integer y) {
        ImageView imageView = createVertexInPosition(x, y);
        imageView.setOnTouchListener(new CornerVertexTouchListener());
        return imageView;
    }

    private ImageView createMidVertexInPosition(Integer x, Integer y, ImageView cornerVertex1, ImageView cornerVertex2) {
        ImageView imageView = createVertexInPosition(x, y);
        imageView.setOnTouchListener(new MidVertexTouchListener(cornerVertex1, cornerVertex2));
        return imageView;
    }

    private ImageView createVertexInPosition(Integer x, Integer y) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.circle);
        Resources r = getResources();
        Float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
        Float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(width.intValue(), height.intValue()));
        return imageView;
    }

    private void drawLineForVertices(ImageView x, ImageView y, Canvas canvas) {
        canvas.drawLine(x.getX() + x.getWidth() / 2, x.getY() + x.getWidth() / 2,
                        y.getX() + y.getWidth() / 2, y.getY() + y.getWidth() / 2,
                        edges);
    }

    private void updateMidVerticePositionFromCorners(ImageView xy, ImageView x, ImageView y) {
        xy.setX(y.getX() - ((y.getX() - x.getX()) / 2));
        xy.setY(y.getY() - ((y.getY() - x.getY()) / 2));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawLineForVertices(v1, v2, canvas);
        drawLineForVertices(v2, v3, canvas);
        drawLineForVertices(v3, v4, canvas);
        drawLineForVertices(v4, v1, canvas);
        updateMidVerticePositionFromCorners(v12, v1, v2);
        updateMidVerticePositionFromCorners(v23, v2, v3);
        updateMidVerticePositionFromCorners(v34, v3, v4);
        updateMidVerticePositionFromCorners(v41, v4, v1);
    }

    /////////////////////////

    private abstract class AbstractVertexTouchListener implements OnTouchListener {

        protected PointF imageStartPosition = new PointF();
        protected PointF touchStartPosition = new PointF();
        protected PointF movement = new PointF();

        public Boolean isVertexInsideCanvas(View view) {
            return isXInsideCanvas(imageStartPosition.x, view) && isYInsideCanvas(imageStartPosition.y, view);
        }

        public Boolean isXInsideCanvas(Float x, View view) {
            Boolean isMaxWidthOk = x + movement.x + view.getWidth() < PolygonCanvas.this.getWidth();
            Boolean isMinWidthOk = x + movement.x > 0;
            return isMaxWidthOk && isMinWidthOk;
        }

        public Boolean isYInsideCanvas(Float y, View view) {
            Boolean isMaxHeightOk = y + movement.y + view.getWidth() < PolygonCanvas.this.getHeight();
            Boolean isMinHeightOk = y + movement.y > 0;
            return isMaxHeightOk && isMinHeightOk;
        }

        public void onTouchActionDown(View v, MotionEvent event) {
            touchStartPosition.x = event.getX();
            touchStartPosition.y = event.getY();
            imageStartPosition.x = v.getX();
            imageStartPosition.y = v.getY();
        }

        public void onTouchActionUp() {
            //Nada
        }

        public void updateMovement(MotionEvent event) {
            movement.x = event.getX() - touchStartPosition.x;
            movement.y = event.getY() - touchStartPosition.y;
        }

        public abstract void onTouchActionMove(View v, MotionEvent event);

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Integer action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    onTouchActionMove(v, event);
                    break;
                case MotionEvent.ACTION_DOWN:
                    onTouchActionDown(v, event);
                    break;
                case MotionEvent.ACTION_UP:
                    updateMovement(event);
                    onTouchActionUp();
                    imageStartPosition.x = v.getX();
                    imageStartPosition.y = v.getY();
                    break;
            }
            PolygonCanvas.this.invalidate();
            return false;
        }

    }

    private class CornerVertexTouchListener extends AbstractVertexTouchListener {

        @Override
        public void onTouchActionMove(View v, MotionEvent event) {
            if (isVertexInsideCanvas(v)) {
                v.setX(imageStartPosition.x + movement.x);
                v.setY(imageStartPosition.y + movement.y);
            }
        }
    }

    private class MidVertexTouchListener extends AbstractVertexTouchListener {

        private ImageView cornerVertex1;
        private ImageView cornerVertex2;

        public MidVertexTouchListener(ImageView cornerVertex1, ImageView cornerVertex2) {
            this.cornerVertex1 = cornerVertex1;
            this.cornerVertex2 = cornerVertex2;
        }

        private Boolean midVertexMovesXOfCornerVertices() {
            return Math.abs(cornerVertex1.getX() - cornerVertex2.getX()) < Math.abs(cornerVertex1.getY() - cornerVertex2.getY());
        }

        @Override
        public void onTouchActionMove(View v, MotionEvent event) {

            if (midVertexMovesXOfCornerVertices()) {
                if (isXInsideCanvas(cornerVertex1.getX(), v)) {
                    v.setX(touchStartPosition.x + movement.x);
                    cornerVertex1.setX(cornerVertex1.getX() + movement.x);
                }
                if (isXInsideCanvas(cornerVertex2.getX(), v)) {
                    v.setX(touchStartPosition.x + movement.x);
                    cornerVertex2.setX(cornerVertex2.getX() + movement.x);
                }
            } else {
                if (isYInsideCanvas(cornerVertex1.getY(), v)) {
                    v.setX(touchStartPosition.y + movement.y);
                    cornerVertex1.setY(cornerVertex1.getY() + movement.y);
                }
                if (isYInsideCanvas(cornerVertex2.getY(), v)) {
                    v.setX(touchStartPosition.y + movement.y);
                    cornerVertex2.setY(cornerVertex2.getY() + movement.y);
                }
            }

        }
    }
}
