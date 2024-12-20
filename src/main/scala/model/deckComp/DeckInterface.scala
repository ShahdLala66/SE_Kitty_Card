package model.deckComp

import model.cardComp.NumberCards

trait DeckInterface {
    def drawCard(): Option[NumberCards]
    def size: Int
}