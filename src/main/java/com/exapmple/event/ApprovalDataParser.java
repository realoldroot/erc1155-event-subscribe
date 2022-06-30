package com.exapmple.event;

import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.tx.Contract;

import java.util.Arrays;

/**
 * 授权事件
 *
 * @author zhengenshen@gmail.com
 * @date 2022/4/2 15:49
 */
public class ApprovalDataParser implements EventDataParser {

    @Override
    public ChainMessage parse(Log eventData) {
        EventValues eventValues = Contract.staticExtractEventParameters(getEvent(), eventData);
        Address address = (Address) eventValues.getIndexedValues().get(0);
        Address operator = (Address) eventValues.getIndexedValues().get(1);
        Bool approved = (Bool) eventValues.getNonIndexedValues().get(0);
        ApprovalEventMessage t = new ApprovalEventMessage();
        t.setBlockNumber(eventData.getBlockNumber().longValue());
        t.setTransactionHash(eventData.getTransactionHash());
        t.setAddress(address.getValue());
        t.setOperator(operator.getValue());
        t.setApproved(approved.getValue());
        return t;
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
