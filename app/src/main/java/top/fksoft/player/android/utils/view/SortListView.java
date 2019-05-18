package top.fksoft.player.android.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import top.fksoft.player.android.utils.android.DisplayUtils;

import java.util.Collections;
import java.util.List;


/**
 * @author ExplodingDragon
 * @Date 2019-5-13
 */
public class SortListView extends RelativeLayout {
    private ListView listView;
    private SortListAdapter adapter;

    public static String[] letter = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    public int[] letterIndex = null;
    private LineView lineView;

    public SortListView(Context context) {
        super(context);
        init();
    }

    public SortListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SortListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SortListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        listView = new ListView(getContext());
        listView.setHorizontalScrollBarEnabled(false);
        listView.setVerticalScrollBarEnabled(false);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        listView.setLayoutParams(layoutParams);
        addView(listView);
        lineView = new LineView(getContext());
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.rightMargin = 0;
        lineView.setLayoutParams(layoutParams);
        addView(lineView);
        lineView.setCheckedListener((view, index) -> {
            if (letterIndex != null)
                listView.setSelection(letterIndex[index > 0 ? index - 1 : 0]);
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (letterIndex != null) {
                    int item = letterIndex.length - 1;
                    float d = ((float) firstVisibleItem) / listView.getCount() * visibleItemCount;
                    int itemIndex = (int) (firstVisibleItem + d);
                    for (int i = letterIndex.length - 1; i >= 0; i--) {
                        int d2 = letterIndex[i];
                        if (d2 > itemIndex) {
                            item = i;
                        }
                    }
                    lineView.checkIndex(item);
                }
            }
        });
    }

    public void setAdapter(SortListAdapter adapter) {
        this.adapter = adapter;
        listView.setAdapter(adapter);
        this.letterIndex = adapter.letterIndex;
    }


    public static abstract class SortListAdapter<T extends ListBean> extends ArrayAdapter<T> {
        private final List<T> objects;
        int letterIndex[] = new int[letter.length];

        public SortListAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
            super(context, resource, objects);

            this.objects = objects;
            sortData();
        }

        protected void sortData() {
            synchronized (objects) {
                Collections.sort(objects, (o1, o2) -> {
                    ListBean bean1 = o1;
                    ListBean bean2 = o2;
                    if ((bean1 == null && bean2 == null) || (bean1.titleCharArr() == null && bean2.titleCharArr() == null))
                        return 0;
                    if (bean1 == null || bean1.titleCharArr() == null)
                        return 1;
                    if (bean2 == null || bean2.titleCharArr() == null)
                        return -1;

                    char[] c1 = bean1.titleCharArr().toCharArray();
                    char[] c2 = bean2.titleCharArr().toCharArray();
                    if (c1.length == 0)
                        return 1;
                    if (c2.length == 0)
                        return -1;

                    if (!Character.isLetter(c1[0]) && !Character.isLetter(c2[0]))
                        return 0;
                    if (!Character.isLetter(c1[0]))
                        return 1;
                    if (!Character.isLetter(c2[0]))
                        return -1;

                    int len = c1.length > c2.length ? c2.length : c1.length;
                    if (len > 5) {
                        len = 5;
                    }
                    for (int i = 0; i < len; i++) {
                        int abs = Math.abs(c1[i] - c2[i]);
                        if (abs != 0) {
                            return c1[i] - c2[i];
                        }
                    }
                    return 0;
                });
                int index = 0;
                int size = objects.size();
                letterIndex[0] = 0;
                for (int i = 0; i < size; i++) {
                    ListBean t = objects.get(i);
                    String title = t.titleCharArr().toUpperCase();
                    for (int j = index; j < letter.length; j++) {
                        if (title.startsWith(letter[j])) {
                            letterIndex[index]++;
                            break;
                        } else if (index == letter.length - 1) {
                            letterIndex[letter.length - 1]++;
                            break;
                        } else {
                            int i1 = letterIndex[index++];
                            letterIndex[index] = i1;
                        }
                    }
                }

            }
        }

        @Override
        public void notifyDataSetChanged() {
            sortData();
            super.notifyDataSetChanged();
        }

    }

    public interface ListBean {
        @NonNull
        String titleCharArr();
    }


    private static class LineView extends View {
        private CheckedListener listener = null;


        private boolean touch = false;//是否被按下
        private int checkId = -1;//点击的item
        private float lineStartX = 0;//可触摸的X
        private float lineStartY = 0;
        private float lineEndX = 0;
        private float lineEndY = 0;

        private Paint textPaint = new Paint();
        private Paint backPaint = new Paint();


        private Paint checkBackPaint = new Paint();

        private Paint centerTextPaint = new Paint();
        private Paint centerBackPaint = new Paint();

        private int defaultTextColor = Color.BLACK;
        private int touchTextColor = Color.WHITE;
        private int checkedTextColor = Color.RED;

        private int defaultBackColor = Color.TRANSPARENT;
        private int touchBackColor = 0x90CCCCCC;

        private int centerBackColor = touchBackColor;
        private int checkedBackColor = 0x30333333;

        private int paddingTop = 5;
        private int paddingLeft = 5;
        private int paddingRight = 20;
        private int paddingBottom = 5;

        private int touchWidth = 10;
        private int centerWidth = 50;
        private float touchX, touchY;
        private int centerTextColor = touchTextColor;
        private int  textSize;

        public LineView(Context context) {
            super(context);
            paddingTop = paddingLeft = paddingBottom = DisplayUtils.dip2px(context, 5);
            touchWidth = DisplayUtils.getDisplayWidth(context)/95;
            textSize = DisplayUtils.dip2px(context, 15);
            centerWidth = DisplayUtils.dip2px(context, 30);
            textPaint.setAntiAlias(true);
            backPaint.setAntiAlias(true);
            centerTextPaint.setAntiAlias(true);
            centerBackPaint.setAntiAlias(true);
            checkBackPaint.setAntiAlias(true);
            textPaint.setFakeBoldText(true);
            textPaint.setTextSize(textSize);
            centerTextPaint.setFakeBoldText(true);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            int width = getWidth();
            int height = getHeight();
            lineStartX = width - paddingRight - touchWidth;
            lineEndX = width - paddingRight;
            lineStartY = paddingTop;
            lineEndY = height - paddingBottom;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawRightBack(canvas);//绘制背景
            drawRightText(canvas);//绘制右侧文字
            if (touch) {
                drawCenter(canvas);
            }

        }

        private void drawCenter(Canvas canvas) {
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            centerBackPaint.setColor(centerBackColor);
            centerTextPaint.setColor(centerTextColor);
            float v1 = (float) ((float) textSize * 1.25);
            centerTextPaint.setTextSize( v1);
            float v = centerTextPaint.measureText(letter[checkId]);
            canvas.drawCircle(centerX , centerY ,centerWidth , centerBackPaint);
            canvas.drawText(letter[checkId], centerX- v/2, centerY +  v1 / 4, centerTextPaint);


        }

        private void drawRightBack(Canvas canvas) {
            RectF rect1 = new RectF(lineStartX - touchWidth - paddingLeft, lineStartY, getWidth() - paddingRight / 2, getHeight());
            if (touch) {
                backPaint.setColor(touchBackColor);
            } else {
                backPaint.setColor(defaultBackColor);
            }
            canvas.drawRoundRect(rect1, 20, 20, backPaint);


        }

        private void drawRightText(Canvas canvas) {
            if (touch) {
                textPaint.setColor(touchTextColor);
            } else {
                textPaint.setColor(defaultTextColor);
            }
            int length = letter.length;
            float a = (lineEndY - lineStartY) / length;
            int color = textPaint.getColor();
            for (int i = 0; i < length; i++) {
                float textWidth = textPaint.measureText(letter[i]);
                float x = lineStartX - textWidth / 2;
                float y = a * (i + 1);
                if (checkId == i) {
                    //绘制选择背景
                    checkBackPaint.setColor(checkedBackColor);
                    float chooseBackY = !touch ? y - textWidth / 2 : touchY;
                    canvas.drawCircle(x + textWidth / 2, chooseBackY, a / 2, checkBackPaint);
                    textPaint.setColor(checkedTextColor);
                } else {
                    textPaint.setColor(color);
                }


                canvas.drawText(letter[i], x, y, textPaint);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            touchX = event.getX();
            touchY = event.getY();
            int c = (int) (touchY / getHeight() * letter.length);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (touchX >= lineStartX - paddingLeft) {
                        touch = true;
                        check(c);
                        invalidate();
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (touch) {
                        check(c);
                        invalidate();
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (touch) {
                        touch = false;
                        check(c);
                        touchX = touchY = -1;
                        invalidate();
                    }
                    break;
            }
            return false;
        }

        private void check(int id) {
            if (checkId != id && listener != null && id >= 0 && id < 27) {
                listener.checked(this, id);
            }
            checkId = id;
            if (id >= letter.length) {
                checkId = letter.length - 1;
            }
            if (id < 0) {
                checkId = 0;
            }
        }

        public void checkIndex(int index) {
            if (touch || index >= letter.length || index < 0)
                return;
            checkId = index;
            invalidate();
        }

        interface CheckedListener {
            void checked(View view, int index);
        }

        public void setCheckedListener(CheckedListener listener) {
            this.listener = listener;

        }

    }
}
