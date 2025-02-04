package model.gameModelComp.baseImp

import model.gameModelComp.CardInterface
import scala.util.Random

case class NumberCards(suit: Suit.Value, value: Value.Value) extends CardInterface {

    private val colors = List("Green", "Brown", "Purple", "Blue", "Red")
    val color: String = colors(Random.nextInt(colors.length))

    override def toString: String = s"${value.toString} of ${suit.toString}"

    override def getColor: String = color
}