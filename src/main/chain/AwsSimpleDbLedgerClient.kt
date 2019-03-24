package main.chain

import framework.chain.*
import main.daos.CryptoKeyPair
import main.daos.Transaction

class AwsSimpleDbLedgerClient<T: Transaction>(
        override val ledger: AwsSimpleDbLedger<T> = AwsSimpleDbLedger(AwsTransactionConstructor()),
        override val validator: TransactionValidator<T> = TransactionValidator()
) : ReadableLedgerClient<T>, WritableLedgerClient<T> {

    override fun read(address: String, vararg kvp: Pair<String, String>): T {
        throw NotImplementedError()
    }

    override fun readAll(address: String, vararg kvp: Pair<String, String>): List<T> {
        throw NotImplementedError()
    }

    override fun write(keyPair: CryptoKeyPair, to: String?, vararg kvp: Pair<String, String>) {
        throw NotImplementedError()
        super.write(keyPair, to, *kvp)
    }
}