package main.chain

import com.amazonaws.services.simpledb.AmazonSimpleDB
import framework.chain.*
import main.daos.Transaction
import kotlin.reflect.KClass

class AwsSimpleDbLedgerClient<T: Transaction>(
    private val db: AmazonSimpleDB,
    private val clazz: KClass<T>,
    private val constructor: Constructor<T>,
    override val ledger: AwsSimpleDbLedger<T> = AwsSimpleDbLedger(db, clazz, constructor),
    override val validator: TransactionValidator<T> = TransactionValidator(ledger)
) : ReadableLedgerClient<T>, WritableLedgerClient<T> {

    override fun read(vararg kvp: Pair<String, String>): T {
        return readAll(*kvp).first()
    }

    override fun readAll(vararg kvp: Pair<String, String>): List<T> {
        // TODO: Add sorting
        return ledger.read(constructor.construct(*kvp))
    }
}