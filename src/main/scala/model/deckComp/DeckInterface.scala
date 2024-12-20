package model.deckComp

import model.cardComp.baseImp.NumberCards

trait DeckInterface {
    def drawCard(): Option[NumberCards]
    def size: Int
}