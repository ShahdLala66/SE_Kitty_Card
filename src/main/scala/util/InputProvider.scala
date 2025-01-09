package util

trait InputProvider {
    def getInput: String

    def interrupt(): Unit
}
