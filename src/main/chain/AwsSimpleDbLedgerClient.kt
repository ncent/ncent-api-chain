package main.chain

import com.amazonaws.services.simpledb.AmazonSimpleDB
import framework.chain.*
import framework.models.BaseObject
import kotlin.reflect.KClass

class AwsSimpleDbLedgerClient<T: BaseObject>(
    private val db: AmazonSimpleDB,
    private val clazz: KClass<T>,
    private val constructor: Constructor<T>,
    override val ledger: AwsSimpleDbLedger<T> = AwsSimpleDbLedger(db, clazz, constructor),
    override val validator: TransactionValidator<T> = TransactionValidator()
) : ReadableLedgerClient<T>, WritableLedgerClient<T> {

    override fun read(address: String, vararg kvp: Pair<String, String>): T {
        throw NotImplementedError()
    }

    override fun readAll(address: String, vararg kvp: Pair<String, String>): List<T> {
        throw NotImplementedError()
    }
}