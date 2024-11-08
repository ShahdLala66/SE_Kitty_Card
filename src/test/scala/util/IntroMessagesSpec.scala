package util

import org.scalatest.BeforeAndAfter
import org.scalatest.wordspec.AnyWordSpec

class IntroMessagesSpec extends AnyWordSpec with BeforeAndAfter {

    before {
        IntroMessages.loadMessages()
    }

    "IntroMessages" should {
        "load messages correctly" in {
            assert(IntroMessages.getRandomMessage.nonEmpty)
        }

        "return a random message" in {
            val message = IntroMessages.getRandomMessage
            assert(message.nonEmpty)
        }

        "return None for an unknown input" in {
            val input = "unknown"
            val specificMessage = ErrorMessages.getSpecificMessage(input)
            assert(specificMessage.isEmpty)
        }
    }
}
