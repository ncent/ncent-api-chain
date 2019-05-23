package test.unit.services.user_acco
import io.kotlintest.*
import io.kotlintest.specs.WordSpec
import io.mockk.junit5.MockKExtension
import kotlinserverless.framework.models.DuplicateEntryError
import org.junit.jupiter.api.extension.ExtendWith
import main.services.user_account.GenerateUserAccountService
import kotlinserverless.framework.models.Handler
import kotlinserverless.framework.models.InvalidArguments

@ExtendWith(MockKExtension::class)
class GenerateUserAccountServiceTest : WordSpec() {

    override fun beforeTest(description: Description): Unit {
        Handler.connectToLedger()
    }

    override fun afterTest(description: Description, result: TestResult) {
        Handler.disconnectAndDropLedger()
    }

    init {
        "calling execute with a valid user account" should {
            "return the newly created user" {
                var result = GenerateUserAccountService.execute("dev@ncnt.io", "dev", "ncnt")
                result.value shouldNotBe null
            }
        }

        "calling execute with an invalid user account" should {
            "return a fail result" {
                val exception = shouldThrow<InvalidArguments> {
                    GenerateUserAccountService.execute("BADEMAIL", "dev", "ncnt")
                }
                exception.message shouldBe "Must enter a valid email address"
            }
        }

        "calling execute with an already existing user account" should {
            "return a fail result" {
                GenerateUserAccountService.execute("dev@ncnt.io", "dev", "ncnt")
                val exception = shouldThrow<DuplicateEntryError> {
                    GenerateUserAccountService.execute("dev@ncnt.io", "dev", "ncnt")
                }
                exception.message shouldBe "Entry already exists"
            }
        }
    }
}