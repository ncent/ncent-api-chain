package framework.models

import com.amazonaws.services.simpledb.model.ReplaceableAttribute
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.*

fun currentUtc(): DateTime = DateTime.now(DateTimeZone.UTC)

// TODO: Change how we generate the UUID
abstract class BaseEntity(
    val id: String = UUID.randomUUID().toString(),
    val createdAt: DateTime = currentUtc()
): BaseObject {

    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        return mutableListOf(
            ReplaceableAttribute("id", id, true),
            ReplaceableAttribute("createdAt", createdAt.toString(), true)
        )
    }

    override fun toMap(): MutableMap<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        map["id"] = id
        map["createdAt"] = createdAt.toString()
        return map
    }
}

abstract class BaseEntityNamespace(
    val id: String = UUID.randomUUID().toString(),
    val createdAt: DateTime = currentUtc()
): BaseNamespace {
    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        return mutableListOf(
            ReplaceableAttribute("id", id, true),
            ReplaceableAttribute("createdAt", createdAt.toString(), true)
        )
    }

    override fun toMap(): MutableMap<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        map["id"] = id
        map["createdAt"] = createdAt.toString()
        return map
    }
}

interface BaseNamespace {
    fun toMap(): MutableMap<String, Any?>
    fun getAttributes(): MutableList<ReplaceableAttribute>
}

interface BaseObject {
    fun toMap(): MutableMap<String, Any?>

    fun getAttributes(): MutableList<ReplaceableAttribute>
}
