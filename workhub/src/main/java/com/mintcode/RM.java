package com.mintcode;

import android.content.Context;

public class RM {
    public static int getRId(Context context, String rStr) {
        String[] resStr = rStr.split("\\.");
        if (resStr.length != 3) {
            return 0;
        }
        if (!resStr[0].equals("R")) {
            return 0;
        }
        String className = resStr[1];
        String name = resStr[2];
        String packageName = "com.mintcode.launchr";//context.getPackageName();
        Class<?> r = null;
        int id = 0;
        try {
            r = Class.forName(packageName + ".R");

            Class<?>[] classes = r.getClasses();
            Class<?> desireClass = null;

            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }
            if (desireClass != null)
                id = desireClass.getField(name).getInt(desireClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
