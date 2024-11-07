package util

import scala.io.Source
import scala.util.Random

object ErrorMessages {
  private var messages: List[String] = List()
  private var usedMessages: Set[String] = Set()
  private var specificMessages: Map[String, String] = Map()

  def loadMessages(familyFriendly: Boolean): Unit = {
    val fileName = if (familyFriendly) "error_messages_friendly.txt" else "error_messages.txt"
    val source = Source.fromResource(fileName)
    messages = source.getLines().toList
    source.close()

    // Load specific messages
    specificMessages = Map(
      "dick" -> "Bro what?",
      "Zayne" -> "Corina go to work. Or is this someone else from the Blobby?",
      "69" -> "Arent you tired of this joke? 6966696969696969 LAUGH HAHAHAHA. Funny."
    )
  }

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

  def getSpecificMessage(input: String): Option[String] = {
    specificMessages.find { case (word, _) => input.contains(word) }.map(_._2)
  }
}