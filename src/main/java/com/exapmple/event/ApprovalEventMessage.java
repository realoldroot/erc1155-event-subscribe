package com.joygame.joytd.nft;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhengenshen@gmail.com
 * @date 2022/4/2 16:25
 */
@Getter
@Setter
@ToString
public class NftApprovalEventMessage implements ChainMessage {

    private long blockNumber;

    private String transactionHash;

    private String address;

    private String operator;

    private boolean approved;


    @Override
    public String getStreamKey() {
        return NftConstDefine.APPROVAL_STREAM_KEY;
    }
}
