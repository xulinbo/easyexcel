package com.alibaba.excel.listener;

public class Robot
{
    private RobotListener robotListener;

    /**
     * 注册监听器
     * @param robotListener
     */
    public void registerListener(RobotListener robotListener){
        this.robotListener = robotListener;
    }

    public void working(){
        if (robotListener != null) {
            Even even = new Even();
            this.robotListener.working(even);
        }
        System.out.println("机器人开始工作");
    }
    public void dancing(){
        if (robotListener != null) {
            Even even = new Even();
            this.robotListener.working(even);
        }
        System.out.println("机器人开始跳舞");
    }
}
