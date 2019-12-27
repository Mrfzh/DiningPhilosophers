package com.feng.diningphilosophers;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/27
 */
public class Chopstick {
    private boolean[] used = new boolean[5];

    /**
     * 取筷子 x 和筷子 y
     */
    public synchronized void take(int x, int y) {
        while (used[x] || used[y]) {    // 要拿的筷子正在使用
            try {
                wait(); // 等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 拿到筷子
        used[x] = true;
        used[y] = true;
    }

    /**
     * 放回筷子 x 和筷子 y
     */
    public synchronized void put(int x, int y) {
        used[x] = false;
        used[y] = false;
        notifyAll();    // 唤醒其他线程
    }
}
