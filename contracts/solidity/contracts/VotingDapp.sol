// contracts/VotingDapp.sol
// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./interfaces/ICallService.sol";
import "./interfaces/ICallServiceReceiver.sol";

/**
 * @title VotingDapp
 * @notice A simple voting dapp that can be called from another chain
 */
contract VotingDapp is ICallServiceReceiver {
  struct Votes {
    uint256 countOfYes;
    uint256 countOfNo;
  }

  Votes public votes;
  address private callSvc;
  uint256 private lastId;
  struct RollbackData {
      uint256 id;
      bytes rollback;
      uint256 ssn;
  }
  mapping(uint256 => RollbackData) private rollbacks;

  /*
   * @notice Constructor
   * @param _callService The address of the Call Message Service
   */
  constructor(address _callService) {
    votes.countOfYes = 0;
    votes.countOfNo = 0;
    callSvc = _callService;
  }

  /**
   * @notice Returns the number of yes and no votes
   * @return The number of yes and no votes
   */
  function getVotes() public view returns (uint256, uint256) {
    return (votes.countOfYes, votes.countOfNo);
  }

  /**
   * @notice add a yes vote
   */
  function addYesVote() internal {
    votes.countOfYes++;
  }

  /**
   * @notice add a no vote
   */
  function addNoVote() internal {
    votes.countOfNo++;
  }

  /**
   * @notice modifier to restrict access to the Call Message Service
   */
  modifier onlyCallService() {
      require(msg.sender == callSvc, "OnlyCallService");
      _;
  }

  /**
   * @notice Returns the address of the Call Message Service
   * @return The address of the Call Message Service
   */
  function getCallService() public view returns (address) {
      return callSvc;
  }

  /**
   * @notice compares two strings
   * @param _base The base string
   * @param _value The string to compare
   * @return True if the strings are equal, false otherwise
   */
  function compareTo(
      string memory _base,
      string memory _value
  ) internal pure returns (bool) {
      if (
          keccak256(abi.encodePacked(_base)) ==
          keccak256(abi.encodePacked(_value))
      ) {
          return true;
      }
      return false;
  }

  /**
     @notice Handles the call message received from the source chain.
     @dev Only called from the Call Message Service.
     @param _from The BTP address of the caller on the source chain
     @param _data The calldata delivered from the caller
   */
  function handleCallMessage(
      string calldata _from,
      bytes calldata _data
  ) external override onlyCallService {
      string memory msgData = string(_data);
      emit MessageReceived(_from, _data, msgData);
      if (compareTo("revertMessage", msgData)) {
          revert("revertFromDApp");
      }
      if (compareTo("voteYes", msgData)) {
          addYesVote();
      } else if (compareTo("voteNo", msgData)) {
          addNoVote();
      }
  }

  /**
   * @notice event emitted when a call message is received
   * @dev Only called from the Call Message Service.
   * @param _from The BTP address of the caller on the source chain
   * @param _data The calldata delivered from the caller
   * @param msgData The calldata converted to a string
   */
  event MessageReceived(
      string _from,
      bytes _data,
      string msgData
  );
}
