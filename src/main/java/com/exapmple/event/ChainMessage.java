package com.exapmple.event;

/**
 * @author zhengenshen@gmail.com
 * @date 2022/2/15 16:09
 */
public interface ChainMessage extends BaseMessage {

    long getBlockNumber();
}
