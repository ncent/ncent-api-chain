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
    override val validator: TransactionValidator<T> = TransactionValidator(ledger)
) : ReadableLedgerClient<T>, WritableLedgerClient<T> {

    override fun read(vararg kvp: Pair<String, String>): T {
        // TODO: Add sorting and limit to query
        val query = constructor.construct(*kvp)
        return ledger.read(query).first()
    }

    override fun readAll(vararg kvp: Pair<String, String>): List<T> {
        return ledger.read(constructor.construct(*kvp))
    }
}