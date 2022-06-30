package com.exapmple.event;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.datatypes.Event;
import org.web3j.protocol.core.methods.response.Log;

/**
 * eth log 解析
 *
 * @author zhengenshen@gmail.com
 * @date 2022/1/17 13:46
 */
public interface EventDataParser {

    /**
     * 根据订阅不同的日志，分别解析数据
     *
     * @param eventData Response.Log
     * @return BaseMessage
     */
    ChainMessage parse(Log eventData);

    /**
     * 解析的事件
     *
     * @return Event
     */
    Event getEvent();


    /**
     * 事件hash
     *
     * @return eventHash
     */
    default String getEventHash() {
        Event event = getEvent();
        return event != null ? EventEncoder.encode(event) : "";
    }
}
