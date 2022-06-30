package com.joygame.joytd.nft;

import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.tx.Contract;

import java.util.Arrays;

/**
 * NFT授权事件
 *
 * @author zhengenshen@gmail.com
 * @date 2022/4/2 15:49
 */
public class NftApprovalDataParser implements DataParser {

    @Override
    public ChainMessage parse(Log respLog) {
        EventValues eventValues = Contract.staticExtractEventParameters(getEvent(), respLog);
        Address address = (Address) eventValues.getIndexedValues().get(0);
        Address operator = (Address) eventValues.getIndexedValues().get(1);
        Bool approved = (Bool) eventValues.getNonIndexedValues().get(0);
        NftApprovalEventMessage ctb = new NftApprovalEventMessage();
        ctb.setBlockNumber(respLog.getBlockNumber().longValue());
        ctb.setTransactionHash(respLog.getTransactionHash());
        ctb.setAddress(address.getValue());
        ctb.setOperator(operator.getValue());
        ctb.setApproved(approved.getValue());
        return ctb;
    }

    @Override
    public Event getEvent() {

        return event;
    }


    /**
     * event ApprovalForAll(address indexed account, address indexed operator, bool approved);
     */
    private final Event event = new Event("ApprovalForAll",
            Arrays.asList(
                    new TypeReference<Address>(true) {
                    },
                    new TypeReference<Address>(true) {
                    },
                    new TypeReference<Bool>(false) {
                    }
            )
    );

}
