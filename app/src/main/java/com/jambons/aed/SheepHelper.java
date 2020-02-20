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
public class SheepHelper extends Contract {
    public static final String BINARY = "608060405260106000908155662386f26fc10000600155600a60025560045534801561002a57600080fd5b50610b6d8061003a6000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c806380a665981161005b57806380a66598146102225780638cc8b591146102ca578063adb0e41d146102e7578063fa7bd13d146100f85761007d565b80633e90ad18146100825780634f592bfc146100f8578063541ce3a714610112575b600080fd5b6100a86004803603602081101561009857600080fd5b50356001600160a01b0316610320565b60408051602080825283518183015283519192839290830191858101910280838360005b838110156100e45781810151838201526020016100cc565b505050509050019250505060405180910390f35b6101006103d5565b60408051918252519081900360200190f35b61012f6004803603602081101561012857600080fd5b50356103dc565b604051808060200186815260200185815260200184815260200180602001838103835288818151815260200191508051906020019080838360005b8381101561018257818101518382015260200161016a565b50505050905090810190601f1680156101af5780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b838110156101e25781810151838201526020016101ca565b50505050905090810190601f16801561020f5780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390f35b6102c86004803603602081101561023857600080fd5b81019060208101813564010000000081111561025357600080fd5b82018360208201111561026557600080fd5b8035906020019184600183028401116401000000008311171561028757600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061053c945050505050565b005b61012f600480360360208110156102e057600080fd5b5035610565565b610304600480360360208110156102fd57600080fd5b503561071a565b604080516001600160a01b039092168252519081900360200190f35b60608060066000846001600160a01b03166001600160a01b0316815260200190815260200160002054604051908082528060200260200182016040528015610372578160200160208202803883390190505b5090506000805b6003548110156103cc576000818152600560205260409020546001600160a01b03868116911614156103c457808383815181106103b257fe5b60209081029190910101526001909101905b600101610379565b50909392505050565b6004545b90565b600381815481106103e957fe5b60009182526020918290206005919091020180546040805160026001841615610100026000190190931692909204601f8101859004850283018501909152808252919350918391908301828280156104825780601f1061045757610100808354040283529160200191610482565b820191906000526020600020905b81548152906001019060200180831161046557829003601f168201915b505050505090806001015490806002015490806003015490806004018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105325780601f1061050757610100808354040283529160200191610532565b820191906000526020600020905b81548152906001019060200180831161051557829003601f168201915b5050505050905085565b600061054782610735565b60648106900390506002600161055f848484846107be565b50505050565b606060008060006060610576610a13565b6003878154811061058357fe5b600091825260209182902060408051600593909302909101805460026001821615610100026000190190911604601f8101859004909402830160c090810190925260a08301848152929390928492909184918401828280156106265780601f106105fb57610100808354040283529160200191610626565b820191906000526020600020905b81548152906001019060200180831161060957829003601f168201915b50505050508152602001600182015481526020016002820154815260200160038201548152602001600482018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106e65780601f106106bb576101008083540402835291602001916106e6565b820191906000526020600020905b8154815290600101906020018083116106c957829003601f168201915b5050509190925250508151604083015160608401516020850151608090950151929c919b5099509297509550909350505050565b6005602052600090815260409020546001600160a01b031681565b600080826040516020018082805190602001908083835b6020831061076b5780518252601f19909201916020918201910161074c565b6001836020036101000a0380198251168184511680821785525050505050509050019150506040516020818303038152906040528051906020012060001c905060015481816107b657fe5b069392505050565b60606040518060800160405280605d8152602001610adb605d91396040805160a081018252878152602080820188905261ffff808816938301939093529185166060820152608081018390526003805460018101825560009190915281518051949550919360059091027fc2575a0e9e593c00f959f8c92f12db2869c3395a3b0502d05e2516446f71f85b0192610859928492910190610a42565b5060208281015160018301556040830151600283015560608301516003830155608083015180516108909260048501920190610a42565b505060048054600190810190915560035460001901600081815260056020908152604080832080546001600160a01b03191632179055338352600682528083208054909501909455835183815293840189905261ffff80891660608601528716608085015260c08482018181528b51918601919091528a519395507f4879619f7ff9493ef67b03bd003b4128af5d0d3fc86f27ec4d03017d481365159486948c948c948c948c948c949093919260a085019260e0860192918b0191908190849084905b8381101561096b578181015183820152602001610953565b50505050905090810190601f1680156109985780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b838110156109cb5781810151838201526020016109b3565b50505050905090810190601f1680156109f85780820380516001836020036101000a031916815260200191505b509850505050505050505060405180910390a1505050505050565b6040518060a0016040528060608152602001600081526020016000815260200160008152602001606081525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610a8357805160ff1916838001178555610ab0565b82800160010185558215610ab0579182015b82811115610ab0578251825591602001919060010190610a95565b50610abc929150610ac0565b5090565b6103d991905b80821115610abc5760008155600101610ac656fe68747470733a2f2f696d6167652e7368757474657273746f636b2e636f6d2f696d6167652d766563746f722f637574652d66756e6e792d636172746f6f6e2d6b69636b2d73686565702d363030772d313537373238373231362e6a7067a264697066735822122072e78f3b2e0f51410aede53a0051356a39881a4efaabafb3c354a5e35f8ecc0564736f6c63430006010033";

    public static final String FUNC_CREATERANDOMSHEEP = "createRandomSheep";

    public static final String FUNC_FINDMYSHEEPTOTAL = "findMySheepTotal";

    public static final String FUNC_GETSHEEPBYID = "getSheepById";

    public static final String FUNC_GETSHEEPTOTAL = "getSheepTotal";

    public static final String FUNC_GETSHEEPSBYOWNER = "getSheepsByOwner";

    public static final String FUNC_SHEEPTOOWNER = "sheepToOwner";

    public static final String FUNC_SHEEPS = "sheeps";

    public static final Event NEWSHEEP_EVENT = new Event("NewSheep",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected SheepHelper(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SheepHelper(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SheepHelper(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SheepHelper(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
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

    public RemoteFunctionCall<TransactionReceipt> findMySheepTotal() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_FINDMYSHEEPTOTAL,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> getSheepById(BigInteger _sheepId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GETSHEEPBYID,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_sheepId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> getSheepTotal() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GETSHEEPTOTAL,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> getSheepsByOwner(String _owner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GETSHEEPSBYOWNER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _owner)),
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
    public static SheepHelper load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SheepHelper(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SheepHelper load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SheepHelper(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SheepHelper load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new SheepHelper(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SheepHelper load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SheepHelper(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SheepHelper> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SheepHelper.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SheepHelper> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SheepHelper.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<SheepHelper> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SheepHelper.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SheepHelper> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SheepHelper.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
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
