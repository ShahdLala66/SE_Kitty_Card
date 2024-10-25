package model

case class Player(name: String, var points: Int = 0) {
    def addPoints(newPoints: Int): Unit = {
        points += newPoints
    }

    override def toString: String = s"$name (Points: $points)"
}
