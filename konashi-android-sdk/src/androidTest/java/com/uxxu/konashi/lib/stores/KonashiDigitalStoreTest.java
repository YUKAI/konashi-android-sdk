package com.uxxu.konashi.lib.stores;

import android.support.test.runner.AndroidJUnit4;

import com.uxxu.konashi.lib.Konashi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by izumin on 8/18/15.
 */
@RunWith(AndroidJUnit4.class)
public class KonashiDigitalStoreTest {

    private KonashiDigitalStore mDigitalStore;

    @Before
    public void setUp() throws Exception {
        mDigitalStore = new KonashiDigitalStore();
        Whitebox.setInternalState(mDigitalStore, "mPioInputs", (byte) 0x17);
    }

    @Test
    public void testGetPioInput() throws Exception {
        assertThat(mDigitalStore.getPioInput(Konashi.PIO1)).isEqualTo((byte) Konashi.HIGH);
        assertThat(mDigitalStore.getPioInput(Konashi.PIO5)).isEqualTo((byte) Konashi.LOW);
    }
}