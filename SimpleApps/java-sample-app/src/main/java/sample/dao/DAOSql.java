package sample.dao;

public class DAOSql {
    
    /* ORG_TBL */
    public static String SQL_SELECT_ORG_MAX_ID = "SELECT max(id)+1 id FROM ORG_TBL ";
    
    public static String SQL_SELECT_ORGS = "SELECT id, label, `desc`, url, created, modified "
            + " FROM ORG_TBL ";

    public static String SQL_SELECT_ORG = "SELECT id, label, `desc`, url, created, modified "
                                        + " FROM ORG_TBL "
                                        + " WHERE id = ? ";

    public static String SQL_SELECT_ORGID_BY_LABEL = "SELECT id "
            + " FROM ORG_TBL "
            + " WHERE label = ? ";
    
    public static String SQL_INSERT_ORG = "INSERT INTO ORG_TBL( id, label, `desc`, url, created, modified ) "
                                        +              "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ";


    public static String SQL_UPDATE_ORG =    "UPDATE ORG_TBL "
                                           + "   SET label = ? "
                                           + "     , `desc` = ? "
                                           + "     , url = ? "
                                           + "     , modified = CURRENT_TIMESTAMP "
                                           + " WHERE id = ? " ;

    public static String SQL_DELETE_ORG = "DELETE FROM ORG_TBL "              
                                            + " WHERE id = ? ";


    /* GROUP_TBL */
    public static String SQL_SELECT_GROUP_MAX_ID = "SELECT max(id)+1 id FROM GROUP_TBL ";
    
    public static String SQL_SELECT_GROUPS = "SELECT id, org_id, parent_id, label, `desc`, thumb_img_name, thumb_img_path, url, created, modified "
            + " FROM GROUP_TBL "
            + " WHERE org_id = ? ";
    
    public static String SQL_SELECT_GROUP = "SELECT id, org_id, parent_id, label, `desc`, thumb_img_name, thumb_img_path, url, created, modified "
            + " FROM GROUP_TBL "
            + " WHERE id = ? ";

    public static String SQL_INSERT_GROUP = "INSERT INTO GROUP_TBL( id, org_id, parent_id, label, `desc`, thumb_img_name, thumb_img_path, url, created, modified ) " 
                                            +             " VALUES( ?,  ?,      ?,         ?,     ?,      ?,              ?,              ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)" ;

    public static String SQL_UPDATE_GROUP = "UPDATE GROUP_TBL "
                                            + " SET org_id = ?  "
                                            + " , parent_id = ? "
                                            + " , label = ? "
                                            + " , `desc` = ? "
                                            + " , thumb_img_name = ? "
                                            + " , thumb_img_path = ? "
                                            + " , url = ? "
                                            + "     , modified = CURRENT_TIMESTAMP "
                                            + " WHERE id = ? ";

    public static String SQL_DELETE_GROUP = "DELETE FROM GROUP_TBL "
                                            + " WHERE id = ? ";

}
