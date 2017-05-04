package com.thinghowa.rollinganimation;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by jinseop on 2017. 5. 3..
 */
public class RollingFrame extends FrameLayout {

    private Context mContext;

    // 공지사항이 들어갈 FrameLayout
    private FrameLayout mNoticeContaier;

    // View Array
    private ArrayList<View> mNoticeViewArr;

    // 공지사항 Array
    private ArrayList<NoticeItem> mNoticeArr;

    // 공지사항 Layout ViewHolder;
    private ViewHolder mViewHolder;

    // 공지사항 글 애니메이션
    private Animation mAppear, mDisappear;

    // 공지사항 Current index;
    private int mCurrentIndex = 0;

    // 공지사항 Current view
    private View mCurrentView = null;

    public RollingFrame(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        mViewHolder = new ViewHolder();

        View view = View.inflate(context, R.layout.rolling_frame_container, null);
        addView(view);

        mNoticeContaier = (FrameLayout)findViewById(R.id.notice_container);

        // Apprear animation
        mAppear = AnimationUtils.loadAnimation(context, R.anim.up_appear);
        mAppear.setFillAfter(true);
        mAppear.setAnimationListener(onAnimationAppear);

        // Disappear anumation
        mDisappear = AnimationUtils.loadAnimation(context, R.anim.up_disappear);
        mDisappear.setFillAfter(true);
        mDisappear.setAnimationListener(getOnAnimationDisappear);
    }

    /**
     * Notice list setting
     * @param noticeArr
     */
    public void initNotice(ArrayList<NoticeItem> noticeArr) {

        // 공지사항 목록
        mNoticeArr = noticeArr;

        // 공지사항 항목에 대한 뷰 생성 및 뷰 어레이에 저장장
       mNoticeViewArr = new ArrayList<View>();
        for(int i=0; i<mNoticeArr.size(); i++) {

            final String title      = mNoticeArr.get(i).getTitle();
            final Boolean isNew     = mNoticeArr.get(i).getIsNew();
            final String linkURl    = mNoticeArr.get(i).getLinkUrl();

            View view = View.inflate(mContext, R.layout.notice_layout, null);

            mViewHolder.rlNoticeItem = (LinearLayout)view.findViewById(R.id.rl_notice_item);
            mViewHolder.rlNoticeItem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("title :");
                    sb.append(title);
                    sb.append("\n");
                    sb.append("is new :");
                    sb.append(isNew);
                    sb.append("\n");
                    sb.append("url :");
                    sb.append(linkURl);
                    Toast.makeText(mContext, sb.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            mViewHolder.tvNoticeTitle = (TextView)view.findViewById(R.id.notice_title);
            mViewHolder.tvNoticeTitle.setText(title);

            mViewHolder.tvNew = (TextView)view.findViewById(R.id.tv_new);
            if(isNew) mViewHolder.tvNew.setVisibility(VISIBLE);

            mNoticeViewArr.add(view);
        }

        // Current index 초기화
        initCurrent();

        // 현재 뷰 추가
        mCurrentView = getCurrentView();
        mNoticeContaier.addView(mCurrentView);
        mCurrentView.startAnimation(mAppear);
    }

    /**
     * Notice show animation Listener
     */
    private Animation.AnimationListener onAnimationAppear = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 현재 보여지는 공지사항 Disappear
                    // mDisappear 애니메이션 listener에서 disappear 애니메이션이 끝난 후
                    // 다음 공지사항이 Appear되도록 설정
                    mCurrentView = getCurrentView();
                    mCurrentView.startAnimation(mDisappear);
                }
            }, 4000);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    };

    /**
     * Notice disappear animation Listener
     */
    private Animation.AnimationListener getOnAnimationDisappear = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            doNextMove();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    };

    /**
     * Initialize current view index
     */
    private void initCurrent() {
        mCurrentIndex = 0;
    }

    /**
     * Get current view
     * @return
     */
    private View getCurrentView() {
        if(mCurrentIndex >= getNoticeCount()) mCurrentIndex = 0;

        return mNoticeViewArr.get(mCurrentIndex);
    }

    /**
     * Increase current index
     * 마지막 인덱스이면 처음(0)으로 초기화
     */
    private void upCurrentIndex() {
        if(mCurrentIndex+1 >= getNoticeCount()) initCurrent();
        else mCurrentIndex++;
    }

    /**
     * Go to next notice
     */
    private void doNextMove() {
        upCurrentIndex();

        mCurrentView = getCurrentView();
        mNoticeContaier.removeAllViews();
        mNoticeContaier.addView(mCurrentView);
        mCurrentView.startAnimation(mAppear);
    }

    /**
     * Get notice count
     * @return
     */
    private int getNoticeCount() {
        return mNoticeArr.size();
    }

    private class ViewHolder {
        private LinearLayout rlNoticeItem;
        private TextView tvNoticeTitle;
        private TextView tvNew;
    }
}
