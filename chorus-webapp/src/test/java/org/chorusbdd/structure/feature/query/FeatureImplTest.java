package org.chorusbdd.structure.feature.query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeatureImplTest {

    @Mock Supplier<String> mockTextSupplier;
    @Mock Supplier<String> mockMd5Supplier;

    @Test(expected=NullPointerException.class)
    public void disallowsNullFeatureId() {
        new FeatureImpl(null, "package-id-123", "human name", mockTextSupplier, mockMd5Supplier);
    }

    @Test(expected=IllegalArgumentException.class)
    public void disallowsEmptyFeatureId() {
        new FeatureImpl("", "package-id-123", "human name", mockTextSupplier, mockMd5Supplier);
    }

    @Test(expected=NullPointerException.class)
    public void disallowsNullPakageId() {
        new FeatureImpl("feature-id-123", null, "human name", mockTextSupplier, mockMd5Supplier);
    }

    @Test(expected=IllegalArgumentException.class)
    public void disallowsEmptyPakageId() {
        new FeatureImpl("feature-id-123", "", "human name", mockTextSupplier, mockMd5Supplier);
    }

    @Test(expected=NullPointerException.class)
    public void disallowsNullHumanName() {
        new FeatureImpl("feature-id-123", "package-id-123", null, mockTextSupplier, mockMd5Supplier);
    }

    @Test(expected=IllegalArgumentException.class)
    public void disallowsEmptyHumanName() {
        new FeatureImpl("feature-id-123", "package-id-123", "", mockTextSupplier, mockMd5Supplier);
    }

    @Test(expected=NullPointerException.class)
    public void disallowsNullContentsSupplier() {
        new FeatureImpl("feature-id-123", "package-id-123", "human name", null, mockMd5Supplier);
    }

    @Test(expected=NullPointerException.class)
    public void disallowsNullMd5Supplier() {
        new FeatureImpl("feature-id-123", "package-id-123", "human name", mockTextSupplier, null);
    }

    @Test
    public void hasPropertiesSetByConstructor() {
        // run
        final FeatureImpl feature = new FeatureImpl("feature-id-123", "package-id-123", "human name", mockTextSupplier, mockMd5Supplier);

        // verify
        assertThat(feature.id(), is("feature-id-123"));
        assertThat(feature.pakageId(), is("package-id-123"));
        assertThat(feature.humanName(), is("human name"));
    }

    @Test
    public void hasTextRetrievedFromTextSupplier() {
        // prepare
        when(mockTextSupplier.get()).thenReturn("SOME TEXT");

        // run
        final FeatureImpl feature = new FeatureImpl("feature-id-123", "package-id-123", "human name", mockTextSupplier, mockMd5Supplier);

        // verify
        assertThat(feature.text(), is("SOME TEXT"));
    }

    @Test
    public void hasMd5RetrievedFrommd5Supplier() {
        // prepare
        when(mockMd5Supplier.get()).thenReturn("SOME MD5");

        // run
        final FeatureImpl feature = new FeatureImpl("feature-id-123", "package-id-123", "human name", mockTextSupplier, mockMd5Supplier);

        // verify
        assertThat(feature.md5(), is("SOME MD5"));
    }
}
