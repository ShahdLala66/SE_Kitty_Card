package util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.BeforeAndAfter

class ErrorMessagesSpec extends AnyWordSpec with BeforeAndAfter {

    before {
        ErrorMessages.loadMessages(familyFriendly = true)
    }

    "ErrorMessages" should {
        "load messages correctly" in {
            assert(ErrorMessages.getRandomMessage.nonEmpty)
        }

        "return a random message" in {
            val message = ErrorMessages.getRandomMessage
            assert(message.nonEmpty)
        }

        "return a specific message for a known input" in {
            val input = "dick"
            val specificMessage = ErrorMessages.getSpecificMessage(input)
            assert(specificMessage.contains("Bro what?"))
        }

        "return None for an unknown input" in {
            val input = "unknown"
            val specificMessage = ErrorMessages.getSpecificMessage(input)
            assert(specificMessage.isEmpty)
        }
    }
}