package model.fileIOComp

import controller.GameControllerInterface
import model.gameModelComp.baseImp.Grid

trait FileIOInterface {

  def load(game: GameControllerInterface): String

  def save(game: GameControllerInterface, grid: Grid): String
    
}

