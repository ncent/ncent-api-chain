package main.chain

import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.amazonaws.services.simpledb.AmazonSimpleDB
import com.amazonaws.services.simpledb.model.PutAttributesRequest
import com.amazonaws.services.simpledb.model.SelectRequest
import framework.chain.*
import framework.models.BaseObject
import main.daos.Transaction
import org.joda.time.DateTime
import kotlin.reflect.KClass

class AwsSimpleDbLedger<T: BaseObject>(
    private val db: AmazonSimpleDB,
    private val clazz: KClass<T>,
    override val constructor: Constructor<T>
): Ledger<T> {

    @Throws(AmazonServiceException::class, AmazonClientException::class)
    override fun read(query: String): List<T> {
        val result = db.select(SelectRequest(query))
        return result.items.map { constructor.construct(clazz, it) }
    }

    @Throws(AmazonServiceException::class, AmazonClientException::class)
    override fun write(value: T) {
        db.putAttributes(PutAttributesRequest(
            Transaction::class.simpleName,
            "${value.javaClass.simpleName}_${value.id}_${DateTime.now().millis}",
            value.getAttributes()
        ))
    }
}