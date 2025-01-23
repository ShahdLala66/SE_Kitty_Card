package aview.gui

case class CardImage(value: String, suit: String) {
    
    def imagePath: String = s"assets/cards/$value/$value-$suit.png"

}