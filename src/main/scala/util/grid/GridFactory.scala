package util.grid

import model.gameModelComp.baseImp
import model.gameModelComp.baseImp.Grid

object GridFactory {
  def createGrid(size: Int): Grid = {
    val rectangleColors = GridUtils.generateRandomColors(size)
    baseImp.Grid(size, rectangleColors)
  }
}