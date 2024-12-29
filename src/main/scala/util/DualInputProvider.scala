package util


class DualInputProvider extends InputProvider {
  private val inputQueue = scala.collection.mutable.Queue[String]()
  private val lock = new Object()
  private var isInjectedInput = false
  private var isWaitingForInput = false
  private var toggle = false

  def getInput: String = {
    lock.synchronized {
      isWaitingForInput = true
      while (inputQueue.isEmpty && System.in.available() == 0) {
        lock.wait(100) // Short wait to check for input periodically
      }
      isWaitingForInput = false
      toggle = false

      if (inputQueue.nonEmpty) { // If input was injected, return it
        isInjectedInput = true
        inputQueue.dequeue()
      } else {
        isInjectedInput = false
        toggle = false
        scala.io.StdIn.readLine()
      }
    }
  }

  def injectInput(input: String, toggle : Boolean): Unit = {

      lock.synchronized {
        if (!isInjectedInput && !toggle) { // Only inject if no input is currently injected
          inputQueue.enqueue(input)
          if (isWaitingForInput) {
            lock.notify()
        }
      }
    }
  }

  def isCurrentInputInjected: Boolean = isInjectedInput
}