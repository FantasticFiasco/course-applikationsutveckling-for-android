package com.kindborg.mattias.dialpadapplication;

import java.util.*;

import android.content.*;
import android.content.pm.*;

/**
 * Class containing extensions to the class {@link Intent}.
 */
public class IntentExtensions {

    /**
     * Determines whether any component has registered for specified
     * {@link Intent}. For example, you want to check if a certain intent
     * receiver is available and in case a component is available, you enable a
     * functionality in your application.
     * 
     * @return true if a component is registered to handle the {@link Intent};
     *         otherwise false.
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager mgr = context.getPackageManager();

        List<ResolveInfo> list = mgr.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY);

        return list.size() > 0;
    }
}