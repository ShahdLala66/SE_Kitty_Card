package model.FileIO

import controller.{GameController, GameControllerInterface}

trait FileIOInterface {

  def load(game: GameControllerInterface): String

  def save(game: GameControllerInterface): String


}

