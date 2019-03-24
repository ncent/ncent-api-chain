package main.chain

import framework.chain.*
import main.daos.CryptoKeyPair
import main.daos.Transaction

class AwsSimpleDbLedgerClient<T: Transaction>(
    override val constructor: Constructor<T>,
    override val ledger: Ledger<T>,
    override val validator: Validator<T>
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