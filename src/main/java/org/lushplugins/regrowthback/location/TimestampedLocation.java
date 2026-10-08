package org.lushplugins.regrowthback.location;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.regrowthback.RegrowthBack;
import org.lushplugins.regrowthback.util.MathUtil;


import java.time.Instant;

@JsonAutoDetect(
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE,
    fieldVisibility = JsonAutoDetect.Visibility.ANY
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimestampedLocation {
    private final String worldName;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    private final long creationEpochSeconds;

    @JsonCreator
    public TimestampedLocation(
        @JsonProperty("worldName") String worldName,
        @JsonProperty("x") double x,
        @JsonProperty("y") double y,
        @JsonProperty("z") double z,
        @JsonProperty("yaw") float yaw,
        @JsonProperty("pitch") float pitch,
        @JsonProperty("creationEpochSeconds") long creationEpochSeconds
    ) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.creationEpochSeconds = creationEpochSeconds;
    }

    public TimestampedLocation(Location location) {
        this(
            location.getWorld().getName(),
            MathUtil.round(location.x()),
            MathUtil.round(location.y()),
            MathUtil.round(location.z()),
            MathUtil.round(location.getYaw()),
            MathUtil.round(location.getPitch()),
            Instant.now().getEpochSecond()
        );
    }

    public @Nullable World getWorld() {
        return Bukkit.getWorld(this.worldName);
    }

    public @Nullable Location toLocation() {
        World world = getWorld();
        return world != null ? new Location(world, x, y, z, yaw, pitch) : null;
    }

    public boolean hasExpired() {
        int expiryTime = RegrowthBack.getInstance().getConfigManager().expiryTime();
        return this.creationEpochSeconds + expiryTime < Instant.now().getEpochSecond();
    }
}
