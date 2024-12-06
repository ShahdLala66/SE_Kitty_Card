package util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import util.Dialoges.IntroMessages

class IntroMessagesSpec extends AnyWordSpec with Matchers {

    "IntroMessages" should {

        "load messages" in {
            IntroMessages.loadMessages()
            IntroMessages.getMessages should not be empty
        }

        "get a random message" in {
            IntroMessages.loadMessages()
            val message = IntroMessages.getRandomMessage
            message should not be empty
        }

        "return a random message from the list" in {
            IntroMessages.loadMessages()
            val message = IntroMessages.getRandomMessage
            IntroMessages.getMessages should contain(message)
        }

        "reset used messages and get a new random message when all messages are used" in {
            IntroMessages.loadMessages()
            val initialMessages = IntroMessages.getMessages.size
            // Use all messages
            for (_ <- 1 to initialMessages) {
                IntroMessages.getRandomMessage
            }
            // At this point, all messages should be used
            // Get a new random message, which should reset the used messages
            val newMessage = IntroMessages.getRandomMessage
            IntroMessages.getMessages should contain(newMessage)
        }
    }
}