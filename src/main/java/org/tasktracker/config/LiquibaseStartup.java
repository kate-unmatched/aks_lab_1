package org.tasktracker.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import javax.sql.DataSource;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.database.jvm.JdbcConnection;

import java.sql.Connection;

@Singleton
@Startup
public class LiquibaseStartup {
    @Resource(lookup = "jdbc/postgresDS")
    private DataSource dataSource;

    private static final String CHANGELOG = "db/db.changelog-master.yaml";
    private static final String CONTEXTS  = System.getProperty("liquibase.contexts", "dev");

    @PostConstruct
    public void migrateOnStartup() {
        try (Connection c = dataSource.getConnection()) {
            Database db = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(c));
            db.setDefaultSchemaName("tasktracker");
            db.setLiquibaseSchemaName("tasktracker");
            Liquibase lb = new Liquibase(CHANGELOG, new ClassLoaderResourceAccessor(), db);
            lb.update(CONTEXTS);
        } catch (Exception e) {
            throw new IllegalStateException("Liquibase failed", e);
        }
    }
}