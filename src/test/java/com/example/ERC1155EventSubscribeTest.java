package com.example;

import com.exapmple.event.ChainEventSubscribe;
import com.exapmple.event.EventDataParser;
import com.exapmple.event.TransferBatchEventDataParser;
import com.exapmple.event.TransferSingleEventDataParser;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhengenshen@gmail.com
 * @date 2022/6/30 10:14
 */
@Slf4j
public class ERC1155EventSubscribeTest {


    public static void main(String[] args) throws InterruptedException {

        String chainAddress = "https://data-seed-prebsc-1-s1.binance.org:8545/";
        String contractAddress = "0x5a5e7a203a9d11248c692f3504f35ba3135a127e";
        long startBlockNumber = 20637990L;
        //需要查询的事件日志

        List<EventDataParser> events = Stream.of(new TransferBatchEventDataParser(),
                new TransferSingleEventDataParser()).collect(Collectors.toList());

        ChainEventSubscribe subscribe = new ChainEventSubscribe(chainAddress, Arrays.asList(contractAddress), events, startBlockNumber);
        subscribe.run((msg) -> System.out.println("event " + msg));

        Thread.currentThread().join();
    }
}
