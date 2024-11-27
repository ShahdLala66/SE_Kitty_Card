// src/main/scala/util/ObserverPattern.scala
package util

object ObserverPattern {
    val observable = new ConcreteObservable
    val observer1 = new TestObject
    val observer2 = new TestObject
    observable.add(observer1)
    observable.add(observer2)
    observable.notifyObservers(PlayerTurn("Player1"))
    observable.remove(observer1)
    observable.notifyObservers(PlayerTurn("Player2"))
    observable.remove(observer2)
}