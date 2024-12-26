package util.grid

import model.cardComp.baseImp.Suit.Suit
import model.cardComp.baseImp.Grid

object GridFactory {
  def createGrid(size: Int): Grid = {
    val rectangleColors = GridUtils.generateRandomColors(size)
    Grid(size, rectangleColors)
  }

  def createPredefinedGrid(size: Int, predefinedColors: Array[Array[Suit]]): Grid = {
    Grid(size, predefinedColors)
  }
}