import com.google.inject.{AbstractModule, Provides}
import controller.GameControllerInterface
import controller.baseImp.GameController
import model.fileIOComp.baseImp.{FileIOJSON, FileIOXML}
import model.fileIOComp.FileIOInterface
import model.gameModelComp.baseImp.{Deck, Hand}
import model.gameModelComp.{DeckInterface, HandInterface}

class JsonModule extends AbstractModule {
    override def configure(): Unit = {
        bind(classOf[DeckInterface]).to(classOf[Deck])
        bind(classOf[HandInterface]).to(classOf[Hand])
        bind(classOf[GameControllerInterface]).to(classOf[GameController])
    }

    @Provides
    def provideFileIO(): FileIOInterface = {
        new FileIOJSON()
    }

    @Provides
    def provideGameController(deck: DeckInterface, hand: HandInterface, fileIO: FileIOInterface): GameController = {
        new GameController(deck.asInstanceOf[Deck], hand.asInstanceOf[Hand], fileIO)
    }
}

class XmlModule extends AbstractModule {
    override def configure(): Unit = {
        bind(classOf[DeckInterface]).to(classOf[Deck])
        bind(classOf[HandInterface]).to(classOf[Hand])
        bind(classOf[GameControllerInterface]).to(classOf[GameController])
    }

    @Provides
    def provideFileIO(): FileIOInterface = {
        new FileIOXML()
    }

    @Provides
    def provideGameController(deck: DeckInterface, hand: HandInterface, fileIO: FileIOInterface): GameController = {
        new GameController(deck.asInstanceOf[Deck], hand.asInstanceOf[Hand], fileIO)
    }
}