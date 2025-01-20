package util.grid

import model.gameModelComp.baseImp.{Grid, Suit}
import Suit.Suit

import scala.util.Random

object GridUtils {
  def generateRandomColors(size: Int): Array[Array[Suit]] = {
    val colors = List(Suit.Blue, Suit.Green, Suit.Purple, Suit.Red, Suit.Brown)
    val positions = Random.shuffle(for {
      x <- 0 until size
      y <- 0 until size
    } yield (x, y)).take(4)

    val colorGrid = Array.fill(size, size)(Suit.White) 
    positions.zip(Random.shuffle(colors)).foreach { case ((x, y), color) =>
      colorGrid(x)(y) = color
    }

    colorGrid
  }

  def createEmptyGrid(size: Int): Grid = {
    val emptyColors = Array.fill(size, size)(Suit.White)
      Grid(size, emptyColors)
  }
}