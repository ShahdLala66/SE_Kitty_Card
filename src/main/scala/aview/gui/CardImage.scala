package aview.gui

case class CardImage(value: String, suit: String) {
    
    def imagePath: String = s"Assets/Cards/$value/$value-$suit.png"

}