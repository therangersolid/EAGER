package com.pk.eager.view.android;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by therangersolid on 11/28/17.
 */

public class Utility {
    public static Resources r;

    // Use getResources to pass this!. At least run it once on UI Thread
    public static void initialize(Resources res) {
        r = res;
    }

    public static int dipToPX(int size) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, size, r.getDisplayMetrics()));
    }

}
