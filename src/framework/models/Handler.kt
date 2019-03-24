package kotlinserverless.framework.models

import com.amazonaws.auth.PropertiesCredentials
import kotlinserverless.framework.dispatchers.RequestDispatcher
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.simpledb.AmazonSimpleDBClient
import com.amazonaws.services.simpledb.model.CreateDomainRequest
import com.bugsnag.Bugsnag
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.log4j.BasicConfigurator
import org.apache.log4j.LogManager

open class Handler: RequestHandler<Map<String, Any>, ApiGatewayResponse> {

  var requestDispatcher: RequestDispatcher = RequestDispatcher()

  constructor() {
    BasicConfigurator.configure()
    connectToLedger()
  }

  constructor(test: Boolean = false) {
    if(!test!!) {
      BasicConfigurator.configure()
    }
    connectToLedger()
  }

  override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {

    var status = 500
    var body: Any? = null

    try {
      body = requestDispatcher.locate(ApiGatewayRequest(input, context))

      status = when (body == null || body == true) {
        true -> 204
        false -> 200
      }
    }
    catch (e: MyException) {
      log(e, e.message)
      status = e.code
      body = e.message
    }
    catch (e: Throwable) {
      log(e, e.message)
      status = 500
      body = "Internal server error " + e.message.toString() + "\n" + e.stackTrace.map { "\n"+it.toString() }
    }
    finally {
      return build {
        statusCode = status
        rawBody = body
      }
    }
  }

  companion object {
    lateinit var ledger: AmazonSimpleDBClient

    inline fun build(block: ApiGatewayResponse.Builder.() -> Unit) = ApiGatewayResponse.Builder().apply(block).build()
    private val LOG = LogManager.getLogger(this::class.java)!!
    val objectMapper = ObjectMapper()

    private var bugsnagInstance: Bugsnag? = null
    private fun bugsnag(): Bugsnag {
      if(bugsnagInstance == null) {
        bugsnagInstance = Bugsnag(System.getenv("bugsnag_api_key") ?: "local")
        bugsnagInstance!!.setNotifyReleaseStages("production", "development")
        bugsnagInstance!!.setReleaseStage(System.getenv("release_stage") ?: "local")
      }
      return bugsnagInstance!!
    }

    fun log(e: Throwable?, message: String?) {
      if(e != null) {
        LOG.error(message, e)
        bugsnag().notify(e)
      } else if(message != null) {
        LOG.info(message)
      }
    }

    // TODO figure out how to do this correctly
    fun connectToLedger(): AmazonSimpleDBClient {
      try {
        ledger = AmazonSimpleDBClient(PropertiesCredentials(this.javaClass.getResourceAsStream("AwsCredentials.properties")))
        ledger.createDomain(CreateDomainRequest("transaction"))
      } catch(e: Exception) {
        println(e.message)
      }
      return ledger
    }
  }
}