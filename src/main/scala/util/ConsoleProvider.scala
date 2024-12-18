package util

class ConsoleProvider extends InputProvider {
    override def getInput: String = {
        scala.io.StdIn.readLine()
    }
}