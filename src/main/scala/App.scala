import controller.GameController

object KittyCardGame {
  def main(args: Array[String]): Unit = {
    val gameController = new GameController()
    gameController.startGame()
  }
}