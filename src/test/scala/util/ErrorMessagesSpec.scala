package util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ErrorMessagesSpec extends AnyWordSpec with Matchers {

    "ErrorMessages" should {

        "load family-friendly messages" in {
            ErrorMessages.loadMessages(familyFriendly = true)
            ErrorMessages.getRandomMessage should not be empty
        }

        "load non-family-friendly messages" in {
            ErrorMessages.loadMessages(familyFriendly = false)
            ErrorMessages.getRandomMessage should not be empty
        }

        "get a random message" in {
            ErrorMessages.loadMessages(familyFriendly = true)
            val message = ErrorMessages.getRandomMessage
            message should not be empty
        }

        "get a specific message" in {
            ErrorMessages.loadMessages(familyFriendly = true)
            val specificMessage = ErrorMessages.getSpecificMessage("dick")
            specificMessage shouldBe Some("Bro what?")
        }

        "return None for non-specific message" in {
            ErrorMessages.loadMessages(familyFriendly = true)
            val specificMessage = ErrorMessages.getSpecificMessage("nonexistent")
            specificMessage shouldBe None
        }

        "return a random message from the list" in {
            ErrorMessages.loadMessages(familyFriendly = true)
            val message = ErrorMessages.getRandomMessage
            ErrorMessages.getUsedMessages should contain(message)
        }

        "all messages are used" in {
            ErrorMessages.loadMessages(familyFriendly = true)
            val initialMessages = ErrorMessages.getUsedMessages.size
            while (ErrorMessages.getUsedMessages.size < initialMessages + 1) {
                ErrorMessages.getRandomMessage
            }
            ErrorMessages.getUsedMessages should not be empty
        }

        "reset used messages and get a new random message when all messages are used" in {
            ErrorMessages.loadMessages(familyFriendly = true)
            val initialMessages = ErrorMessages.getMessages.size
            println(s"Initial messages size: $initialMessages") // Debugging line
            // Use all messages
            for (_ <- 1 to initialMessages) {
                ErrorMessages.getRandomMessage
            }
            // At this point, all messages should be used
            // Get a new random message, which should reset the used messages
            val newMessage = ErrorMessages.getRandomMessage
            ErrorMessages.getUsedMessages should contain(newMessage)
        }
    }
}