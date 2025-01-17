package util

import java.io.{BufferedReader, InputStreamReader}
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class ConsoleProvider extends InputProvider {
    private val reader = new BufferedReader(new InputStreamReader(System.in))
    private val shouldInterrupt = new AtomicBoolean(false)
    private var currentThread: Option[Thread] = None

    override def getInput: String = {
        shouldInterrupt.set(false)
        var input: String = null

        currentThread = Some(Thread.currentThread())

        try {
            while (!shouldInterrupt.get() && !reader.ready()) {
                TimeUnit.MILLISECONDS.sleep(100)
            }

            if (shouldInterrupt.get()) {
                return null
            }

            input = reader.readLine()
        } catch {
            case _: InterruptedException =>
                return null 
            case e: Exception =>
                e.printStackTrace()
                return null
        } finally {
            currentThread = None
        }
        input
    }

    override def interrupt(): Unit = {
        shouldInterrupt.set(true)
        currentThread.foreach(_.interrupt())
    }
}