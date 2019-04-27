package com.example.bottommenudemo;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
/*
这是一个自定义LinearLayout，主要用于画动态曲线图，即频谱半径报警趋势显示图
*/
@SuppressLint("HandlerLeak")
public class ChartLineUtil extends LinearLayout {
    private int b1=0,b2=0,b3=0,b4=0;
    private String[] titles = new String[]{"左前轮", "右前轮", "左后轮", "右后轮"};// 曲线标题
    private int[] colors = new int[]{Color.GREEN, Color.RED, Color.YELLOW,
            Color.BLUE, Color.CYAN};//定义颜色的数组
    private PointStyle[] styles = new PointStyle[]{
            PointStyle.CIRCLE,// 圆圈状
            PointStyle.DIAMOND, PointStyle.SQUARE, PointStyle.TRIANGLE,
            PointStyle.POINT};// 菱形状,矩形状,三角形
    private String chartLineTitle = "";
    private String xMessage = "当前时间(分秒)";
    private String yMessage = "数量(度或小时)";
    private int yMin;// y轴的最小值
    private int yMax;// y轴的最大值
    private Timer timer;
    private TimerTask task;
    private Handler handle;
    private XYSeries series;
    private List<XYSeries> seriesList;
    private XYMultipleSeriesDataset dataset;
    private XYMultipleSeriesRenderer renderer;
    private String[] xkedu;// x轴数据缓冲
    private GraphicalView mChartView;
    private SimpleDateFormat nowTime; // 获取当前时间
    private List<Integer> dataList; // 存放数据的集合
    private int dataOne, dataTwo, dataThree, dataFour;// 模拟数据
    private Float[] catchOne, catchTwo, catchThree, catchFour;// x轴缓存数据
    private int xLenght = 100;// x轴长度,即屏幕中显示的点数
    private List<Float[]> catchList;// 存放缓存数据的集合

    public ChartLineUtil(Context context,int Min,int Max) {
        super(context);
        yMin=Min;
        yMax=Max;
        initView();
        createView(context);

    }
    //初始化
    @SuppressLint("SimpleDateFormat")
    private void initView() {
        Log.d("SSS","初始化视图");
        timer = new Timer();
        catchOne = new Float[xLenght];
        catchTwo = new Float[xLenght];
        catchThree = new Float[xLenght];
        catchFour = new Float[xLenght];
        xkedu = new String[xLenght];
        dataList = new ArrayList<Integer>();
        nowTime = new SimpleDateFormat("mm:ss");
        seriesList = new ArrayList<XYSeries>();
        catchList = new ArrayList<Float[]>();
        catchList.add(catchOne);
        catchList.add(catchTwo);
        catchList.add(catchThree);
        catchList.add(catchFour);
    }
    //传入参数方法
    public void setDataset(int a1,int a2,int a3,int a4 ) {
        b1=-a1;
        b2=-a2;
        b3=-a3;
        b4=-a4;

    }


    private void createView(Context context) {
        LinearLayout mLinear = new LinearLayout(context);
        mLinear.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mLinear.setOrientation(HORIZONTAL);
        mLinear.setBackgroundColor(Color.parseColor("#ffffff"));
        mChartView = ChartFactory.getCubeLineChartView(context, getDataSet(),
                getRender(), 0.3f);
        mLinear.removeAllViews();
        mLinear.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        this.addView(mLinear);
        sendMessage();
    }

    /**
     * 发送消息
     */
    private void sendMessage() {
        handle = new Handler() {
            public void handleMessage(Message msg) {
                updatechart();
            }
        };
        task = new TimerTask() {
            public void run() {
                Message msg = new Message();
                msg.what = 200;
                handle.sendMessage(msg);
            }
        };
        timer.schedule(task, 0, 3000);
    }

