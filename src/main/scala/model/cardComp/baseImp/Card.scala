package model.cardComp.baseImp

import model.cardComp.CardInterface

abstract class Card extends CardInterface {
    def getColor: String
    override def toString: String
}