package es.uam.eps.dadm.othello_alejandromartin.database;

/**
 * Created by jalij on 22/03/2017.
 */

public class RoundDataBaseSchema {

    public static final class UserTable {
        public static final String NAME = "users";
        public static final class Cols {
            public static final String PLAYERUUID = "playeruuid1";
            public static final String PLAYERNAME = "playername";
            public static final String PLAYERPASSWORD = "playerpassword";
        }
    }
    public static final class RoundTable {
        public static final String NAME = "rounds";
        public static final class Cols {
            public static final String PLAYERUUID = "playeruuid2";
            public static final String ROUNDUUID = "rounduuid";
            public static final String DATE = "date";
            public static final String TITLE = "title";
            public static final String SIZE = "size";
            public static final String BOARD = "board";
        }
    }

}
