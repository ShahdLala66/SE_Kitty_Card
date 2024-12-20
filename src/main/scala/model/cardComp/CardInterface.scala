package model.cardComp

import model.cardComp.baseImp.Value


trait CardInterface {
    def getColor: String
    def toString: String
}

trait NumberCardsInterface {
    def getColor: String
    override def toString: String
}

trait SuitInterface {
    def suits: List[String]
}

trait ValueInterface {
    def toInt(value: Value.Value): Int
}
