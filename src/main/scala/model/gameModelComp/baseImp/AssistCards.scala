package model.gameModelComp.baseImp

import model.gameModelComp.CardInterface

object AssistCardType extends Enumeration {
    type AssistCardType = Value
    val Skip, Freeze, MeowThis, CatChing, KittyPlot, Purrceive,
    Meowster, Purrator, KittyPow, MagicPaw, Byebye, PawCombo = Value
}

case class AssistCardInterface(cardType: AssistCardType.Value) extends CardInterface {

    override def getColor: String = "Special"

    override def toString: String = cardType.toString
}
