package com.feng.diningphilosophers;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/27
 */
public class Philosopher extends Thread {

    private int id;                 // 哲学家编号
    private int leftChopstickId;    // 左边筷子的编号
    private int rightChopstickId;   // 右边筷子的编号
    private Chopstick chopstick;    // 筷子类，包含取筷子、放回筷子等操作
    private PhilosopherListener mListener;
    private boolean finish = false;

    public interface PhilosopherListener {
        void stateChanged(int id, String state);
    }

    public Philosopher(int id, int leftChopstickId, int rightChopstickId,
                       Chopstick chopstick, PhilosopherListener mListener) {
        this.id = id;
        this.leftChopstickId = leftChopstickId;
        this.rightChopstickId = rightChopstickId;
        this.chopstick = chopstick;
        this.mListener = mListener;
    }

    @Override
    public void run() {
        while (!finish) {
            thinking();
            mListener.stateChanged(id, "取筷子中");
            chopstick.take(leftChopstickId, rightChopstickId);
            eating();
            chopstick.put(leftChopstickId, rightChopstickId);
        }
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    private void thinking() {
        mListener.stateChanged(id, "正在思考");
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void eating() {
        mListener.stateChanged(id, "正在进餐");
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
