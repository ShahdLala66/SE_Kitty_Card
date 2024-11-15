package util

trait GameCallbacks extends Observer {
    def displayMeh(color: String): Unit
    def displayCatInColor(color: String): Unit
    def displayBadChoice(color: String): Unit
}
