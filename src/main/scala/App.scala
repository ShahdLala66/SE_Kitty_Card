import controller.GameController

object App {
  def main(args: Array[String]): Unit = {
    val gameController = new GameController()
    gameController.startGame()
  }
}