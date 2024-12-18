package util.grid

import model.Grid
import model.cards.Suit.Suit

object GridFactory {
  def createGrid(size: Int): Grid = {
    val rectangleColors = GridUtils.generateRandomColors(size)
    Grid(size, rectangleColors)
  }

  def createPredefinedGrid(size: Int, predefinedColors: Array[Array[Suit]]): Grid = {
    Grid(size, predefinedColors)
  }
}