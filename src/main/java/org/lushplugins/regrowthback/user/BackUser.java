package org.lushplugins.regrowthback.user;

import org.jetbrains.annotations.Nullable;
import org.jooq.Record;
import org.lushplugins.regrowthback.RegrowthBack;
import org.lushplugins.regrowthback.location.TimestampedLocation;
import org.lushplugins.regrowthback.storage.UsersTable;
import org.lushplugins.regrowthback.storage.binding.BinaryUUIDBinding;
import org.lushplugins.regrowthback.storage.binding.StringTimestampedLocationBinding;

import java.util.UUID;

public class BackUser {
    private final UUID uuid;
    private TimestampedLocation backLocation;
    private TimestampedLocation deathLocation;

    public BackUser(
        UUID uuid,
        TimestampedLocation backLocation,
        TimestampedLocation deathLocation
    ) {
        this.uuid = uuid;
        this.backLocation = backLocation;
        this.deathLocation = deathLocation;
    }

    public BackUser(UUID uuid) {
        this(uuid, null, null);
    }

    public @Nullable TimestampedLocation backLocation() {
        validateLocations();
        return backLocation;
    }

    public void backLocation(TimestampedLocation location) {
        this.backLocation = location;
        save();
    }

    public @Nullable TimestampedLocation deathLocation() {
        validateLocations();
        return deathLocation;
    }

    public void deathLocation(TimestampedLocation location) {
        this.deathLocation = location;
        save();
    }

    public void validateLocations() {
        if (backLocation != null && backLocation.hasExpired()) {
            backLocation(null);
        }

        if (deathLocation != null && deathLocation.hasExpired()) {
            deathLocation(null);
        }
    }

    public void save() {
        String backLocation = StringTimestampedLocationBinding.to(this.backLocation);
        String deathLocation = StringTimestampedLocationBinding.to(this.deathLocation);

        RegrowthBack.getInstance().getStorageHandler().execute(context -> context
            .insertInto(UsersTable.TABLE)
            .set(UsersTable.UUID, BinaryUUIDBinding.to(uuid))
            .set(UsersTable.BACK_LOCATION, backLocation)
            .set(UsersTable.DEATH_LOCATION, deathLocation)
            .onConflict(UsersTable.UUID)
            .doUpdate()
            .set(UsersTable.BACK_LOCATION, backLocation)
            .set(UsersTable.DEATH_LOCATION, deathLocation)
            .execute()
        );
    }

    public static BackUser read(Record record) {
        return new BackUser(
            BinaryUUIDBinding.from(record.get(UsersTable.UUID)),
            StringTimestampedLocationBinding.from(record.get(UsersTable.BACK_LOCATION)),
            StringTimestampedLocationBinding.from(record.get(UsersTable.DEATH_LOCATION))
        );
    }
}
