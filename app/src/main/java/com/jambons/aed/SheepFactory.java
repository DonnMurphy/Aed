package com.jambons.aed;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.12.
 */
@SuppressWarnings("rawtypes")
public class SheepFactory extends Contract {
    public static final String BINARY = "608060405260106000908155662386f26fc10000600155600a60025560045534801561002a57600080fd5b506107e78061003a6000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c8063541ce3a71461004657806380a6659814610156578063adb0e41d146101fe575b600080fd5b6100636004803603602081101561005c57600080fd5b5035610237565b604051808060200186815260200185815260200184815260200180602001838103835288818151815260200191508051906020019080838360005b838110156100b657818101518382015260200161009e565b50505050905090810190601f1680156100e35780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b838110156101165781810151838201526020016100fe565b50505050905090810190601f1680156101435780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390f35b6101fc6004803603602081101561016c57600080fd5b81019060208101813564010000000081111561018757600080fd5b82018360208201111561019957600080fd5b803590602001918460018302840111640100000000831117156101bb57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610397945050505050565b005b61021b6004803603602081101561021457600080fd5b50356103c0565b604080516001600160a01b039092168252519081900360200190f35b6003818154811061024457fe5b60009182526020918290206005919091020180546040805160026001841615610100026000190190931692909204601f8101859004850283018501909152808252919350918391908301828280156102dd5780601f106102b2576101008083540402835291602001916102dd565b820191906000526020600020905b8154815290600101906020018083116102c057829003601f168201915b505050505090806001015490806002015490806003015490806004018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561038d5780601f106103625761010080835404028352916020019161038d565b820191906000526020600020905b81548152906001019060200180831161037057829003601f168201915b5050505050905085565b60006103a2826103db565b6064810690039050600260016103ba84848484610464565b50505050565b6005602052600090815260409020546001600160a01b031681565b600080826040516020018082805190602001908083835b602083106104115780518252601f1990920191602091820191016103f2565b6001836020036101000a0380198251168184511680821785525050505050509050019150506040516020818303038152906040528051906020012060001c9050600154818161045c57fe5b069392505050565b60606040518060800160405280605d8152602001610755605d91396040805160a081018252878152602080820188905261ffff808816938301939093529185166060820152608081018390526003805460018101825560009190915281518051949550919360059091027fc2575a0e9e593c00f959f8c92f12db2869c3395a3b0502d05e2516446f71f85b01926104ff9284929101906106b9565b50602082810151600183015560408301516002830155606083015160038301556080830151805161053692600485019201906106b9565b505060048054600190810190915560035460001901600081815260056020908152604080832080546001600160a01b03191632179055338352600682528083208054909501909455835183815293840189905261ffff80891660608601528716608085015260c08482018181528b51918601919091528a519395507f4879619f7ff9493ef67b03bd003b4128af5d0d3fc86f27ec4d03017d481365159486948c948c948c948c948c949093919260a085019260e0860192918b0191908190849084905b838110156106115781810151838201526020016105f9565b50505050905090810190601f16801561063e5780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b83811015610671578181015183820152602001610659565b50505050905090810190601f16801561069e5780820380516001836020036101000a031916815260200191505b509850505050505050505060405180910390a1505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106106fa57805160ff1916838001178555610727565b82800160010185558215610727579182015b8281111561072757825182559160200191906001019061070c565b50610733929150610737565b5090565b61075191905b80821115610733576000815560010161073d565b9056fe68747470733a2f2f696d6167652e7368757474657273746f636b2e636f6d2f696d6167652d766563746f722f637574652d66756e6e792d636172746f6f6e2d6b69636b2d73686565702d363030772d313537373238373231362e6a7067a26469706673582212204d02f4b6883942903f1176e17fdaa2394ffa7779293cdca4fef2eb24f3f15e8164736f6c63430006010033";

    public static final String FUNC_CREATERANDOMSHEEP = "createRandomSheep";

    public static final String FUNC_SHEEPTOOWNER = "sheepToOwner";

    public static final String FUNC_SHEEPS = "sheeps";

    public static final Event NEWSHEEP_EVENT = new Event("NewSheep",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected SheepFactory(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SheepFactory(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SheepFactory(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SheepFactory(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<NewSheepEventResponse> getNewSheepEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NEWSHEEP_EVENT, transactionReceipt);
        ArrayList<NewSheepEventResponse> responses = new ArrayList<NewSheepEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NewSheepEventResponse typedResponse = new NewSheepEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sheepId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.name = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.dna = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.hp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.dp = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.imageAsset = (String) eventValues.getNonIndexedValues().get(5).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewSheepEventResponse> newSheepEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewSheepEventResponse>() {
            @Override
            public NewSheepEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NEWSHEEP_EVENT, log);
                NewSheepEventResponse typedResponse = new NewSheepEventResponse();
                typedResponse.log = log;
                typedResponse.sheepId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.name = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.dna = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.hp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.dp = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                typedResponse.imageAsset = (String) eventValues.getNonIndexedValues().get(5).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewSheepEventResponse> newSheepEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWSHEEP_EVENT));
        return newSheepEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> createRandomSheep(String _name) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATERANDOMSHEEP,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> sheepToOwner(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SHEEPTOOWNER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> sheeps(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SHEEPS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static SheepFactory load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SheepFactory(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SheepFactory load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SheepFactory(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SheepFactory load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new SheepFactory(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SheepFactory load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SheepFactory(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SheepFactory> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SheepFactory.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SheepFactory> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SheepFactory.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<SheepFactory> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SheepFactory.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SheepFactory> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SheepFactory.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class NewSheepEventResponse extends BaseEventResponse {
        public BigInteger sheepId;

        public String name;

        public BigInteger dna;

        public BigInteger hp;

        public BigInteger dp;

        public String imageAsset;
    }
}
