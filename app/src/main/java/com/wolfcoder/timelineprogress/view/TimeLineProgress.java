package com.wolfcoder.timelineprogress.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;


import com.wolfcoder.timelineprogress.R;

import java.util.ArrayList;

/**
 * Description:
 * User: Dream_Coder(chenchen_839@126.com)
 * Date: 2016-04-02
 * Time: 15:50
 */
public class TimeLineProgress extends View {
    /**
     * 自定义属性
     * <attr name="reachColor" format="color"/>
     * <attr name="unReachColor" format="color"/>
     * <attr name="clockIconColor" format="color"/>
     * <attr name="stopColor" format="color"/>
     * <attr name="clockWidth" format="dimension"/>
     * <attr name="timePositionWidth" format="dimension"/>
     * <attr name="subTimePostionWidth" format="dimension"/>
     */
    private int reachColor;
    private int unReachColor;
    private int clockIconColor;
    private int stopColor;
    private float timeIconWidth;
    private float timePositionWidth;
    private float subCircleRadius;


    private Paint mPaintText;
    private Paint mPaintTimeClock;
    private Paint mPaintTimeReach;
    private Paint mPaintTimeUnReach;
    private Paint mPaintSubTimePositionReach;
    private Paint mPaintSubTimePositionUnReach;
    private Rect timeClockRect;

    private int timePositionSize;
    private int currentTimePosition ;
    private int currentSubTimePosition ;
    private int stopIndex ;

    private ArrayList<String> timePositionMsg = new ArrayList<>();
    private ArrayList<Rect> timePositionRect = new ArrayList<>();
    private ArrayList<ArrayList<String>> subTimePositionMsg = new ArrayList<>();

    public TimeLineProgress(Context context) {
        this(context, null);
    }

