package main.daos

import com.amazonaws.services.simpledb.model.ReplaceableAttribute
import framework.models.BaseNamespace
import kotlinserverless.framework.models.Handler

class Healthcheck(
	val status: String,
	val message: String
): BaseNamespace() {
	override fun getAttributes(): MutableList<ReplaceableAttribute> {
		var list = super.getAttributes()
		list.add(ReplaceableAttribute("status", status, true))
		list.add(ReplaceableAttribute("message", message, true))
		list.add(ReplaceableAttribute("dburl", Handler.db.listDomains().domainNames.toString(), true))
		return list
	}

	override fun toMap(): MutableMap<String, Any?> {
		var map = super.toMap()
		map.put("status", status)
		map.put("message", message)
		map.put("dburl", Handler.db.listDomains().domainNames.toString())
		return map
	}
}