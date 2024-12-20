package model.cardComp

abstract class Card extends CardInterface {
    def getColor: String
    override def toString: String
}