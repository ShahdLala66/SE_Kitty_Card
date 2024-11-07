package util

import scala.io.Source
import scala.util.Random

object ErrorMessages {
  private var messages: List[String] = List()
  private var usedMessages: Set[String] = Set()

  def loadMessages(familyFriendly: Boolean): Unit = {
    val fileName = if (familyFriendly) "error_messages_friendly.txt" else "error_messages.txt"
    val source = Source.fromResource(fileName)
    messages = source.getLines().toList
    source.close()
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
}