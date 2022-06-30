package com.exapmple.event;

import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.tx.Contract;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author zhengenshen@gmail.com
 * @date 2022/1/17 11:23
 */
@Slf4j
public class TransferBatchEventDataParser implements EventDataParser {


    @Override
    public ChainMessage parse(Log eventData) {
        EventValues eventValues = Contract.staticExtractEventParameters(getEvent(), eventData);
        Address operator = (Address) eventValues.getIndexedValues().get(0);
        Address from = (Address) eventValues.getIndexedValues().get(1);
        Address to = (Address) eventValues.getIndexedValues().get(2);
        DynamicArray<Uint256> ids = (DynamicArray<Uint256>) eventValues.getNonIndexedValues().get(0);
        DynamicArray<Uint256> counts = (DynamicArray<Uint256>) eventValues.getNonIndexedValues().get(1);
        TransferEventMessage t = new TransferEventMessage();
        t.setBlockNumber(eventData.getBlockNumber().longValue());
        t.setTransactionHash(eventData.getTransactionHash());
        t.setOperator(operator.getValue());
        t.setFrom(from.getValue());
        t.setTo(to.getValue());
        t.setTokenIds(ids.getValue().stream().map(i -> i.getValue().longValue()).collect(Collectors.toList()));
        t.setCountList(counts.getValue().stream().map(i -> i.getValue().longValue()).collect(Collectors.toList()));
        return t;
    }

    @Override
    public Event getEvent() {
        return event;
    }


    /**
     * ERC1155 TransferBatch事件
     * TransferBatch(address operator, address from, address to, uint256[] ids, uint256[] values)
     * <p>
     * 等效于多个TransferSingle事件
     */
    private final Event event = new Event("TransferBatch",
            Arrays.asList(
                    new TypeReference<Address>(true) {
                    },
                    new TypeReference<Address>(true) {
                    },
                    new TypeReference<Address>(true) {
                    },
                    new TypeReference<DynamicArray<Uint256>>() {
                    },
                    new TypeReference<DynamicArray<Uint256>>() {
                    }
            )
    );

}
