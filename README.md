# Crosschain voting dapp

The following is a cross chain DApp to showcase ICON XCall, it consists of a set of smart contracts deployed on Berlin (ICON) and Sepolia (Ethereum) to cast votes on the ICON chain and it will keep a ledger of the votes on both chains.

## Setup

Create a `.env` file in the root folder and place the private keys of a wallet on Berlin and Sepolia with enough balance to deploy and run the DApp test scenarios.
```bash
PK_BERLIN="ICON WALLET PRIVATE KEY"
PK_SEPOLIA="ETHEREUM WALLET PRIVATE KEY"
```

Run command to install node packages.
```
npm install
```

## Compile contracts

To run the project you first need to compile both the java and solidity contracts.

To compile the solidity contract run the following command:
```
npm run compile-solidity
```

To compile the Java contracts move into the `./contracts/jvm/` folder and run the compilation command with gradle.
```
cd contracts/jvm/
./gradlew b
./gradlew op
```

## Run main script

Once you had compiled the solidity and java contracts you can run the main script with the following command
```
npm run start
```

If this is the first time running the command, the compiled solidity and java contracts will be deployed to berlin and sepolia networks, after that the script will execute a full example by casting a vote on the Berlin chain and then checking the votes ledger on the Sepolia network.

## Further resources

- [btp](https://github.com/icon-project/btp2) - Blockchain Transmission Protocol, which is the ICON Foundation's core interoperability product
- [iconloop/btp2-testnet](https://github.com/iconloop/btp2-testnet) - Information on the BTP network connected to the ICON Berlin TestNet.
- [fidelve/xcall-sample-dapp](https://github.com/FidelVe/xcall-sample-dapp) - xCall sample dApp written in javascript.
- [R0bi7/xCall-testing-JVM](https://github.com/R0bi7/xCall-testing-JVM) - Sample JVM smart contract that interacts with xCall.
- [R0bi7/xCall-testing-EVM](https://github.com/R0bi7/xCall-testing-EVM) - Sample EVM smart contract that interacts with xCall.
- [R0bi7/xCall-testing-dApp](https://github.com/R0bi7/xCall-testing-dApp/tree/master) - Sample DApp that interacts with xCall.
