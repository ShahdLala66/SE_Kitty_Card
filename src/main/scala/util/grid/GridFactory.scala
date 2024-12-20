package util.grid

import model.cardComp.Suit.Suit
import model.gridComp.Grid

object GridFactory {
  def createGrid(size: Int): Grid = {
    val rectangleColors = GridUtils.generateRandomColors(size)
    Grid(size, rectangleColors)
  }

  def createPredefinedGrid(size: Int, predefinedColors: Array[Array[Suit]]): Grid = {
    Grid(size, predefinedColors)
  }
}