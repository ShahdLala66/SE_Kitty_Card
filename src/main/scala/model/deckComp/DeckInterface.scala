package model.deckComp

import model.cardComp.CardInterface

trait DeckInterface {
    def drawCard(): Option[CardInterface]
    def size: Int
}