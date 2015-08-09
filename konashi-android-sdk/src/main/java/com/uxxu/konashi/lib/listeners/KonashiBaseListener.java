package com.uxxu.konashi.lib.listeners;

import com.uxxu.konashi.lib.KonashiErrorReason;

/**
 * Created by izumin on 8/5/15.
 */
public interface KonashiBaseListener {
    /**
     * エラーが起きた時に呼ばれる
     */
    void onError(KonashiErrorReason errorReason, String message);
}
