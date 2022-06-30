package com.exapmple.event;

import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author zhengenshen@gmail.com
 * @date 2022/1/17 14:35
 */
@Slf4j
public class ChainEventSubscribe implements BaseEventSubscribe, Runnable {


    private final static int BLOCK_LIMIT = 5000;
    private final static int INTERVAL = 3;
    private final Web3j web3j;
    private final List<String> addressList;
    private final String[] eventHashArr;
    private final Map<String, EventDataParser> eventParserMap;
    private Consumer<ChainMessage> consumer;
    private long current;
    private final Thread thread;


    public ChainEventSubscribe(String bscChainAddress, List<String> addressList, List<EventDataParser> events, long startBlockNumber) {
        this.web3j = Web3j.build(new HttpService(bscChainAddress));
        this.current = startBlockNumber;
        this.addressList = addressList;
        this.eventParserMap = new HashMap<>(events.size());
        for (EventDataParser event : events) {
            if (event.getEvent() != null) {
                eventParserMap.put(event.getEventHash(), event);
            }
        }

        // 监听多个topic
        this.eventHashArr = events.stream().map(EventDataParser::getEventHash).toArray(String[]::new);
        // 使用一个线程轮询链事件
        this.thread = new Thread(this, "event-loop");
        log.info("Rpc address: {} startBlockNumber: {} address: {}", bscChainAddress, startBlockNumber, addressList);

    }

    @Override
    public void run(Consumer<ChainMessage> consumer) {
        this.consumer = consumer;
        this.thread.start();
    }

    @Override
    public void destroy() {
    }

    @Override
    public void run() {
        for (; ; ) {
            try {
                getNewestBlockAndSubscribe();
                TimeUnit.SECONDS.sleep(INTERVAL);
            } catch (InterruptedException ex) {
                log.warn(ex.getMessage());
            } catch (Throwable te) {
                te.printStackTrace();
            }
        }
    }


    /**
     * 查询最新区块并且订阅
     */
    private void getNewestBlockAndSubscribe() {
        try {
            EthBlockNumber block = web3j.ethBlockNumber().send();
            long chainLastBlockNumber = block.getBlockNumber().longValue();
            long from = current + 1;
            long to = from + BLOCK_LIMIT;
            if (to > chainLastBlockNumber) {
                to = chainLastBlockNumber;
            }
            if (to - from < 0) {
                return;
            }
            log.info("from: {} to: {} count: {}", from, to, to - from);

            EthFilter filter = new EthFilter(new DefaultBlockParameterNumber(from), new DefaultBlockParameterNumber(to), addressList);
            filter.addOptionalTopics(eventHashArr);
            EthLog ethLog = web3j.ethGetLogs(filter).send();
            if (ethLog != null && ethLog.getLogs() != null) {
                for (var logs : ethLog.getLogs()) {
                    Log l = (Log) logs;
                    parse(l);
                }
            }
            current = to;
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }


    private void parse(Log respLog) {
        String topicHash = respLog.getTopics().get(0);
        EventDataParser eventDataParser = eventParserMap.get(topicHash);
        if (eventDataParser == null) {
            log.error("unsupported topic: {}", topicHash);
            return;
        }
        ChainMessage msg = eventDataParser.parse(respLog);
        if (msg == null) {
            return;
        }
        try {
            consumer.accept(msg);
        } catch (Exception e) {
            log.error("consumer msg error.", e);
        }
    }
}
