package com.uxxu.konashi.lib;

import com.uxxu.konashi.lib.events.KonashiEvent;
import com.uxxu.konashi.lib.listeners.KonashiBaseListener;

import java.util.ArrayList;

/**
 * konashiのイベントをKonashiObserverに伝えるクラス
 * 
 * @author monakaz, YUKAI Engineering
 * http://konashi.ux-xu.com
 * ========================================================================
 * Copyright 2014 Yukai Engineering Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
public class KonashiNotifier {
    /**
     * オブザーバたち
     */
    private ArrayList<KonashiBaseListener> mListeners = null;

    /**
     * コンストラクタ
     */
    public KonashiNotifier() {
        mListeners = new ArrayList<>();
    }

    /**
     * リスナーを追加する
     * @param listener 追加するリスナー
     */
    public void addListener(KonashiBaseListener listener){
        if(!mListeners.contains(listener)){
            mListeners.add(listener);
        }
    }

    /**
     * リスナーを削除する
     * @param listener 削除するリスナー
     */
    public void removeListener(KonashiBaseListener listener){
        if(mListeners.contains(listener)){
            mListeners.remove(listener);
        }
    }

    /**
     * オブザーバをすべて削除する
     */
    public void removeAllListeners(){
        mListeners.clear();
    }

    /**
     * オブザーバを追加する
     * @param observer 追加するオブザーバ
     * @deprecated This method deprecated in 0.5.0.
     * Use {@link #addListener(KonashiBaseListener)} instead.
     */
    @Deprecated
    public void addObserver(KonashiObserver observer){
        addListener(observer);
    }
    
    /**
     * オブザーバを削除する
     * @param observer 削除するオブザーバ
     * @deprecated This method deprecated in 0.5.0.
     * Use {@link #removeListener(KonashiBaseListener)} instead.
     */
    @Deprecated
    public void removeObserver(KonashiObserver observer){
        removeListener(observer);
    }
    
    /**
     * オブザーバをすべて削除する
     * @deprecated This method deprecated in 0.5.0.
     * Use {@link #removeAllListeners()} instead.
     */
    @Deprecated
    public void removeAllObservers(){
        removeAllListeners();
    }
    
    /**
     * オブザーバにイベントを通知する
     * @param event イベント名(KonashiEventだよっ）
     */
    public void notifyKonashiEvent(final KonashiEvent event, final Object param0, final Object param1){
        for(final KonashiBaseListener listener : mListeners){
            final KonashiObserver observer = (listener instanceof KonashiObserver) ? (KonashiObserver) listener : null;
            if(observer != null && !observer.getActivity().isDestroyed()) {
                observer.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyKonashiEvent(event, param0, param1, observer);
                    }
                });
            } else {
                notifyKonashiEvent(event, param0, param1, listener);
            }
        }
    }

    public void notifyKonashiError(final KonashiErrorReason errorReason){
        // 呼び出し元のメソッド名
        final String cause = errorReason.name() + " on " + new Throwable().getStackTrace()[2].getMethodName() + "()";
        for(final KonashiBaseListener listener : mListeners){
            final KonashiObserver observer = (listener instanceof KonashiObserver) ? (KonashiObserver) listener : null;
            if(observer != null && !observer.getActivity().isDestroyed()) {
                observer.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyKonashiError(errorReason, cause, listener);
                    }
                });
            } else {
                notifyKonashiError(errorReason, cause, listener);
            }
        }
    }

    private void notifyKonashiEvent(KonashiEvent event, Object param0, Object param1, KonashiBaseListener listener) {
        event.notify(param0, param1, listener);
    }

    private void notifyKonashiError(KonashiErrorReason errorReason, String cause, KonashiBaseListener listener) {
        listener.onError(errorReason, cause);
    }
}
