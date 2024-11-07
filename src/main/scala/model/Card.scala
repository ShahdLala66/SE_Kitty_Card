package model

import model.Suit.Suit
import model.Value1.Value
import scala.util.Random

case class Card(suit: Suit.Value, value: Value1.Value) {

  private val colors = List("Green", "Brown", "Purple", "Blue", "Red")
  val color: String = colors(Random.nextInt(colors.length))

  override def toString: String = s"${value.toString} of ${suit.toString}" //excepts for white

  // Method to get the color associated with the card
  def getColor: String = color
}