    /**
     * 更新数据
     */
    private void updatechart() {


        dataOne = b1;
        dataTwo = b2;
        dataThree = b3;
        dataFour = b4;
        Log.d("SSS","dataOne ="+dataOne+";dataTwo ="+dataTwo +";dataThree="+dataThree+";dataFour="+dataFour);
        dataList.clear();
        dataList.add(dataOne);
        dataList.add(dataTwo);
        dataList.add(dataThree);
        dataList.add(dataFour);
        String xKeduValue = nowTime.format(new java.util.Date());
        //得到x轴上点的数量
        int seriesItemLenght = seriesList.get(0).getItemCount();
        // x轴控制显示100个数值
        if (seriesItemLenght > xLenght) {
            seriesItemLenght = xLenght;
        }
        if (seriesItemLenght%10==0) {
            renderer.addXTextLabel(seriesItemLenght + 1, xKeduValue);//添加x轴标签
            xkedu[seriesItemLenght%10] = xKeduValue;
        }
        // 移除旧的点集
        for (int i = 0; i < titles.length; i++) {
            dataset.removeSeries(seriesList.get(i));
        }

        if (seriesItemLenght < xLenght) {
            for (int i = 0; i < titles.length; i++) {
                seriesList.get(i).add(seriesItemLenght + 1, dataList.get(i));
            }
            if (seriesItemLenght%10==0){
                renderer.addXTextLabel(seriesItemLenght + 1, xKeduValue);//添加x轴标签
                xkedu[seriesItemLenght] = xKeduValue;}


        } else {

            // 将x,y数值缓存
            for (int i = 0; i < seriesItemLenght - 1; i++) {
                for (int j = 0; j < titles.length; j++) {
                    catchList.get(j)[i] = (float) seriesList.get(j).getY(i + 1);
                }
                xkedu[i] = xkedu[i + 1];
            }
            for (int i = 0; i < titles.length; i++) {

                seriesList.get(i).clear();
                Log.d("SSS","移除旧点");
            }
            // 添加新点,变换坐标
            for (int i = 0; i < seriesItemLenght - 1; i++) {
                for (int j = 0; j < titles.length; j++) {
                    seriesList.get(j).add(i + 1, catchList.get(j)[i]);
                    Log.d("SSS","添加新点");
                }
                if (i%10==0)
                    renderer.addXTextLabel(i + 1, xkedu[i]);
            }

            xkedu[xLenght - 1] = xKeduValue;
            for (int i = 0; i < titles.length; i++) {
                seriesList.get(i).add(xLenght, dataList.get(i));
            }
            renderer.addXTextLabel(xLenght, xKeduValue);
        }
        for (int i = 0; i < titles.length; i++) {
            dataset.addSeries(seriesList.get(i));
        }
        mChartView.invalidate();
    }

    private XYMultipleSeriesDataset getDataSet() {
        dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, 0);
        return dataset;
    }

    private XYMultipleSeriesRenderer getRender() {
        renderer = new XYMultipleSeriesRenderer();// 设置描绘器
        setRenderer(renderer, colors, styles);
        renderer.setPointSize(5.5f);
        setChartSettings(renderer, chartLineTitle, xMessage, yMessage, 0.0,
                yMin, yMax, Color.LTGRAY, Color.LTGRAY);// 设置图表的X轴，Y轴,标题
        renderer.setXLabels(0);// 取消x轴的数字,动态设置
        renderer.setYLabels(20);// Y轴均分10项
        renderer.setShowGrid(true);// 显示表格
        renderer.setXLabelsAlign(Align.RIGHT);// 右对齐
        renderer.setYLabelsAlign(Align.RIGHT);
        renderer.setZoomButtonsVisible(false);// 不显示放大缩小
        renderer.setClickEnabled(true);// 不允许放大或缩小
        renderer.setPanEnabled(false, false);// 上下左右都不可以移动
        renderer.setBarSpacing(0.5);
        return renderer;
    }

    // 图表样式设置
    protected void setChartSettings(XYMultipleSeriesRenderer renderer,
                                    String title, String xTitle, String yTitle, double xMin,
                                    double yMin, double yMax, int axesColor, int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);// X轴标题
        renderer.setYTitle(yTitle);// Y轴标题
        // renderer.setXAxisMin(xMin);// X最小值
        renderer.setYAxisMin(yMin);// Y最小值
        renderer.setYAxisMax(yMax);// Y最小值
        renderer.setAxesColor(axesColor);// X轴颜色
        renderer.setLabelsColor(labelsColor);// Y轴颜色
    }

    /**
     * 设置描绘器属性
     */
    protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors,
                               PointStyle[] styles) {
        renderer.setAxisTitleTextSize(30);
        renderer.setBackgroundColor(Color.WHITE);
        renderer.setChartTitleTextSize(30);
        renderer.setLabelsTextSize(30);
        renderer.setLegendTextSize(30);
        renderer.setPointSize(5f);
        renderer.setMargins(new int[]{10, 100, 100, 0});// 上,左,下,右
        for (int i = 0; i < titles.length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            r.setDisplayChartValues(true);
            r.setLineWidth(2f);// 宽度
            r.setFillPoints(true);// 完全填充
            r.setChartValuesSpacing(3);
            renderer.addSeriesRenderer(r);
        }
    }

    /**
     * 设置点集
     */
    public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles,
                            int scale) {
        for (int i = 0; i < titles.length; i++) {
            series = new XYSeries(titles[i], scale);
            seriesList.add(series);
            dataset.addSeries(series);
        }
    }


    protected void destroy() {
        if (timer != null) {
            timer.cancel();
        }
    }}