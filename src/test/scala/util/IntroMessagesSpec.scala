package util

import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class IntroMessagesSpec extends AnyWordSpec with BeforeAndAfter {

    before {
        IntroMessages.loadMessages()
    }

    "IntroMessages" should {
        "load messages correctly" in {
            IntroMessages.getRandomMessage should not be empty
        }

        "return a random message" in {
            val message = IntroMessages.getRandomMessage
            message should not be empty
        }

        "return None for an unknown input" in {
            val input = "unknown"
            val specificMessage = ErrorMessages.getSpecificMessage(input)
            specificMessage should be (empty)
        }
    }
}