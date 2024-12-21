package util

trait InputHandler {
  def handleInput(input: String): Unit
  def requestInput(): Unit
}
