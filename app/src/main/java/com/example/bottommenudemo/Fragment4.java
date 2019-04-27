package com.example.bottommenudemo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

public class Fragment4 extends Fragment  {
    ChartLineUtil chartLineUtil;                                                                    //实例化折线图组件显示半径报警趋势
    ChartLineUtil chartLineUtil2;                                                                   //实例化折线图组件显示频谱报警趋势
    private LinearLayout mUp;
    private LinearLayout mDown;
    View view=null;
    Timer timer = new Timer();
    private TimerTask timerTask2;
    private TimerTask timerTask1;
    Bundle s;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PPP","Bundle"+savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view==null)
            view = inflater.inflate(R.layout.fragment_fragment4, container, false);

        chartLineUtil=new ChartLineUtil(getContext(),-1000,0);                                              //引用折线图组件显示半径报警趋势
        mUp=(LinearLayout)view.findViewById(R.id.tv);
        mUp.addView(chartLineUtil);
        chartLineUtil2=new ChartLineUtil(getContext(),-1200,100);                                             //实例化折线图组件显示频谱报警趋势
        mDown=(LinearLayout)view.findViewById(R.id.tv2);
        mDown.addView(chartLineUtil2);
        Log.d("KKK","Bundle"+savedInstanceState);
        return view;

    }




    public void onStart() {
        super.onStart();
        timerTask2 = new TimerTask() {
            @Override
            public void run() {                                                            //新建定时器任务用于刷新半径与频谱报警趋势折线图
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myHandler.sendEmptyMessage(0x1234);
                    }
                });
            }
        };
        timer = Tim.getTimer();
        timer.schedule(timerTask2, 1000,100);//延时1秒，每隔3秒刷新一次
        timerTask1 = new TimerTask() {
            @Override
            public void run() {                                                            //新建定时器任务用于刷新半径与频谱报警趋势折线图
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myHandler2.sendEmptyMessage(0x1235);
                    }
                });
            }
        };
        timer = Tim.getTimer();
        timer.schedule(timerTask1, 100,60000);
    }


    final Handler myHandler2 = new Handler()
    {
        @Override
        //重写handleMessage方法,根据msg中what的值判断是否执行后续操作
        public void handleMessage(Message msg) {
            if(msg.what == 0x1235) {


                Log.d("EEE","执行");
            }
        }};

    @Override
    public void onStop() {
        super.onStop();
        timerTask2.cancel();                                                                        //关闭定时任务
    }

    /**********************半径与频谱折线图刷新部分**********************/
    final Handler myHandler = new Handler()
    {
        @Override
        //重写handleMessage方法,根据msg中what的值判断是否执行后续操作
        public void handleMessage(Message msg) {
            if(msg.what == 0x1234) {
                SharedPreferences pref3 = getActivity().getSharedPreferences("data", MODE_PRIVATE);
                String input = pref3.getString("input", "");
                if (input.length() < 73)                                                            //尚未连接WIFI时传输数据时，为input设置初值
                    input="0\n0\n0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0";
                final String[] input0 = input.split("\n");
                final String[] input2 = input0[2].split(";");                                //将WIFI传输过来的数据进行切割，转为字符串数组
                if (input2[6]==null||input2[7]==null||input2[8]==null||input2[9]==null)             //未连接WIFI时，为折线图设置初值
                {
                    input2[6]="0";input2[7]="0";input2[8]="0";input2[9]="0";
                }
                //为半径与频谱的八条折线传入数据
                //chartLineUtil.setDataset(Integer.parseInt(input2[6]),Integer.parseInt(input2[7]),Integer.parseInt(input2[8]),Integer.parseInt(input2[9]));
                 chartLineUtil.setDataset((int)(Math.random()*100),(int)(Math.random()*120),(int)(Math.random()*200),(int)(Math.random()*10));
                chartLineUtil2.setDataset(Integer.parseInt(input2[10]),Integer.parseInt(input2[11]),Integer.parseInt(input2[12]),Integer.parseInt(input2[13]));
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
