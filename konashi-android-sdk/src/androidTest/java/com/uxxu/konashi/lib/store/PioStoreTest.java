package com.uxxu.konashi.lib.store;

import android.support.test.runner.AndroidJUnit4;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.dispatcher.CharacteristicDispatcher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by izumin on 8/18/15.
 */
@RunWith(AndroidJUnit4.class)
public class PioStoreTest {

    @Mock private CharacteristicDispatcher mDispatcher;
    private PioStore mPioStore;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mPioStore = new PioStore(mDispatcher);
        Whitebox.setInternalState(mPioStore, "mInputs", (byte) 0x17);
    }

    @Test
    public void testGetPioInput() throws Exception {
        assertThat(mPioStore.getInput(Konashi.PIO1)).isEqualTo((byte) Konashi.HIGH);
        assertThat(mPioStore.getInput(Konashi.PIO5)).isEqualTo((byte) Konashi.LOW);
    }
}