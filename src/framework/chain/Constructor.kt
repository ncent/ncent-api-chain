package framework.chain

import com.amazonaws.services.simpledb.model.Item
import main.daos.Transaction
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

interface Constructor<T: Transaction> {
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

    fun construct(vararg kvp: Pair<String, String>): String
}