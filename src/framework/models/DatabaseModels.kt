package framework.models

import com.amazonaws.services.simpledb.model.ReplaceableAttribute
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.*

fun currentUtc(): DateTime = DateTime.now(DateTimeZone.UTC)

// Base entity are the main objects put into the database
// Currently only Transactions exist as this type
// TODO: Change how we generate the UUID
abstract class BaseEntity(
    override val id: String = UUID.randomUUID().toString(),
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

// Base entity namespace's are sub objects that are stored inside of BaseEntity's
// Currently these are stored in Transaction > Action > data
abstract class BaseEntityNamespace(
    override val id: String = UUID.randomUUID().toString(),
    val createdAt: DateTime = currentUtc()
): BaseObject {

    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        val list = super.getAttributes()
        list.add(ReplaceableAttribute("${this.javaClass.simpleName}_id", id, true))
        list.add(ReplaceableAttribute("${this.javaClass.simpleName}_createdAt", createdAt.toString(), true))
        return list
    }

    override fun toMap(): MutableMap<String, Any?> {
        val map = super.toMap()
        map["${this.javaClass.simpleName}_id"] = id
        map["${this.javaClass.simpleName}_createdAt"] = createdAt.toString()
        return map
    }
}

interface BaseObject {
    val id: String
    fun getAttributes(): MutableList<ReplaceableAttribute> {
        return mutableListOf()
    }

    fun toMap(): MutableMap<String, Any?> {
        return mutableMapOf()
    }

    fun getQuery(): String {

    }
}
