
package model

object AssistCardType extends Enumeration {
  type AssistCardType = Value
  val Freeze, Steal, Deny, Reset, VeryRareWin = Value
}

case class AssistCard(cardType: AssistCardType.Value) extends Cards {

  override def getColor: String = "Special"

  override def toString: String = cardType.toString
}
//vielleicht einstelle was 
  // die warschanlichkeit ist karten zu ziehen
  
  
  
  //in 5. semster können wir gacha zufügen kurz, karten ziehen und wenn man gute karte hat dann 
  //kann man stärker sein und mehr privligen hat, iwi die battle und kitty cards machen  , aber gacha logic wäre witzig 

