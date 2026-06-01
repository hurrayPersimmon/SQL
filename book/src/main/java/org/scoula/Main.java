package org.scoula;

import org.scoula.database.JDBCUtil;

public class Main {

    public static void main(String[] args) {
        System.out.println(JDBCUtil.getConnection());
    }
}