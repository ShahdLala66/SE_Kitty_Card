FROM hseeberger/scala-sbt:8u222_1.3.5_2.13.1
WORKDIR /SE_Kitty_Card_1
ADD . /SE_Kitty_Card_1
CMD sbt run