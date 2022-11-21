# Crying Counter

> A JDA bot to reply to specific phrases (e.g. "im crying")

## Table of Contents

   - <a href='#setup'>Setup</a>
   - <a href='#running'>Running (IntelliJ)</a>
   - <a href='#commands'>Commands</a>
   - <a href='#contributing'>Contributing</a>

<h3 id='setup'>Setup</h3>

1. Clone the repository
2. Create a `.env` file in the main directory with the following variables:
    - `DISCORD_TOKEN`: The token of the bot
3. Specify JDK version (Tested on JDK 17/19)

<h3 id='running'>Running (IntelliJ)</h3>

1. Create a `resources` directory in `src/main`
2. Go to File -> Project Structure -> Artifacts
3. Create a new JAR from modules with dependencies
    - Select `Bot.java` as the main class
    - Select the newly created `resources` as the directory for `META-INF/MANIFEST.MF`
4. Build artifacts
5. Copy your `.env` file into `out/artifacts/crying_counter_jar`
6. Start your new bot
    - Open terminal, change directories to `out/artifacts/crying_counter_jar`
    - Run the .jar through `java -jar crying-counter.jar`

<h3 id='commands'>Commands</h3>

| Command           | Description                |
|-------------------|----------------------------|
| `/trigger new`    | Adds new trigger           |
| `/trigger reset`  | Resets all stored triggers |
| `/trigger list`   | Lists all stored triggers  |
| `/trigger delete` | Deletes specified trigger  |
| `/trigger toggle` | Toggles trigger feature    |

<h3 id='contributing'>Contributing</h3>

1. Create a new branch
2. Make your changes
3. Create a pull request

---

Created with 💖 by **[acmcsuf.com](https://acmcsuf.com) Bot Committee**
