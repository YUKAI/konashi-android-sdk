package com.uxxu.konashi.test_all_functions;

import android.view.ViewGroup;
import android.widget.TableRow;

/**
 * Created by kiryu on 7/28/15.
 */
public class Utils {

    public static TableRow.LayoutParams createTableRowLayoutParamwWithWeight(float weight) {
        return new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, weight);
    }
}