    public TimeLineProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeLineProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimeLineProgress);
        reachColor = array.getColor(R.styleable.TimeLineProgress_reachColor, Color.parseColor("#FF9900"));
        unReachColor = array.getColor(R.styleable.TimeLineProgress_unReachColor, Color.parseColor("#CCCCCC"));
        clockIconColor = array.getColor(R.styleable.TimeLineProgress_clockIconColor, Color.parseColor("#CCCCCC"));
        stopColor = array.getColor(R.styleable.TimeLineProgress_stopColor, Color.RED);
        timeIconWidth = array.getDimension(R.styleable.TimeLineProgress_clockWidth, dp2px(30));
        timePositionWidth = array.getDimension(R.styleable.TimeLineProgress_timePositionWidth, dp2px(20));
        subCircleRadius = array.getDimension(R.styleable.TimeLineProgress_subTimePostionWidth, dp2px(4));
        initializePaint();
        array.recycle();
    }

    /**
     * 初始化所使用的笔
     */
    private void initializePaint() {
        //设置时间线标志
        mPaintTimeClock = new Paint();
        mPaintTimeClock.setColor(clockIconColor);
        mPaintTimeClock.setStyle(Paint.Style.STROKE);
        mPaintTimeClock.setStrokeWidth(dp2px(1));
        mPaintTimeClock.setAntiAlias(true);
        //设置未到达部分
        mPaintTimeUnReach = new Paint();
        mPaintTimeUnReach.setColor(unReachColor);
        mPaintTimeUnReach.setStyle(Paint.Style.FILL);
        mPaintTimeUnReach.setStrokeWidth(dp2px(1));
        mPaintTimeUnReach.setAntiAlias(true);
        //设置已经到达的部分
        mPaintTimeReach = new Paint();
        mPaintTimeReach.setColor(reachColor);
        mPaintTimeReach.setStyle(Paint.Style.STROKE);
        mPaintTimeReach.setStrokeWidth(dp2px(2));
        mPaintTimeReach.setAntiAlias(true);
        //设置字体
        mPaintText = new Paint();
        mPaintText.setColor(Color.parseColor("#765972"));
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setAntiAlias(true);
        mPaintText.setTextSize(sp2px(12));
        //设置事件未到达子标题
        mPaintSubTimePositionReach = new Paint();
        mPaintSubTimePositionReach.setColor(reachColor);
        mPaintSubTimePositionReach.setStyle(Paint.Style.FILL);
        mPaintSubTimePositionReach.setStrokeWidth(dp2px(1));
        mPaintSubTimePositionReach.setAntiAlias(true);
        //设置事件未到达子标题
        mPaintSubTimePositionUnReach = new Paint();
        mPaintSubTimePositionUnReach.setColor(unReachColor);
        mPaintSubTimePositionUnReach.setStyle(Paint.Style.FILL);
        mPaintSubTimePositionUnReach.setStrokeWidth(dp2px(1));
        mPaintSubTimePositionUnReach.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        timeClockRect = camputeTimeClockRect();
        Bitmap timeClock = drawTimeClockIcon();
        canvas.drawBitmap(timeClock, timeClockRect.left + 1, timeClockRect.top + 1, mPaintTimeClock);
        drawTimePositionRect(canvas);
        drawStopIcon(canvas);
    }

    /**
     * 绘制停止在某个阶段的icon
     *
     * @param canvas
     */
    private void drawStopIcon(Canvas canvas) {
        if (stopIndex > 0) {
            Rect rect = timePositionRect.get(stopIndex - 1);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(dp2px(2));
            paint.setColor(stopColor);
            canvas.drawLine(rect.centerX() - dp2px(4), rect.centerY(), rect.centerX() + dp2px(4), rect.centerY(), paint);
        }
    }

    /**
     * 绘制时间线的ICON
     *
     * @return
     */
    private Bitmap drawTimeClockIcon() {
        Bitmap bitmap = Bitmap.createBitmap(
                (int) timeIconWidth,
                (int) timeIconWidth,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        float position = timeIconWidth / 2;
        canvas.drawCircle(position, position, position - 2, mPaintTimeClock);
        canvas.drawLine(position, position, position, position - 20, mPaintTimeClock);
        canvas.drawLine(position, position, position + 20, position, mPaintTimeClock);
        return bitmap;
    }

    /**
     * 计算时间线的ICON
     *
     * @return
     */
    private Rect camputeTimeClockRect() {
        Rect rect = new Rect();
        int height = getMeasuredHeight();
        rect.left = getPaddingLeft();
        rect.top = (int) (height - timeIconWidth - getPaddingBottom() - dp2px(10));
        rect.right = (int) (rect.left + timeIconWidth);
        rect.bottom = (int) (height - getPaddingBottom() - dp2px(12));
        return rect;
    }

    /**
     * 绘制时间点
     *
     * @param canvas
     */
    private void drawTimePositionRect(Canvas canvas) {
        camputeTimePositionRect();
        if (timePositionSize == 0) {
            return;
        } else if (timePositionSize == 1) {
            Rect rect = timePositionRect.get(0);
            canvas.drawLine(rect.left, rect.height() / 2, rect.right - timePositionWidth, rect.height() / 2, mPaintTimeClock);
            canvas.drawCircle(rect.right - timePositionWidth / 2, rect.height() / 2, timePositionWidth / 2, mPaintTimeClock);
        } else {
            for (int i = 0; i < timePositionSize; i++) {
                drawItemTimePosition(i, i == timePositionSize - 1, canvas);
            }
        }
    }

    /**
     * 绘制二级事件
     *
     * @param canvas
     */
    private void drawSubTimePosition(Canvas canvas, int index, Rect rect, boolean isLast) {

        if (index < subTimePositionMsg.size()) {
            ArrayList<String> currentSubMsg = subTimePositionMsg.get(index);
            if (currentSubMsg != null && currentSubMsg.size() != 0) {
                Paint currentPaint;
                int sumHeight = (int) (rect.top - getPaddingTop() - dp2px(1));
                int perHeight = sumHeight / currentSubMsg.size();
                int x, y;//记录下方圆心的位置
                if (isLast) {
                    x = (int) (rect.right - timePositionWidth / 2);
                } else {
                    x = rect.centerX();
                }
                y = (int) (getPaddingTop() + dp2px(2));

                for (int i = 0; i < currentSubMsg.size(); i++) {
                    int top = i * perHeight + y;
                    int bottom = (i + 1) * perHeight;
                    if (index >= currentTimePosition) {
                        currentPaint = mPaintSubTimePositionUnReach;
                    } else {
                        if (index < currentTimePosition - 1) {
                            currentPaint = mPaintSubTimePositionReach;
                        } else {
                            if (i < currentSubTimePosition) {
                                currentPaint = mPaintSubTimePositionReach;
                            } else {
                                currentPaint = mPaintSubTimePositionUnReach;
                            }
                        }
                    }
                    canvas.drawCircle(x, top + subCircleRadius, subCircleRadius, currentPaint);
                    canvas.drawLine(x, top + subCircleRadius, x, bottom, currentPaint);
                    if (!isLast) {
                        canvas.drawText(currentSubMsg.get(i), x + subCircleRadius + dp2px(2), top + 2 * subCircleRadius, mPaintText);
                    } else {
                        String content = currentSubMsg.get(i);
                        float length = mPaintText.measureText(content);
                        canvas.drawText(currentSubMsg.get(i), x - 2 * subCircleRadius - length, top + 2 * subCircleRadius, mPaintText);
                    }
                }
            }
        }
    }

    /**
     * 绘制每一个时间点
     *
     * @param index  坐标点
     * @param isLast 是否是最后一个
     */
    private void drawItemTimePosition(int index, boolean isLast, Canvas canvas) {
        Paint currentPaint = index >= currentTimePosition ? mPaintTimeUnReach : mPaintTimeReach;
        Rect rect = timePositionRect.get(index);
        int x = 0, y = 0;
        float textLength = mPaintText.measureText(timePositionMsg.get(index));
        if (isLast) {//如果是最后一个
            x = (int) (rect.right - timePositionWidth / 2);
            y = (rect.bottom + rect.top) / 2;
            canvas.drawLine(rect.left, y, rect.right - timePositionWidth, y, currentPaint);
            canvas.drawCircle(x, y, timePositionWidth / 2, currentPaint);
            canvas.drawText(timePositionMsg.get(index), x + timePositionWidth / 2 - textLength, rect.bottom + dp2px(10), mPaintText);
        } else {
            x = (rect.right + rect.left) / 2;
            y = (timeClockRect.top + timeClockRect.bottom) / 2;
            canvas.drawLine(rect.left, y, x - timePositionWidth / 2, y, currentPaint);
            canvas.drawCircle(x, y, timePositionWidth / 2, currentPaint);
            canvas.drawCircle(x, y, timePositionWidth / 4, currentPaint);
            canvas.drawLine(x + timePositionWidth / 2, y, rect.right, y, currentPaint);
            canvas.drawText(timePositionMsg.get(index), x - textLength / 2, rect.bottom + dp2px(11), mPaintText);
        }
        drawSubTimePosition(canvas, index, rect, isLast);

    }

    /**
     * 根据timePosition点的个数计算每个时间点所占据的位置
     */
    private void camputeTimePositionRect() {
        int sum = getMeasuredWidth() - getPaddingRight() - timeClockRect.right; //可分的所有部分
        int perWidth = sum / timePositionSize;//每一块的大小
        if (timePositionSize == 0) {
            return;
        }
        if (timePositionSize == 1) {
            Rect rect = new Rect(timeClockRect.right, getPaddingTop(),
                    getMeasuredWidth() - getPaddingRight(),
                    getMeasuredHeight() - getPaddingBottom());
            timePositionRect.add(rect);
        } else {
            for (int i = 0; i < timePositionSize; i++) {
                Rect rect = new Rect();
                rect.left = timeClockRect.right + i * perWidth;
                rect.top = timeClockRect.top;
                rect.right = rect.left + perWidth;
                rect.bottom = timeClockRect.bottom;
                timePositionRect.add(rect);
            }
        }
    }

    public void setTimePositionSize(int size) {
        this.timePositionSize = size;
        invalidate();
    }

    public int getTimePositionSize() {
        return this.timePositionSize;
    }

    public void setCurrentTimePosition(int position) {
        this.currentTimePosition = position;
        invalidate();
    }

    public int getCurrentTimePosition() {
        return currentTimePosition;
    }

    public void setCurrentStatus(int index,int subIndex) {
        this.currentTimePosition = index;
        this.currentSubTimePosition = subIndex;
        invalidate();
    }
    public void addTimePositionMsg(ArrayList<String> msg) {
        this.timePositionMsg = msg;
        timePositionSize = msg.size();
        invalidate();
    }

    public ArrayList<String> getTimePositionMsg() {
        return this.timePositionMsg;
    }

    public void addSubTimePosition(ArrayList<ArrayList<String>> subTimePositionMsg) {
        this.subTimePositionMsg = subTimePositionMsg;
        invalidate();
    }
    public float dp2px(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return density * dp + 0.5f;
    }

    public float sp2px(int sp) {
        float scale = getResources().getDisplayMetrics().scaledDensity;
        return scale * sp + 0.5f;
    }

    public void setStop(int index) {
        this.stopIndex = index;
        invalidate();
    }
}