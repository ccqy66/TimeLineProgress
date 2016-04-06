####TimeLineProgress
###简介
一个可以作为时间线所使用的库，使用起来超级简单，但是效果却是很棒的，你可以用在计划表，并支持二级阶段计划。你也可以用作招聘进度，并可以细粒化的指示当前阶段。总之它有很广的应用场景。本库的灵感来源于阿里巴巴的实习生招聘的后台进度条，想着如果能在手机端有一个类似的功能块的话，一定是不错的，所以花了两天的时间绘制了出来。在实现的过程中，我尽力的解决了所遇到的BUG，但是使用过程中也难免遇到一些其他的问题，欢迎指出。
###如何使用
- 首先引入TimeLineProgress.java到你的项目中，并导入自定义属性文件。
- 在xml文件中导入之
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="com.wolfcoder.mystudy.MainActivity">
    <com.wolfcoder.mystudy.view.TimeLineProgress
        android:layout_width="match_parent"
        android:layout_height="100dp" />
</LinearLayout>
```
- 在MainActivity文件中初始化必要的数据
- 初始化时间点(`ArrayList<String>`)
```
		ArrayList<String> timePosition = new ArrayList<>();
        timePosition.add("笔试");
        timePosition.add("面试");
        timePosition.add("电面");
        timePosition.add("offer");
```
- 初始化二级时间点(`ArrayList<ArrayList<String>>`)
```
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
        subTimePositionMsg.add(list);
        subTimePositionMsg.add(list1);
        subTimePositionMsg.add(null);
        subTimePositionMsg.add(list3);
```
######NOTICE:如何某个时间点没有二级时间点，就令其为null，不能省略不写

###更换状态
- 实际上，上面的步骤已经可以生成一个时间线进度条了，但是为了能指示时间线当前所处的位置，你还需要调用`setCurrentStatus(index,subIndex)`来指示当前的位置。
如我想让当前位置处于，面试下的笔试状态。下标是从0开始的
```
progress.setCurrentStatus(2,1);
```
-如想让状态停止在某状态下，需要调用`setStop(index)`
例如我想在面试状态下停止
```
progress.setStop(2);
```

###自定义属性
```
		<attr name="reachColor" format="color"/> 到达部分的颜色
        <attr name="unReachColor" format="color"/> 未到达部分的颜色
        <attr name="clockIconColor" format="color"/> 最左端时钟图标的颜色
        <attr name="stopColor" format="color"/> 停止状态下的ICON
        <attr name="clockWidth" format="dimension"/> 最左端时钟的宽度
        <attr name="timePositionWidth" format="dimension"/> 时间点的宽度
        <attr name="subTimePostionWidth" format="dimension"/> 二级时间点的半径
```