package com.wolfcoder.timelineprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wolfcoder.timelineprogress.view.TimeLineProgress;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TimeLineProgress progress = (TimeLineProgress) findViewById(R.id.timeline);
        ArrayList<String> timePosition = new ArrayList<>();
        timePosition.add("笔试");
        timePosition.add("面试");
        timePosition.add("电面");
        timePosition.add("offer");
        ArrayList<ArrayList<String>> timesubPositionMsg = new ArrayList<>();

        ArrayList<String> list = new ArrayList<>();
        list.add("网申");
        list.add("审核");
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("笔试");
        list1.add("面试");
        list1.add("offer");
        ArrayList<String> list2 = new ArrayList<>();
        list2.add("入职");
        list2.add("成功");
        ArrayList<String> list3 = new ArrayList<>();
        list3.add("报道");
        timesubPositionMsg.add(list);
        timesubPositionMsg.add(list1);
        timesubPositionMsg.add(null);
        timesubPositionMsg.add(list3);
        progress.addTimePositionMsg(timePosition);
        progress.addSubTimePosition(timesubPositionMsg);
        progress.setCurrentStatus(2,1);
        progress.setStop(2);
    }
}
