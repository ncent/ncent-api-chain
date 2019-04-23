package framework.models

import com.amazonaws.services.simpledb.model.ReplaceableAttribute
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.*

fun currentUtc(): DateTime = DateTime.now(DateTimeZone.UTC)

// TODO: Change how we generate the UUID
abstract class BaseEntity(
    val id: String = UUID.randomUUID().toString(),
    val createdAt: DateTime = currentUtc(),
    val updatedAt: DateTime? = null,
    val deletedAt: DateTime? = null
): BaseObject {

    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        return mutableListOf(
            ReplaceableAttribute("id", id, true),
            ReplaceableAttribute("createdAt", createdAt.toString(), true),
            ReplaceableAttribute("updatedAt", updatedAt?.toString(), true),
            ReplaceableAttribute("deletedAt", deletedAt?.toString(), true)
        )
    }

    override fun toMap(): MutableMap<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        map["id"] = id
        map["createdAt"] = createdAt.toString()
        if(updatedAt != null)
            map["updatedAt"] = updatedAt.toString()
        if(deletedAt != null)
            map["deletedAt"] = deletedAt.toString()
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
