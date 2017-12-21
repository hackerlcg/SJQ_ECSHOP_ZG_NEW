package com.ecjia.component.network.model;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

public class ActivityManagerModel extends BaseModel
{
    public static ArrayList <Activity> liveActivityList = new ArrayList<>();
    public static ArrayList <Activity> visibleActivityList = new ArrayList<>();
    public static ArrayList <Activity> foregroundActivityList = new ArrayList<>();


    public ActivityManagerModel(Context context)
    {
        super(context);
    }

    public static void addLiveActivity(Activity baseActivity)
    {
       if (!liveActivityList.contains(baseActivity))
       {
           liveActivityList.add(baseActivity);
       }
    }

    public static void removeLiveActivity(Activity baseActivity)
    {
        liveActivityList.remove(baseActivity);
        visibleActivityList.remove(baseActivity);
        foregroundActivityList.remove(baseActivity);
    }


    public static void addVisibleActivity(Activity baseActivity)
    {
        if (!visibleActivityList.contains(baseActivity))
        {
            visibleActivityList.add(baseActivity);
        }
    }

    public static void removeVisibleActivity(Activity baseActivity)
    {
        visibleActivityList.remove(baseActivity);
    }


}
