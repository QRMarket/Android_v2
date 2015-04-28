package com.qr_market.db.contract;


import android.provider.BaseColumns;

/**
 * @author Kemal Sami KARACA
 * @since 03.03.2015
 * @version v1.01
 */
public class GuppyContract {


    public GuppyContract(){}

    private static final String TYPE_TEXT       = " TEXT";
    private static final String TYPE_INTEGER    = " INTEGER";
    private static final String COMMA_SEP       = ", ";

    /**
     *  REDUCTIONs
     *      gu <-> guppy user
     *      ga <-> guppy address
     */

    // ************************************
    // ************************************
    // GUPPY-USER TABLE CONTRACT
    // ************************************
    // ************************************
    public static abstract class GuppyUser implements BaseColumns {
        public static final String TABLE_NAME = "gu";
        public static final String COLUMN_NAME_USER_NAME        = "guname";
        public static final String COLUMN_NAME_USER_MAIL        = "gumail";
        public static final String COLUMN_NAME_USER_PASSWORD    = "gupass";
        public static final String COLUMN_NAME_USER_TOKEN       = "gutoken";
        public static final String COLUMN_NAME_USER_SESSIONID   = "gusession";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + GuppyUser.TABLE_NAME + " (" +
                        GuppyUser._ID + " INTEGER PRIMARY KEY," +
                        GuppyUser.COLUMN_NAME_USER_TOKEN + TYPE_TEXT + COMMA_SEP +
                        GuppyUser.COLUMN_NAME_USER_SESSIONID + TYPE_TEXT + COMMA_SEP +
                        GuppyUser.COLUMN_NAME_USER_MAIL + TYPE_TEXT + COMMA_SEP +
                        GuppyUser.COLUMN_NAME_USER_PASSWORD + TYPE_TEXT + COMMA_SEP +
                        GuppyUser.COLUMN_NAME_USER_NAME + TYPE_TEXT +
                        " )";
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + GuppyUser.TABLE_NAME;


        public static String getSqlCreateEntries() {
            return SQL_CREATE_ENTRIES;
        }

        public static String getSqlDeleteEntries() {
            return SQL_DELETE_ENTRIES;
        }
    }

    // ************************************
    // ************************************
    // GUPPY-ADDRESS TABLE CONTRACT
    // ************************************
    // ************************************
    public static abstract class GuppyAddress implements BaseColumns{
        public static final String TABLE_NAME = "ga";
        public static final String COLUMN_NAME_ADDRESS_CITY     = "gacity";
        public static final String COLUMN_NAME_ADDRESS_BOROUGH  = "guborough";
        public static final String COLUMN_NAME_ADDRESS_LOCALITY = "gulocality";
        public static final String COLUMN_NAME_ADDRESS_STREET   = "gustreet";
        public static final String COLUMN_NAME_ADDRESS_AVENUE   = "guavenue";
        public static final String COLUMN_NAME_ADDRESS_DESC     = "gudesc";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + GuppyAddress.TABLE_NAME + " (" +
                        GuppyAddress._ID + " INTEGER PRIMARY KEY," +
                        GuppyAddress.COLUMN_NAME_ADDRESS_CITY + TYPE_TEXT + COMMA_SEP +
                        GuppyAddress.COLUMN_NAME_ADDRESS_BOROUGH + TYPE_TEXT + COMMA_SEP +
                        GuppyAddress.COLUMN_NAME_ADDRESS_LOCALITY + TYPE_TEXT + COMMA_SEP +
                        GuppyAddress.COLUMN_NAME_ADDRESS_STREET + TYPE_TEXT + COMMA_SEP +
                        GuppyAddress.COLUMN_NAME_ADDRESS_AVENUE + TYPE_TEXT + COMMA_SEP +
                        GuppyAddress.COLUMN_NAME_ADDRESS_DESC + TYPE_TEXT +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + GuppyAddress.TABLE_NAME;

        public static String getSqlCreateEntries() {
            return SQL_CREATE_ENTRIES;
        }

        public static String getSqlDeleteEntries() {
            return SQL_DELETE_ENTRIES;
        }
    }








}
