package com.exapmple.event;

import java.util.function.Consumer;

/**
 * @author zhengenshen@gmail.com
 * @date 2022/1/17 15:30
 */
public interface BaseEventSubscribe {

    /**
     * 传入一个用来消费BaseMessage的实现
     *
     * @param consumer 消费BaseMessage
     */
    void run(Consumer<ChainMessage> consumer);

    /**
     * 资源清理
     */
    void destroy();
}
