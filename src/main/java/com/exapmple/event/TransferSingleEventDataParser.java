package com.exapmple.event;

import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.tx.Contract;

import java.util.Arrays;
import java.util.List;

/**
 * @author zhengenshen@gmail.com
 * @date 2022/1/17 11:23
 */
@Slf4j
public class TransferSingleEventDataParser implements EventDataParser {

    @Override
    public ChainMessage parse(Log eventData) {
        EventValues eventValues = Contract.staticExtractEventParameters(getEvent(), eventData);
        Address operator = (Address) eventValues.getIndexedValues().get(0);
        Address from = (Address) eventValues.getIndexedValues().get(1);
        Address to = (Address) eventValues.getIndexedValues().get(2);
        Uint256 id = (Uint256) eventValues.getNonIndexedValues().get(0);
        TransferEventMessage ctb = new TransferEventMessage();
        ctb.setBlockNumber(eventData.getBlockNumber().longValue());
        ctb.setTransactionHash(eventData.getTransactionHash());
        ctb.setOperator(operator.getValue());
        ctb.setFrom(from.getValue());
        ctb.setTo(to.getValue());
        ctb.setTokenIds(List.of(id.getValue().longValue()));
        return ctb;
    }

    @Override
    public Event getEvent() {
        return event;
    }


    /**
     * ERC1155 TransferSingle事件
     * TransferSingle(address operator, address from, address to, uint256 id, uint256 value)
     * 当token id value从from转移到to时发出operator
     */
    private final Event event = new Event("TransferSingle",
            Arrays.asList(
                    new TypeReference<Address>(true) {
                    },
                    new TypeReference<Address>(true) {
                    },
                    new TypeReference<Address>(true) {
                    },
                    new TypeReference<Uint256>() {
                    },
                    new TypeReference<Uint256>() {
                    }
            )
    );
}
