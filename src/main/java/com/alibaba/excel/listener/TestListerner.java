package com.alibaba.excel.listener;

public class TestListerner {

    public static void main(String[] args) {
        Robot robot = new Robot();
        robot.registerListener(new MyRobotListener());
        robot.dancing();
    }

}
