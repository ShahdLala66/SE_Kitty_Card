package model.FileIO

import controller.GameControllerInterface
import model.baseImp.Grid

trait FileIOInterface {

  def load(game: GameControllerInterface): String

  def save(game: GameControllerInterface, grid: Grid): String


}

