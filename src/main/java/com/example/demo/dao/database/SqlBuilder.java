package com.example.demo.dao.database;

import java.util.ArrayList;
import java.util.List;

public class SqlBuilder {
    private final StringBuilder sql = new StringBuilder();
    private final List<String> clauses = new ArrayList<>();

    public SqlBuilder select(String rows) {
        sql.append("SELECT ");
        sql.append(rows);
        return this;
    }

    public SqlBuilder from(String tables) {
        sql.append(" FROM ");
        sql.append(tables);

        return this;
    }

    public SqlBuilder where(String clause) {
        clauses.add(clause);
        return this;
    }

    public String build() {
        if (clauses.size() > 0) {
            sql.append(" WHERE ");

            for (int i = 0; i < clauses.size(); i++) {
                if (i != clauses.size() - 1) {
                    sql.append(clauses.get(i)).append(" and ");
                } else {
                    sql.append(clauses.get(i)).append(";");
                }
            }
        }

        return sql.toString();
    }
}
