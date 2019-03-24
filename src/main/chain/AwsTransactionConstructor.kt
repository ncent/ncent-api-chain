package main.chain

import com.amazonaws.services.simpledb.model.Item
import main.daos.Transaction

class AwsTransactionConstructor<T: Transaction>: TransactionConstructor<T> {
    fun construct(item: Item): T {
        throw NotImplementedError()
    }
}