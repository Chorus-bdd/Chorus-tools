package org.chorusbdd.structure.feature.query;

import org.chorusbdd.structure.feature.Feature;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.function.Supplier;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

@Immutable
public class FeatureImpl implements Feature {

    private final String id;
    private final String pakageId;

    private final Supplier<String> textSupplier;
    private final Supplier<String> md5Supplier;
    private final String humanName;

    public FeatureImpl(final String id, final String pakageId, final String humanName,
                        final Supplier<String> textSupplier,
                        final Supplier<String> md5Supplier) {
        this.id = notBlank(id);
        this.pakageId = notBlank(pakageId);
        this.humanName = notBlank(humanName);
        this.textSupplier = notNull(textSupplier);
        this.md5Supplier = notNull(md5Supplier);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String pakageId() {
        return this.pakageId;
    }

    @Override
    public String humanName() {
        return humanName;
    }

    @Override
    public String text() {
        return textSupplier.get();
    }

    @Override
    public String md5() {
        return md5Supplier.get();
    }
}
