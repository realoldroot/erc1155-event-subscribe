package com.exapmple.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * nft交易事件消息
 *
 * @author zhengenshen@gmail.com
 * @date 2021/11/1 13:05
 */
@Getter
@Setter
@ToString
public class TransferEventMessage implements ChainMessage {

    private long blockNumber;

    private String operator;

    private String from;

    private String to;

    private List<Long> tokenIds;

    private List<Long> countList;

    private String transactionHash;

}
