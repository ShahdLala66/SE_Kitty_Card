package util.grid

import model.cardComp.baseImp.Grid

object GridFactory {
  def createGrid(size: Int): Grid = {
    val rectangleColors = GridUtils.generateRandomColors(size)
    Grid(size, rectangleColors)
  }
}