package org.lushplugins.regrowthback.storage;

import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.lushplugins.regrowthback.location.TimestampedLocation;
import org.lushplugins.regrowthback.storage.binding.BinaryUUIDBinding;
import org.lushplugins.regrowthback.storage.binding.StringTimestampedLocationBinding;

import java.util.UUID;

public class UsersTable {
    public static final Table<Record> TABLE = DSL.table("regrowthback_users");

    private static final DataType<UUID> UUID_TYPE = SQLDataType.BINARY(16).asConvertedDataType(new BinaryUUIDBinding());
    private static final DataType<TimestampedLocation> TIMESTAMPED_LOCATION_TYPE = SQLDataType.VARCHAR.asConvertedDataType(new StringTimestampedLocationBinding());

    public static final Field<byte[]> UUID = DSL.field("uuid", SQLDataType.BINARY(16).notNull());
    //    public static final Field<UUID> UUID = DSL.field("uuid", SQLDataType.UUID);
    public static final Field<String> BACK_LOCATION = DSL.field("back_location", SQLDataType.VARCHAR);
    public static final Field<String> DEATH_LOCATION = DSL.field("death_location", SQLDataType.VARCHAR);

    public static void createTableIfNotExists(DSLContext context) {
        context
            .createTableIfNotExists(UsersTable.TABLE)
            .column(UsersTable.UUID)
            .column(UsersTable.BACK_LOCATION)
            .column(UsersTable.DEATH_LOCATION)
            .primaryKey(UsersTable.UUID)
            .execute();
    }
}
