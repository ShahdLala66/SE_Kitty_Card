package model.cardComp.baseImp

import model.cardComp.SuitInterface

object Suit extends Enumeration with SuitInterface {
    type Suit = Value
    val Green, Brown, Purple, Blue, Red, White = Value // Added White for the white rectangle

    override def suits: List[String] = values.toList.map(_.toString)
}