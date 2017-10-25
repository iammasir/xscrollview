
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;





public class XScrollView extends ScrollView {

    private boolean isThreadStart = false;//线程是否开启
    private int i = 0;//计时器可调的值

    private boolean noti;//是否输出结果
    private float oY = -100;//原始getScrollY()
    private boolean isUp;//手指是否抬起

    private float preY = -1;//前一次的y值
    private static final int STOP = 0;
    private static final int UP = 1;
    private static final int DOWN = -1;
    private int scrollState = DOWN;//滚动状态

    private ScrollStateListener scrollStateListener;

    public void setScrollStateListener(ScrollStateListener scrollStateListener) {
        this.scrollStateListener = scrollStateListener;
    }

    public interface ScrollStateListener{
        void scrollUp();
        void scrollDown();
        void reachTop();
        void reachBottom();
        void onScrolling(int scrollY);
        void onStop();

    }


    public XScrollView(Context context) {
        super(context);
    }

    public XScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public XScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isUp = false;
                noti = false;

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                isUp = true;

                break;

        }
        boolean result = super.onTouchEvent(event);
        return result;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        scrollStateListener.onScrolling(t);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {

        if (oY == -100) {//获取原始getScrollY()的值，只获取一次
            oY = getScrollY();
        }

        float currentY = getScrollY();//当前的滚动距离

        if (currentY - preY >= 2 && scrollState != UP && !isUp) {//向上滑动

            scrollState = UP;
            if (scrollStateListener!=null){
                scrollStateListener.scrollUp();
            }

        } else if (preY - currentY >= 2 && scrollState != DOWN && !isUp) {//向下滑动

            scrollState = DOWN;
            if (scrollStateListener!=null){
                scrollStateListener.scrollDown();
            }

        } else if (preY - currentY == 0) {

        }

        preY = currentY;


        if (clampedY && !noti) {//到了边界了
            noti = true;
            if (scrollState == UP) {

                if (scrollStateListener!=null){
                    scrollStateListener.reachBottom();
                }

            } else if (scrollState == DOWN) {

                if (scrollStateListener!=null){
                    scrollStateListener.reachTop();
                }

            }

        }
        timeDown();
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }


    /**
     * 计时器,0.5秒内不执行第二次或者更多次，便执行结束的方法
     */
    public void timeDown() {
        if (!isThreadStart) {//方法执行的时候，线程只开启一次
            isThreadStart=true;
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    for (; i < 5; i++) {//循环可运行0.5秒的时间
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    isThreadStart=false;
                    i=0;
                    //要执行的方法
                    if (scrollStateListener!=null){
                        scrollState=STOP;
                        scrollStateListener.onStop();
                    }

                }
            }.start();
        } else {
            i = 0;
        }
    }




}
