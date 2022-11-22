# Crying Counter

> A JDA bot to reply to specific phrases (e.g. "im crying")

## Table of Contents

- <a href='#setup'>IDE Setup</a>
- <a href='#bot'>Bot Setup</a>
- <a href='#running'>Running (IntelliJ)</a>
- <a href='#commands'>Commands</a>
- <a href='#documentation'>Documentation</a>
- <a href='#faq'>FAQ</a>
- <a href='#contributing'>Contributing</a>

<h2 id='setup'>IDE Setup</h2>

1. Clone the repository
2. Create a `.env` file in the main directory with the following variables:
    - `DISCORD_TOKEN`: The token of the bot
    - `AUTHORIZED_ROLE_ID`: Array of role IDs that are allowed to use the bot (separate by comma)
3. Specify JDK version (Tested on JDK 17/19)
4. Restart IDE

<h2 id='bot'>Bot Setup</h2>

1. Go to the [Discord Developer Dashboard](https://discord.com/developers/applications)
2. Register your bot
3. Enable Server Members Intent and Message Content Intent

<h2 id='running'>Running (IntelliJ)</h2>

1. Create a `resources` directory in `src/main`
2. Go to File -> Project Structure -> Artifacts
3. Create a new JAR from modules with dependencies
    - Select `Bot.java` as the main class
    - Select the newly created `resources` as the directory for `META-INF/MANIFEST.MF`
4. Build artifacts
5. Copy your `.env` file into `out/artifacts/crying_counter_jar`
6. Start your new bot
    - Open terminal, change directories to `out/artifacts/crying_counter_jar`
    - Run the .jar with `java -jar crying-counter.jar`
7. To invite your bot,
   use `https://discord.com/api/oauth2/authorize?client_id=$DISCORD_CLIENT_ID&permissions=66560&scope=bot%20applications.commands`
   and replace `$DISCORD_CLIENT_ID` with the bot ID

<h2 id='commands'>Commands</h2>

| Command           | Description                |
|-------------------|----------------------------|
| `/trigger new`    | Adds new trigger           |
| `/trigger reset`  | Resets all stored triggers |
| `/trigger list`   | Lists all stored triggers  |
| `/trigger delete` | Deletes specified trigger  |
| `/trigger toggle` | Toggles trigger feature    |

<h2 id='documentation'>Documentation</h2>

- <a href='https://github.com/DV8FromTheWorld/JDA'>JDA</a>
- <a href='https://github.com/qos-ch/slf4j'>SLF4J</a>
- <a href='https://github.com/cdimascio/dotenv-java'>dotenv Java</a>
- <a href='https://github.com/xdrop/fuzzywuzzy'>fuzzywuzzy</a>

<h2 id='faq'>FAQ</h2>

1. **"I can't invite my bot"**

    - Make sure you have the correct permissions (66560)
    - Make sure you have the correct scope (bot%20applications.commands)
    - Make sure you have the correct client ID

2. **"I can't run the bot"**

    - Make sure you have the correct token
    - Make sure you have the correct role IDs
    - Make sure you have the correct JDK version

3. **"How can I get my bot ID?"**

   - Go to Discord Developer Dashboard -> Your bot -> General Information -> Application ID

4. **"Why does the bot not respond to my messages?"**

   - Make sure you have the correct role IDs

5. **"How can I get my role ID?"**

   - Go to your server -> Server Settings -> Roles
   - Right-click on the role you want to get the ID of and select "Copy ID"

<h2 id='contributing'>Contributing</h2>

1. Create a new branch
2. Make your changes
3. Create a pull request

---

Created with 💖 by **[acmcsuf.com](https://acmcsuf.com) Bot Committee**
