FROM sbtscala/scala-sbt:eclipse-temurin-jammy-22_36_1.10.0_3.4.2

WORKDIR /SE_Kitty_Card_1

RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    gnupg2 \
    x11-apps \
    xvfb \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libxrandr2 \
    libgtk-3-0 \
    libxxf86vm1 \
    libgl1-mesa-glx \
    libgl1-mesa-dri \
    libgl1 \
    libosmesa6 \
    mesa-utils \
    libasound2 \
    libasound2-plugins \
    alsa-utils \
    pulseaudio \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

RUN useradd -m -u 1000 appuser
RUN mkdir -p /home/appuser/.pulse && \
    chown -R appuser:appuser /home/appuser

RUN curl -sL https://dlcdn.apache.org/sbt/debian/sbt-1.9.4.deb -o sbt.deb && \
    dpkg -i sbt.deb || apt-get install -f -y && \
    rm sbt.deb

#COPY --chown=appuser:appuser . /SE_Kitty_Card_1 wenn die anderen nicht gehen dann wieder löschen und das activeiren

#---
COPY --chown=appuser:appuser . /SE_Kitty_Card_1
RUN chmod -R a-w /SE_Kitty_Card_1
#----
USER appuser

RUN sbt update && sbt compile

ENV DISPLAY=:99
ENV PULSE_SERVER=/tmp/pulse/native
ENV SBT_OPTS="-Xms512M -Xmx1536M -Xss2M -XX:MaxMetaspaceSize=512M"
ENV _JAVA_OPTIONS="-Djava.security.policy=applet.policy -Dprism.order=sw"

VOLUME /tmp/.X11-unix
VOLUME /tmp/pulse

CMD ["sh", "-c", "Xvfb :99 -screen 0 1024x768x24 -ac & sleep 2 && sbt run"]

# docker build -t kitty-card . (terminal intellij)
# docker run -it --rm -e DISPLAY=host.docker.internal:0 -v /tmp/.X11-unix:/tmp/.X11-unix -v /run/user/1000/pulse:/run/user/1000/pulse kitty-card (powershell
