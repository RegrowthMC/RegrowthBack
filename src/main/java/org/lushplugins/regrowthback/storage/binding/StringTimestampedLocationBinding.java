package org.lushplugins.regrowthback.storage.binding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jspecify.annotations.NonNull;
import org.lushplugins.lushlib.jackson.JacksonHelper;
import org.lushplugins.regrowthback.location.TimestampedLocation;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;

public class StringTimestampedLocationBinding implements Binding<String, TimestampedLocation> {
    private static final ObjectMapper MAPPER = JacksonHelper.addCustomSerializers(new ObjectMapper());

    @Override
    public @NotNull Converter<String, TimestampedLocation> converter() {
        return new Converter<>() {
            @Override
            public TimestampedLocation from(String string) {
                if (string == null) return null;

                try {
                    return MAPPER.readValue(string, TimestampedLocation.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String to(TimestampedLocation location) {
                if (location == null) return null;

                try {
                    return MAPPER.writeValueAsString(location);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public @NonNull Class<String> fromType() {
                return String.class;
            }

            @Override
            public @NonNull Class<TimestampedLocation> toType() {
                return TimestampedLocation.class;
            }
        };
    }

    @Override
    public void sql(BindingSQLContext<TimestampedLocation> context) {
        context.render().visit(DSL.val(context.convert(converter()).value())).sql("");
    }

    @Override
    public void register(BindingRegisterContext<TimestampedLocation> context) throws SQLException {
        context.statement().registerOutParameter(context.index(), Types.VARCHAR);
    }

    @Override
    public void set(BindingSetStatementContext<TimestampedLocation> context) throws SQLException {
        context.statement().setString(context.index(), converter().to(context.value()));
    }

    @Override
    public void get(BindingGetResultSetContext<TimestampedLocation> context) throws SQLException {
        context.value(converter().from(context.resultSet().getString(context.index())));
    }

    @Override
    public void get(BindingGetStatementContext<TimestampedLocation> context) throws SQLException {
        context.value(converter().from(context.statement().getString(context.index())));
    }

    @Override
    public void set(BindingSetSQLOutputContext<TimestampedLocation> bindingSetSQLOutputContext) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void get(BindingGetSQLInputContext<TimestampedLocation> bindingGetSQLInputContext) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    // TODO: Remove when bindings work
    public static TimestampedLocation from(String string) {
        if (string == null) return null;

        try {
            return MAPPER.readValue(string, TimestampedLocation.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String to(TimestampedLocation location) {
        if (location == null) return null;

        try {
            return MAPPER.writeValueAsString(location);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
