package util

import scala.collection.mutable.ListBuffer

trait Observer {
    def update(event: GameEvent): Unit
}

trait Observable {
    private var observers: List[Observer] = List()

    // buffer to keep recent events so other components (like the SE controller)
    // can pull or drain them when needed.
    private val eventBuffer: ListBuffer[GameEvent] = ListBuffer.empty

    def add(observer: Observer): Unit = {
        observers = observer :: observers
    }

    def remove(observer: Observer): Unit = {
        observers = observers.filterNot(_ == observer)
    }

    /**
     * Notify observers as before, and also append the event to the internal buffer.
     * This preserves existing behaviour while allowing other parts of the system to
     * inspect or drain the recent events.
     */
    def notifyObservers(event: GameEvent): Unit = {
        // push into buffer
        eventBuffer += event
        // dispatch to observers
        observers.foreach(_.update(event))
        // Send update to web server if available
        GameStateHttpClient.sendUpdate(event.toString)
    }

    /** Return a snapshot of buffered events without clearing them. */
    def peekEvents(): List[GameEvent] = eventBuffer.toList

    /** Return and clear the buffered events. */
    def drainEvents(): List[GameEvent] = {
        val out = eventBuffer.toList
        eventBuffer.clear()
        out
    }

    /** Number of events currently buffered. */
    def bufferedSize: Int = eventBuffer.size
}