# Crying Counter

> A JDA bot to reply to specific phrases (e.g. "im crying")

## Development

### Setup

1. Clone the repository
1. Create a `.env` file in the main directory with the following variables:
    - `DISCORD_TOKEN`: The token of the bot

### Running (IntelliJ)

1. Create a `resources` directory in `src`
1. Go to Project Structure -> Artifacts
1. Create a new JAR from modules with dependencies
   - Select `Bot.java` as the main class
   - Select the newly created `resources` as the directory for `META-INF/MANIFEST.MF`
1. Build artifacts 
1. Copy your `.env` file into `out/artifacts/crying_counter_jar`
1. Start your new bot
   - Open terminal, change directories to `out/artifacts/crying_counter_jar`
   - Run the .jar through `java -jar crying-counter.jar`

## Contributing

1. Create a new branch
1. Make your changes
1. Create a pull request

---

Created with 💖 by **[acmcsuf.com](https://acmcsuf.com) src.java.Bot Committee**
