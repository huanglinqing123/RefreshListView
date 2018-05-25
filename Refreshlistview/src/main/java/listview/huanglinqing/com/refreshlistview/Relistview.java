package listview.huanglinqing.com.refreshlistview;

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
 * Created by huanglinqing on 2018/5/24.
 */
public class Relistview extends ListView implements AbsListView.OnScrollListener{


    private static final int STATE_PULL_REFRESH = 0;
    private static final int STATE_RELEASE_REFRESH = 1;
    private static final int STATE_REFRESHING = 2;

    private View mHeaderView;
    private View mFooterView;

    private int startY = -1;
    private int mHeaderViewHeight;
    private int mFooterViewHeight;

    private int mCurrrentState = STATE_PULL_REFRESH;
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

    public Relistview(Context context) {
        super(context);
    }

    public Relistview(Context context, AttributeSet attrs) {
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
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
        tvTime.setText(getContext().getString(R.string.zuihoushuaixinshijian) + getCurrentTime());
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    private void refreshState() {
        switch (mCurrrentState) {
            case STATE_PULL_REFRESH:
                tvTitle.setText(R.string.xialashuaxin1);
                ivArrow.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.INVISIBLE);
                break;
            case STATE_RELEASE_REFRESH:
                tvTitle.setText(R.string.songkaishuaxin);
                ivArrow.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.INVISIBLE);
                break;
            case STATE_REFRESHING:
                tvTitle.setText(R.string.zhengzaishuaxin);
                ivArrow.setVisibility(View.INVISIBLE);
                pbProgress.setVisibility(View.VISIBLE);
                if (mListener != null) {
                    mListener.onRefresh();
                }
                break;

            default:
                break;
        }
    }




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
                if (startY == -1) {
                    startY = (int) ev.getRawY();
                }

                if (mCurrrentState == STATE_REFRESHING) {

                    break;
                }

                int endY = (int) ev.getRawY();
                int dy = endY - startY;

                if (dy > 0 && getFirstVisiblePosition() == 0) {

                    int padding = dy - mHeaderViewHeight;
                    if (padding<15) {

                        mHeaderView.setPadding(0, padding, 0, 0);
                    }
                    if (padding > 0 && mCurrrentState != STATE_RELEASE_REFRESH) {

                        mCurrrentState = STATE_RELEASE_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrrentState != STATE_PULL_REFRESH) {

                        mCurrrentState = STATE_PULL_REFRESH;
                        refreshState();
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (mCurrrentState == STATE_RELEASE_REFRESH) {
                    mCurrrentState = STATE_REFRESHING;
                    mHeaderView.setPadding(0, 0, 0, 0);
                    refreshState();
                } else if (mCurrrentState == STATE_PULL_REFRESH) {
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }




    public void onLoadComplete(){
        if(isLoadingMore){
            mFooterView.setPadding(0,-mFooterViewHeight,0,0);
            isLoadingMore = false;
        }
    }


    public void onRefreshComplete(boolean success) {
        mCurrrentState = STATE_PULL_REFRESH;
        tvTitle.setText(R.string.xialashuaxin);
        ivArrow.setVisibility(View.VISIBLE);
        pbProgress.setVisibility(View.INVISIBLE);
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏
        if (success) {
            tvTime.setText(getContext().getString(R.string.zuihou) + getCurrentTime());
        }
    }


}

