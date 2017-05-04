package com.thinghowa.rollinganimation;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ArrayList<NoticeItem> mArrNotice;
    private RollingFrame mRollingNotice;
    private NoticeItem mNoticeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Make notices
        mArrNotice = new ArrayList<NoticeItem>();
        for(int i=0; i<6; i++) {
            mNoticeItem = new NoticeItem();

            StringBuffer sbTitle = new StringBuffer();
            sbTitle.append(Integer.toString(i));
            sbTitle.append("번째 공지사항 입니다.! ");
            sbTitle.append(Integer.toString(i));
            sbTitle.append("번째 공지사항 입니다.!");

            mNoticeItem.setTitle(sbTitle.toString());
            mNoticeItem.setIsNew(i%2==0?true:false);
            mNoticeItem.setLinkUrl(i%2==0?"https://www.naver.com":"https://www.google.com");
            mArrNotice.add(mNoticeItem);
        }

        mRollingNotice = (RollingFrame)findViewById(R.id.rolling_notice);
        mRollingNotice.initNotice(mArrNotice);
    }
}
