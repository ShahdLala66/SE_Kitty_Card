package model

import util.InputProvider

import scala.concurrent.Promise

class SharedInputProvider extends InputProvider {
  @volatile private var inputQueue: Option[String] = None
  @volatile private var inputPromise: Option[Promise[String]] = None

  def getInput: String = {
    import scala.concurrent.Await
    import scala.concurrent.duration.Duration

    val promise = Promise[String]()
    inputPromise = Some(promise)

    inputQueue match {
      case Some(input) =>
        inputQueue = None
        input
      case None =>
        Await.result(promise.future, Duration.Inf)
    }
  }

  def setInput(input: String): Unit = {
    inputPromise match {
      case Some(promise) =>
        promise.success(input)
        inputPromise = None
      case None =>
        inputQueue = Some(input)
    }
  }

  def hasInput: Boolean = inputQueue.isDefined
}