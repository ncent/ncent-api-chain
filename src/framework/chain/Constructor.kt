package framework.chain

import com.amazonaws.services.simpledb.model.Item
import framework.models.BaseObject
import main.daos.ActionType
import main.daos.CryptoKeyPair
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

interface Constructor<T: BaseObject> {
    // Used to construct a new transaction
    // Each transaction type should have it's own constructor
    fun construct(keyPair: CryptoKeyPair, to: String?, actionType: ActionType, data: T): T

    fun construct(clazz: KClass<T>, item: Item): T {
        val attributes = item.attributes
        val constructor = clazz.primaryConstructor!!
        var constructorParams = mutableMapOf<KParameter, Any?>()
        attributes.forEach { attribute ->
            val param = constructor.parameters.find { it.name == attribute.name }!!
            constructorParams[param] = attribute.value
        }
        return constructor.callBy(constructorParams)
    }
}