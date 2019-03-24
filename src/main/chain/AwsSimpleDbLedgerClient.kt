package main.chain

import framework.chain.*
import main.daos.CryptoKeyPair
import main.daos.Transaction
import main.helpers.TransactionConstructor
import main.helpers.TransactionValidator

class AwsSimpleDbLedgerClient<T: Transaction>(
    override val constructor: TransactionConstructor<T>,
    override val ledger: AwsSimpleDbLedger<T>,
    override val validator: TransactionValidator<T>
) : ReadableLedgerClient<T>, WritableLedgerClient<T> {

    override fun read(address: String, vararg kvp: Pair<String, String>): T {
        throw NotImplementedError()
    }

    override fun readAll(address: String, vararg kvp: Pair<String, String>): List<T> {
        throw NotImplementedError()
    }

    override fun write(keyPair: CryptoKeyPair, to: String?, vararg kvp: Pair<String, String>): T {
        throw NotImplementedError()
        return super.write(keyPair, to, *kvp)
    }
}