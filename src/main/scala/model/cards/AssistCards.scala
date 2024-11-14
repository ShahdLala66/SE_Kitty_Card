package model.cards

object AssistCardType extends Enumeration {
    type AssistCardType = Value
    val Skip, Freeze, MeowThis, CatChing, KittyPlot, Purrceive,
    Meowster, Purrator, KittyPow, MagicPaw, Byebye, PawCombo = Value
}

case class AssistCard(cardType: AssistCardType.Value) extends Card {

    override def getColor: String = "Special"

    override def toString: String = cardType.toString
}

//vielleicht einstelle was 
// die warscheinlichkeit ist karten zu ziehen


//in 5. semster können wir gacha zufügen kurz, karten ziehen und wenn man gute karte hat dann 
//kann man stärker sein und mehr privligen hat, iwi die battle und kitty cards machen  , aber gacha logic wäre witzig 

