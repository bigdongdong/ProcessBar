package com.cxd.processbar;

/*
 * Create by chenxiaodong on 2020/1/26 0026 21:58
 */
public interface IProcessListener {

    /**
     * 进度变化
     * @param process
     */
    void onProcess(int process);

    /**
     * 手指按下时，有焦点状态
     */
    void onFingerDown();


    /**
     * 手指抬起，失去焦点
     */
    void onFingerUp();
}
