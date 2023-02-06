package com.acmcsuf.triggers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;

import io.github.cdimascio.dotenv.Dotenv;

import net.dv8tion.jda.api.entities.Member;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database
{

    final static Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    // Constants
    private static final String URL = dotenv.get( "DATABASE_URL" );
    private static final String USER = dotenv.get( "DATABASE_USER" );
    private static final String PASSWORD = dotenv.get( "DATABASE_PASSWORD" );

    // SLF4J Logger
    private static final Logger log = LoggerFactory.getLogger( Database.class );

    /**
     * Connects to database
     *
     * @return Connection object
     * @throws SQLException On failure to interact with database
     */
    private static Connection getConnect() throws SQLException
    {
        return DriverManager.getConnection( URL, USER, PASSWORD );
    }

    /**
     * Connects to and initializes database
     *
     * @throws SQLException On failure to interact with database
     */
    public static void initializeDatabase() throws SQLException
    {
        String sql = """
                CREATE TABLE IF NOT EXISTS triggers(
                user_id bigint PRIMARY KEY ,
                toggle boolean,
                phrase text[])""";

        try ( Connection conn = getConnect() )
        {
            conn.createStatement().execute( sql );
        }

        log.info( "Connected to PostgreSQL server" );
    }

    /**
     * Appends trigger phrase
     *
     * <p>
     * Appends new phrase to user's trigger list if it is not already in the list and syncs with database afterwards
     * </p>
     *
     * @param member        Event member
     * @param phrase        Phrase to add
     * @param connection    Connection object
     * @param triggerMap    Map of user IDs to trigger phrases
     * @param triggerToggle Map of user IDs to trigger toggle
     * @throws SQLException On failure to interact with database
     */
    public static void appendPhrase( Member member, String phrase, Connection connection,
                                     HashMap<String, LinkedHashSet<String>> triggerMap,
                                     HashMap<String, Boolean> triggerToggle ) throws SQLException
    {
        String userID = member.getId();

        String sql = """
                UPDATE triggers
                SET phrase = array_append(phrase, ?::text)
                WHERE user_id =""" + userID;

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement( sql );
            preparedStatement.setString( 1, phrase );
            preparedStatement.executeUpdate();
        }
        catch ( PSQLException e )
        {
            log.error( "Error appending phrase to database", e );
        }
        try
        {
            syncUserData( connection, member, triggerMap, triggerToggle );
        }
        catch ( PSQLException e )
        {
            log.error( "Error syncing user data", e );
        }
    }

    /**
     * Appends trigger phrase
     *
     * <p>
     * Appends new phrase to user's trigger list if it is not already in the list and syncs with database afterwards
     * </p>
     *
     * @param userID        Event member user ID
     * @param phrase        Phrase to add
     * @param connection    Connection object
     * @param triggerMap    Map of user IDs to trigger phrases
     * @param triggerToggle Map of user IDs to trigger toggle
     * @throws SQLException On failure to interact with database
     */
    public static void appendPhrase( String userID, String phrase, Connection connection,
                                     HashMap<String, LinkedHashSet<String>> triggerMap,
                                     HashMap<String, Boolean> triggerToggle ) throws SQLException
    {

        String sql = """
                UPDATE triggers
                SET phrase = array_append(phrase, ?::text)
                WHERE user_id =""" + userID;

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement( sql );
            preparedStatement.setString( 1, phrase );
            preparedStatement.executeUpdate();
        }
        catch ( PSQLException e )
        {
            log.error( "Error appending phrase to database", e );
        }
        try
        {
            syncUserData( connection, userID, triggerMap, triggerToggle );
        }
        catch ( PSQLException e )
        {
            log.error( "Error syncing user data", e );
        }
    }

    /**
     * Deletes trigger phrase
     *
     * <p>
     * Deletes phrase from user's trigger list and syncs with database afterwards
     * </p>
     *
     * @param userID Event member user ID
     * @param phrase Phrase to delete
     * @throws SQLException On failure to interact with database
     */
    public static void deletePhrase( String userID, String phrase,
                                     HashMap<String, LinkedHashSet<String>> triggerMap,
                                     HashMap<String, Boolean> triggerToggle ) throws SQLException
    {

        String sql = """
                UPDATE triggers
                SET phrase = array_remove(phrase, ?::text)
                WHERE user_id =""" + userID;

        try ( Connection conn = getConnect() )
        {
            PreparedStatement preparedStatement = conn.prepareStatement( sql );
            preparedStatement.setString( 1, phrase );
            preparedStatement.executeUpdate();

            try
            {
                syncUserData( conn, userID, triggerMap, triggerToggle );
            }
            catch ( PSQLException e )
            {
                log.error( "Error syncing user data", e );
            }
        }
        catch ( PSQLException e )
        {
            log.error( "Error deleting phrase from database", e );
        }
    }

    /**
     * Deletes trigger phrase
     *
     * <p>
     * Deletes phrase from user's trigger list and syncs with database afterwards
     * </p>
     *
     * @param member Event member
     * @param phrase Phrase to delete
     * @throws SQLException On failure to interact with database
     */
    public static void deletePhrase( Member member, String phrase,
                                     HashMap<String, LinkedHashSet<String>> triggerMap,
                                     HashMap<String, Boolean> triggerToggle ) throws SQLException
    {
        String userID = member.getId();

        String sql = """
                UPDATE triggers
                SET phrase = array_remove(phrase, ?::text)
                WHERE user_id =""" + userID;

        try ( Connection conn = getConnect() )
        {
            PreparedStatement preparedStatement = conn.prepareStatement( sql );
            preparedStatement.setString( 1, phrase );
            preparedStatement.executeUpdate();

            try
            {
                syncUserData( conn, member, triggerMap, triggerToggle );
            }
            catch ( PSQLException e )
            {
                log.error( "Error syncing user data", e );
            }
        }
        catch ( PSQLException e )
        {
            log.error( "Error deleting phrase from database", e );
        }
    }

    /**
     * Resets user's trigger list
     *
     * @param member Event member
     * @throws SQLException On failure to interact with database
     */
    public static void resetTriggers( Member member,
                                      HashMap<String, LinkedHashSet<String>> triggerMap,
                                      HashMap<String, Boolean> triggerToggle ) throws SQLException
    {
        String userID = member.getId();

        String sql = """
                UPDATE triggers
                SET phrase = '{}'
                WHERE user_id = ?""";

        try ( Connection conn = getConnect() )
        {
            PreparedStatement preparedStatement = conn.prepareStatement( sql );
            preparedStatement.setLong( 1, Long.parseLong( userID ) );
            preparedStatement.executeUpdate();

            try
            {
                syncUserData( conn, member, triggerMap, triggerToggle );
            }
            catch ( PSQLException e )
            {
                log.error( "Error syncing user data", e );
            }
        }
        catch ( PSQLException e )
        {
            log.error( "Error resetting triggers", e );
        }
    }

    /**
     * Toggle trigger response on/off
     *
     * @param member Event member
     * @param toggle Toggle value
     * @throws SQLException On failure to interact with database
     */
    public static void toggleTrigger( Member member, boolean toggle ) throws SQLException
    {
        String userID = member.getId();

        String sql = """
                UPDATE triggers
                SET toggle = ?
                WHERE user_id = ?""";

        try ( Connection conn = getConnect() )
        {
            PreparedStatement preparedStatement = conn.prepareStatement( sql );
            preparedStatement.setBoolean( 1, toggle );
            preparedStatement.setLong( 2, Long.parseLong( userID ) );
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Initializes user and appends one phrase to their trigger list
     *
     * @param member Event member
     * @param phrase Phrase to add
     * @throws SQLException On failure to interact with database
     */
    public static void initializeIfNotExistsAndAppend( Member member, String phrase,
                                                       HashMap<String, LinkedHashSet<String>> triggerMap,
                                                       HashMap<String, Boolean> triggerToggle  ) throws SQLException
    {
        Connection connection = getConnect();

        initializeIfNotExists( connection, member );
        appendPhrase( member, phrase, connection, triggerMap, triggerToggle );
        connection.close();
    }


    /**
     * Checks if a user is registered in the database and inserts if not found
     *
     * @param member Event member
     * @return User is registered
     * @throws SQLException On failure to interact with database
     */
    public static boolean isStoredUser( Member member ) throws SQLException
    {
        String userID = member.getId();
        String sql = String.format( """
                SELECT user_id
                FROM triggers
                WHERE user_id = %s;""", userID );

        try ( Connection conn = getConnect() )
        {

            ResultSet set = conn.createStatement().executeQuery( sql );
            boolean found = false;

            // Insert user if not found
            if ( !set.next() )
            {
                String insert = String.format( """
                        INSERT INTO triggers(user_id, toggle, phrase)
                        VALUES(%s, TRUE, '{}');""", userID );

                conn.createStatement().execute( insert );
            }
            else
            {
                found = true;
            }

            conn.close();
            return found;
        }
    }

    /**
     * Initializes user if not found
     *
     * @param member Event member
     * @throws SQLException On failure to interact with database
     */
    public static void initializeIfNotExists( Member member ) throws SQLException
    {
        if ( !isStoredUser( member ) )
        {
            String userID = member.getId();
            String sql = String.format( """
                    INSERT INTO triggers(user_id, toggle, phrase)
                    VALUES(%s, TRUE, '{}');""", userID );

            try ( Connection conn = getConnect() )
            {
                conn.createStatement().execute( sql );
            }
            catch ( PSQLException ignore )
            {
            }
        }
    }

    /**
     * Initializes user if not found
     *
     * @param connection Connection object
     * @param member     Event member
     * @throws SQLException On failure to interact with database
     */
    public static void initializeIfNotExists( Connection connection, Member member ) throws SQLException
    {
        if ( !isStoredUser( member ) )
        {
            String userID = member.getId();
            String sql = String.format( """
                    INSERT INTO triggers(user_id, toggle, phrase)
                    VALUES(%s, TRUE, '{}');""", userID );

            try
            {
                connection.createStatement().execute( sql );
            }
            catch ( PSQLException ignore )
            {
            }
        }
    }

    /**
     * Sync in-memory triggers with database
     *
     * @param triggerMap In-memory trigger map
     */
    public static void syncData( HashMap<String, LinkedHashSet<String>> triggerMap,
                                 HashMap<String, Boolean> triggerToggle ) throws SQLException
    {
        String sql = "SELECT * FROM triggers";

        try ( Connection conn = getConnect() )
        {
            ResultSet set = conn.createStatement().executeQuery( sql );

            while ( set.next() )
            {
                String userID = set.getString( "user_id" );
                insertData( triggerMap, triggerToggle, userID, set );
            }
        }
    }

    /**
     * Sync member's in-memory trigger with database
     *
     * @param member        Event member
     * @param triggerMap    In-memory trigger map
     * @param triggerToggle In-memory trigger toggle map
     * @throws SQLException On failure to interact with database
     */
    public static void syncUserData( Member member, HashMap<String, LinkedHashSet<String>> triggerMap,
                                     HashMap<String, Boolean> triggerToggle ) throws SQLException
    {
        String userID = member.getId();
        String sql = String.format( """
                SELECT * FROM triggers
                WHERE user_id = %s;""", userID );

        try ( Connection conn = getConnect() )
        {
            ResultSet set = conn.createStatement().executeQuery( sql );

            if ( set.next() )
            {
                insertData( triggerMap, triggerToggle, userID, set );
            }
        }
    }

    /**
     * Sync member's in-memory trigger with database
     *
     * @param userID        Event member user ID
     * @param triggerMap    In-memory trigger map
     * @param triggerToggle In-memory trigger toggle map
     * @throws SQLException On failure to interact with database
     */
    public static void syncUserData( String userID, HashMap<String, LinkedHashSet<String>> triggerMap,
                                     HashMap<String, Boolean> triggerToggle ) throws SQLException
    {
        String sql = String.format( """
                SELECT * FROM triggers
                WHERE user_id = %s;""", userID );

        try ( Connection conn = getConnect() )
        {
            ResultSet set = conn.createStatement().executeQuery( sql );

            if ( set.next() )
            {
                insertData( triggerMap, triggerToggle, userID, set );
            }
        }
    }

    /**
     * Sync member's in-memory trigger with database
     *
     * @param userID        Event member user ID
     * @param member        Event member
     * @param triggerMap    In-memory trigger map
     * @param triggerToggle In-memory trigger toggle map
     * @throws SQLException On failure to interact with database
     */
    public static void syncUserData( Connection connection, String userID,
                                     HashMap<String, LinkedHashSet<String>> triggerMap,
                                     HashMap<String, Boolean> triggerToggle ) throws SQLException
    {
        String sql = String.format( """
                SELECT * FROM triggers
                WHERE user_id = %s;""", userID );

        ResultSet set = connection.createStatement().executeQuery( sql );

        if ( set.next() )
        {
            insertData( triggerMap, triggerToggle, userID, set );
        }
    }

    /**
     * Sync member's in-memory trigger with database
     *
     * @param connection    Connection object
     * @param member        Event member
     * @param triggerMap    In-memory trigger map
     * @param triggerToggle In-memory trigger toggle map
     * @throws SQLException On failure to interact with database
     */
    public static void syncUserData( Connection connection, Member member,
                                     HashMap<String, LinkedHashSet<String>> triggerMap,
                                     HashMap<String, Boolean> triggerToggle ) throws SQLException
    {
        String userID = member.getId();
        String sql = String.format( """
                SELECT * FROM triggers
                WHERE user_id = %s;""", userID );

        ResultSet set = connection.createStatement().executeQuery( sql );

        if ( set.next() )
        {
            insertData( triggerMap, triggerToggle, userID, set );
        }
    }

    /**
     * Inserts data into in-memory maps
     *
     * @param triggerMap    In-memory trigger map
     * @param triggerToggle In-memory trigger toggle map
     * @param userID        User ID
     * @param set           Result set
     * @throws SQLException On failure to interact with database
     */
    private static void insertData( HashMap<String, LinkedHashSet<String>> triggerMap,
                                    HashMap<String, Boolean> triggerToggle, String userID, ResultSet set )
            throws SQLException
    {
        boolean toggle = set.getBoolean( "toggle" );
        String[] phrases = (String[]) set.getArray( "phrase" ).getArray();

        LinkedHashSet<String> set1 = new LinkedHashSet<>( Arrays.asList( phrases ) );
        triggerMap.put( userID, set1 );
        triggerToggle.put( userID, toggle );
    }
}
