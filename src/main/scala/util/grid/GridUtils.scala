package util.grid

import model.cards.Suit
import model.cards.Suit.Suit

import scala.util.Random

object GridUtils {
  def generateRandomColors(size: Int): Array[Array[Suit]] = {
    val colors = List(Suit.Blue, Suit.Green, Suit.Purple, Suit.Red)
    val positions = Random.shuffle(for {
      x <- 0 until size
      y <- 0 until size
    } yield (x, y)).take(4)

    val colorGrid = Array.fill(size, size)(Suit.White) // Start with all White
    positions.zip(Random.shuffle(colors)).foreach { case ((x, y), color) =>
      colorGrid(x)(y) = color
    }

    colorGrid
  }
}