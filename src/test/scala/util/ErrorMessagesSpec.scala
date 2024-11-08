package util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.should.Matchers._

class ErrorMessagesSpec extends AnyWordSpec with BeforeAndAfter {

    before {
        ErrorMessages.loadMessages(familyFriendly = true)
    }

    "ErrorMessages" should {
        "load messages correctly" in {
            ErrorMessages.getRandomMessage should not be empty
        }

        "return a random message" in {
            val message = ErrorMessages.getRandomMessage
            message should not be empty
        }

        "return a specific message for a known input" in {
            val input = "dick"
            val specificMessage = ErrorMessages.getSpecificMessage(input)
            specificMessage should contain ("Bro what?")
        }

        "return None for an unknown input" in {
            val input = "unknown"
            val specificMessage = ErrorMessages.getSpecificMessage(input)
            specificMessage should be (empty)
        }
    }
}