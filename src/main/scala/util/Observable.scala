package util

trait Observer {
    def update(event: GameEvent): Unit
}

trait Observable {
    private var observers: List[Observer] = List()

    def add(observer: Observer): Unit = {
        observers = observer :: observers
    }

    def remove(observer: Observer): Unit = {
        observers = observers.filterNot(_ == observer)
    }

    def notifyObservers(event: GameEvent): Unit = {
        observers.foreach(_.update(event))
    }
}