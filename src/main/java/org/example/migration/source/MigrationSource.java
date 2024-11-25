package org.example.migration.source;

import java.io.InputStream;
import java.util.List;

public interface MigrationSource {
    List<InputStream> getMigrationFiles();
}
