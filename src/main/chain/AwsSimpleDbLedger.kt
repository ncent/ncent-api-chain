package main.chain

import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.amazonaws.services.simpledb.model.PutAttributesRequest
import com.amazonaws.services.simpledb.model.SelectRequest
import framework.chain.*
import kotlinserverless.framework.models.Handler
import main.daos.Transaction
import org.joda.time.DateTime
import java.util.*

class AwsSimpleDbLedger<T: Transaction>(
    override val constructor: AwsTransactionConstructor<T>
): Ledger<T> {

    @Throws(AmazonServiceException::class, AmazonClientException::class)
    override fun read(query: () -> T): List<T> {
        val result = Handler.db.select(SelectRequest(query().getQuery()))
        return result.items.map { constructor.construct(it) }
    }

    @Throws(AmazonServiceException::class, AmazonClientException::class)
    override fun write(value: T) {
        Handler.db.putAttributes(PutAttributesRequest(
            Transaction::class.simpleName,
            "${DateTime.now().millis}_${UUID.randomUUID()}",
            value.getAttributes()
        ))
    }
}