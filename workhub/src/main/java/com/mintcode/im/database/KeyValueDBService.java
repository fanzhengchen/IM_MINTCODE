package com.mintcode.im.database;

import org.litepal.LitePalApplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.mintcode.im.entity.KeyValue;
import com.mintcode.launchr.App;

public class KeyValueDBService {

    private static final String KEY = "KeyValueDBService";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private static KeyValueDBService sInstance;

    public KeyValueDBService(Context context) {
        mSharedPreferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public synchronized static KeyValueDBService getInstance() {
        if (sInstance == null) {
            sInstance = getNewInstance(App.getContext());
        }
        return sInstance;
    }

    public synchronized static KeyValueDBService getNewInstance(Context context) {
        LitePalApplication.initialize(context);
        if (sInstance == null) {
            sInstance = new KeyValueDBService(context);
        }
        return sInstance;
    }

    public void put(KeyValue keyValue) {
        String key = keyValue.getKey();
        String value = keyValue.getValue();

        mEditor.putString(key, value);
        mEditor.commit();
//		List<KeyValue> list = DataSupport.where("key = ?",keyValue.getKey()).find(KeyValue.class);
//		if (list != null && list.size() > 0) {
//			keyValue.updateAll("key = ?",keyValue.getKey());
//		}else{
//			keyValue.save();
//		}
    }

    public void put(String key, String value) {
//		String key = keyValue.getKey();
//		String value = keyValue.getValue();
//		
        mEditor.putString(key, value);
        mEditor.commit();
//		List<KeyValue> list = DataSupport.where("key = ?",key).find(KeyValue.class);
//		KeyValue keyValue = new KeyValue();
//		keyValue.setKey(key);
//		keyValue.setValue(value);
//		if (list != null && list.size() > 0) {
//			keyValue.updateAll("key = ?",key);
//		}else{
//			keyValue.save();
//		}
    }


    public String find(String key) {
        return mSharedPreferences.getString(key, null);
//		List<KeyValue> list = DataSupport.where("key = ?",key).find(KeyValue.class);
//		if (list != null && list.size() > 0) {
//			return list.get(0).getValue();
//		}else{
//			return null;
//		}
    }

    public Integer findInt(String key) {
        String value = this.find(key);
        if (value == null) {
            return null;
        } else {
            try {
                Integer i = Integer.parseInt(value);
                return i;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Float findFloat(String key) {
        String value = this.find(key);
        if (value == null) {
            return null;
        } else {
            try {
                Float f = Float.parseFloat(value);
                return f;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Double findDouble(String key) {
        String value = this.find(key);
        if (value == null) {
            return null;
        } else {
            try {
                Double d = Double.parseDouble(value);
                return d;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Long findLong(String key) {
        String value = this.find(key);
        if (value == null) {
            return null;
        } else {
            try {
                Long l = Long.parseLong(value);
                return l;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Short findShort(String key) {
        String value = this.find(key);
        if (value == null) {
            return null;
        } else {
            try {
                Short s = Short.parseShort(value);
                return s;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Character findChar(String key) {
        String value = this.find(key);
        if (value == null) {
            return null;
        } else {
            return value.length() == 1 ? value.charAt(0) : null;
        }

    }

    public Boolean findBoolean(String key) {
        String value = this.find(key);
        if (value == null) {
            return null;
        } else {
            try {
                Boolean b = Boolean.parseBoolean(value);
                return b;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String findString(String key) {
        return this.find(key);
    }

    public String findValue(String key) {
        return find(key);
    }

    public void delete() {
        mEditor.clear();
        mEditor.commit();
//		DataSupport.deleteAll(KeyValue.class);
    }

    public void put(String key, int value) {
        String str = String.valueOf(value);
        this.put(key, str);
    }

    public void put(String key, double value) {
        String str = String.valueOf(value);
        this.put(key, str);
    }

    public void put(String key, float value) {
        String str = String.valueOf(value);
        this.put(key, str);
    }

    public void put(String key, char value) {
        String str = String.valueOf(value);
        this.put(key, str);
    }

    public void put(String key, boolean value) {
        String str = String.valueOf(value);
        this.put(key, str);
    }

    public void put(String key, short value) {
        String str = String.valueOf(value);
        this.put(key, str);
    }

    public void put(String key, long value) {
        String str = String.valueOf(value);
        this.put(key, str);
    }
}
