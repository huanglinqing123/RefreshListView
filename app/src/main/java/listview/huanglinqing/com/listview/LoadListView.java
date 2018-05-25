package listview.huanglinqing.com.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/3/12.
 */
public class LoadListView extends ListView implements AbsListView.OnScrollListener{


    private static final int STATE_PULL_REFRESH = 0;// 下拉刷新
    private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
    private static final int STATE_REFRESHING = 2;// 正在刷新

    private View mHeaderView;
    private View mFooterView;

    private int startY = -1;// 滑动起点的y坐标
    private int mHeaderViewHeight;
    private int mFooterViewHeight;

    private int mCurrrentState = STATE_PULL_REFRESH;// 当前状态
    private TextView tvTitle;
    private TextView tvTime;
    private ImageView ivArrow;
    private ProgressBar pbProgress;
    private RotateAnimation animUp;
    private RotateAnimation animDown;

    private boolean isLoadingMore;

    OnRefreshListener mListener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public interface OnRefreshListener {
        public void onRefresh();
    }

    public LoadListView(Context context) {
        super(context);
    }

    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        this.setOnScrollListener(this);

    }
    private void initHeaderView() {

        mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
        this.addHeaderView(mHeaderView);

        tvTitle = mHeaderView.findViewById(R.id.tv_title);
        tvTime =  mHeaderView.findViewById(R.id.tv_time);
        ivArrow =  mHeaderView.findViewById(R.id.iv_arr);
        pbProgress =  mHeaderView.findViewById(R.id.pb_progress);

        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏头布局
        tvTime.setText("最后刷新时间:" + getCurrentTime());
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * 刷新下拉控件的布局
     */
    private void refreshState() {        //
        switch (mCurrrentState) {
            case STATE_PULL_REFRESH:
                tvTitle.setText("下拉刷新");
                ivArrow.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.INVISIBLE);
                break;
            case STATE_RELEASE_REFRESH:
                tvTitle.setText("松开刷新");
                ivArrow.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.INVISIBLE);
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新...");
                ivArrow.setVisibility(View.INVISIBLE);
                pbProgress.setVisibility(View.VISIBLE);
                if (mListener != null) {
                    mListener.onRefresh();  //   下拉刷新
                }
                break;

            default:
                break;
        }
    }



    /**
     * 获取当前时间
     */
    public String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {// 确保startY有效F
                    startY = (int) ev.getRawY();
                }

                if (mCurrrentState == STATE_REFRESHING) {
                    // 正在刷新时不做处理
                    break;
                }

                int endY = (int) ev.getRawY();
                int dy = endY - startY;// 移动便宜量

                if (dy > 0 && getFirstVisiblePosition() == 0) {
                    // 只有下拉并且当前是第一个item,才允许下拉
                    int padding = dy - mHeaderViewHeight;// 计算padding
                    if (padding<15) {
                        //这个是我自己设置的，防止 下拉位置   过大
                        mHeaderView.setPadding(0, padding, 0, 0);// 设置当前padding
                    }
                    if (padding > 0 && mCurrrentState != STATE_RELEASE_REFRESH) {
                        // 状态改为松开刷新
                        mCurrrentState = STATE_RELEASE_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrrentState != STATE_PULL_REFRESH) {
                        // 改为下拉刷新状态
                        mCurrrentState = STATE_PULL_REFRESH;
                        refreshState();
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;// 重置
                if (mCurrrentState == STATE_RELEASE_REFRESH) {
                    mCurrrentState = STATE_REFRESHING;// 正在刷新
                    mHeaderView.setPadding(0, 0, 0, 0);// 显示
                    refreshState();
                } else if (mCurrrentState == STATE_PULL_REFRESH) {
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }



    //收起 加载更多
    public void onLoadComplete(){
        if(isLoadingMore){ // 正在加载更多
            mFooterView.setPadding(0,-mFooterViewHeight,0,0);// 隐藏脚布局
            isLoadingMore = false;
        }
    }

    /*
 * 收起下拉刷新的控件
 */
    public void onRefreshComplete(boolean success) {
        mCurrrentState = STATE_PULL_REFRESH;
        tvTitle.setText("下拉刷新");
        ivArrow.setVisibility(View.VISIBLE);
        pbProgress.setVisibility(View.INVISIBLE);
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏
        if (success) {
            tvTime.setText("最后刷新时间:" + getCurrentTime());
        }
    }


}

