package app;

import foundation.icon.score.client.ScoreClient;
import score.Address;
import score.Context;
import score.VarDB;
import score.annotation.External;
import score.annotation.Optional;
import score.annotation.Payable;
import scorex.util.HashMap;

import java.math.BigInteger;
import java.util.Map;

@ScoreClient
public class VotingDapp {
    private final VarDB<BigInteger> countOfYes = Context.newVarDB("yes", BigInteger.class);
    private final VarDB<BigInteger> countOfNo = Context.newVarDB("no", BigInteger.class);
    private final VarDB<String> destinationBtpAddress = Context.newVarDB("btpAddress", String.class);
    private final VarDB<Address> xcallContractAddress = Context.newVarDB("xcall", Address.class);

    /*
     * Constructor
     * @param _sourceXCallContract: Address of the XCall contract
     * @param _destinationBtpAddress: BTP address of the destination chain
     */
    public VotingDapp(Address _sourceXCallContract, String _destinationBtpAddress) {
        this.destinationBtpAddress.set(_destinationBtpAddress);
        this.xcallContractAddress.set(_sourceXCallContract);
        this.countOfNo.set(BigInteger.ZERO);
        this.countOfYes.set(BigInteger.ZERO);
    }

    /*
     * Send call message to XCall contract
     * @param _data: Payload to be sent to XCall contract
     * @param _rollback: Rollback payload to be sent to XCall contract
     * @return: ID of the call message
     */
    private BigInteger _sendCallMessage(byte[] _data, @Optional byte[] _rollback) {
        Address xcallSourceAddress = this.xcallContractAddress.get();
        String _to = this.destinationBtpAddress.get();
        return Context.call(BigInteger.class, Context.getValue(), xcallSourceAddress, "sendCallMessage", _to, _data, _rollback);
    }

    /*
     * Public method to vote Yes
     */
    @Payable
    @External
    public void voteYes() {
        // Increase local count of Yes votes
        BigInteger sum = this.countOfYes.get().add(BigInteger.ONE);
        this.countOfYes.set(sum);

        // make call to xcall
        byte[] _rollback = null;
        String payload = "voteYes";
        byte[] bytePayload = payload.getBytes();

        BigInteger id = _sendCallMessage(bytePayload, _rollback);
        Context.println("sendCallMessage Response:" + id);
    }

    /*
     * Public method to vote No
     */
    @Payable
    @External
    public void voteNo() {
        // Increase local count of No votes
        BigInteger sum = this.countOfNo.get().add(BigInteger.ONE);
        this.countOfNo.set(sum);

        // make call to xcall
        byte[] _rollback = null;
        String payload = "voteNo";
        byte[] bytePayload = payload.getBytes();

        BigInteger id = _sendCallMessage(bytePayload, _rollback);
        Context.println("sendCallMessage Response:" + id);
    }

    /*
     * Public method to get the current vote count
     * @return: Map of vote counts
     */
    @External(readonly = true)
    public Map<String, BigInteger> getVotes() {
        Map<String, BigInteger> votesMap = new HashMap<>();
        votesMap.put("yes", this.countOfYes.get());
        votesMap.put("no", this.countOfNo.get());
        return votesMap;
    }

    /*
     * Public method to get the BTP address of the destination chain
     * @return: BTP address of the destination chain
     */
    @External(readonly = true)
    public String getDestinationBtpAddress() {
        return this.destinationBtpAddress.get();
    }

    /*
     * Public method to get the address of the XCall contract
     * @return: Address of the XCall contract
     */
    @External(readonly = true)
    public Address getXCallContractAddress() {
        return this.xcallContractAddress.get();
    }
}
