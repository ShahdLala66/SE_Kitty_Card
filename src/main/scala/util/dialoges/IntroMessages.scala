package util.dialoges

import scala.annotation.tailrec
import scala.io.Source
import scala.util.Random

object IntroMessages {
    private var messages: List[String] = List()
    private var usedMessages: Set[String] = Set()
    
    def getMessages: List[String] = messages

    def loadMessages(): Unit = {
        val source = Source.fromResource("intro_messages.txt")
        messages = source.getLines().toList
        source.close()
    }

    @tailrec
    def getRandomMessage: String = {
        val availableMessages = messages.diff(usedMessages.toList)
        if (availableMessages.nonEmpty) {
            val message = availableMessages(Random.nextInt(availableMessages.length))
            usedMessages += message
            message
        } else {
            usedMessages = Set()
            getRandomMessage
        }
    }
}
