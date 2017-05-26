package com.example.chinesejar.sipdemo;

/**
 * Created by chinesejar on 17-5-23.
 */

public interface IMainView {

    void sendSuccess(String msg);

    void sendFailed(String msg);

    String getSrcIP();
}
