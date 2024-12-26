import com.google.inject.AbstractModule
import controller.{GameController, GameControllerInterface}


class MainModule extends AbstractModule {
    override def configure(): Unit = {
        bind(classOf[GameControllerInterface]).to(classOf[GameController])
    }
}