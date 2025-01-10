import com.google.inject.{AbstractModule, Provides}
import controller.{GameController, GameControllerInterface}
import model.{DeckInterface, HandInterface}
import model.baseImp.{Deck, Hand}

class MainModule extends AbstractModule {
    override def configure(): Unit = {
        bind(classOf[DeckInterface]).to(classOf[Deck])
        bind(classOf[HandInterface]).to(classOf[Hand]) // Bind Hand to HandInterface
        bind(classOf[GameControllerInterface]).to(classOf[GameController])
    }
//hi
    @Provides
    def provideGameController(deck: DeckInterface, hand: HandInterface): GameController = {
        new GameController(deck.asInstanceOf[Deck], hand.asInstanceOf[Hand])
    }
}