package com.perculacreative.preferenceutils.library

import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences

/**
 * Based on https://gist.github.com/rharter/1df1cd72ce4e9d1801bd2d49f2a96810
 * @param sharedPrefs [SharedPreferences] to use
 * @param key Key for the shared preference
 * @param defValue Default value, if no preference exists
 */
abstract class SharedPreferenceLiveData<T>(val sharedPrefs: SharedPreferences,
                                           val key: String,
                                           val defValue: T) : MutableLiveData<T>() {

    private val preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        if (key == this.key) {
            postValue(getValueFromPreferences(key, defValue))
        }
    }

    abstract fun getValueFromPreferences(key: String, defValue: T): T
    protected abstract fun setValueInPreferences(value: T)

    override fun setValue(value: T?) {
        super.setValue(value)
        if (value != null) {
            setValueInPreferences(value)
        } else {
            sharedPrefs.edit().remove(key).apply()
        }
    }

    override fun onActive() {
        super.onActive()
        postValue(getValueFromPreferences(key, defValue))
        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onInactive() {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
        super.onInactive()
    }
}

class SharedPreferenceIntLiveData(sharedPrefs: SharedPreferences, key: String, defValue: Int) :
        SharedPreferenceLiveData<Int>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: Int): Int = sharedPrefs.getInt(key, defValue)
    override fun setValueInPreferences(value: Int) {
        sharedPrefs.edit().putInt(key, value).apply()
    }
}

class SharedPreferenceStringLiveData(sharedPrefs: SharedPreferences, key: String, defValue: String) :
        SharedPreferenceLiveData<String>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: String): String = sharedPrefs.getString(key, defValue)
    override fun setValueInPreferences(value: String) {
        sharedPrefs.edit().putString(key, value).apply()
    }
}

class SharedPreferenceBooleanLiveData(sharedPrefs: SharedPreferences, key: String, defValue: Boolean) :
        SharedPreferenceLiveData<Boolean>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: Boolean): Boolean = sharedPrefs.getBoolean(key, defValue)
    override fun setValueInPreferences(value: Boolean) {
        sharedPrefs.edit().putBoolean(key, value).apply()
    }
}

class SharedPreferenceFloatLiveData(sharedPrefs: SharedPreferences, key: String, defValue: Float) :
        SharedPreferenceLiveData<Float>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: Float): Float = sharedPrefs.getFloat(key, defValue)
    override fun setValueInPreferences(value: Float) {
        sharedPrefs.edit().putFloat(key, value).apply()
    }
}

class SharedPreferenceLongLiveData(sharedPrefs: SharedPreferences, key: String, defValue: Long) :
        SharedPreferenceLiveData<Long>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: Long): Long = sharedPrefs.getLong(key, defValue)
    override fun setValueInPreferences(value: Long) {
        sharedPrefs.edit().putLong(key, value).apply()
    }
}

class SharedPreferenceStringSetLiveData(sharedPrefs: SharedPreferences, key: String, defValue: Set<String>) :
        SharedPreferenceLiveData<Set<String>>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: Set<String>): Set<String> = sharedPrefs.getStringSet(key, defValue)
    override fun setValueInPreferences(value: Set<String>) {
        sharedPrefs.edit().putStringSet(key, value).apply()
    }
}

fun SharedPreferences.intLiveData(key: String, defValue: Int): SharedPreferenceLiveData<Int> {
    return SharedPreferenceIntLiveData(this, key, defValue)
}
fun SharedPreferences.stringLiveData(key: String, defValue: String): SharedPreferenceLiveData<String> {
    return SharedPreferenceStringLiveData(this, key, defValue)
}
fun SharedPreferences.booleanLiveData(key: String, defValue: Boolean): SharedPreferenceLiveData<Boolean> {
    return SharedPreferenceBooleanLiveData(this, key, defValue)
}
fun SharedPreferences.floatLiveData(key: String, defValue: Float): SharedPreferenceLiveData<Float> {
    return SharedPreferenceFloatLiveData(this, key, defValue)
}
fun SharedPreferences.longLiveData(key: String, defValue: Long): SharedPreferenceLiveData<Long> {
    return SharedPreferenceLongLiveData(this, key, defValue)
}
fun SharedPreferences.stringSetLiveData(key: String, defValue: Set<String>): SharedPreferenceLiveData<Set<String>> {
    return SharedPreferenceStringSetLiveData(this, key, defValue)
}