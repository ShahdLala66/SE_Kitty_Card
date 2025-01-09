package aview.gui

// Model class for a Card
case class CardImage(value: String, suit: String) {
    // Compute the image path based on value and suit
    def imagePath: String = s"Assets/Cards/$value/$value-$suit.png"
}