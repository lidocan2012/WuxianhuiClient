package com.jsondemo.tools;

import java.util.HashSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SPHelper {
	SharedPreferences sp;
	public static SharedPreferences.Editor editor;
	@SuppressLint("CommitPrefEdits") 
	public SPHelper(Context c, String name){
		sp = c.getSharedPreferences(name, Context.MODE_PRIVATE);
		editor = sp.edit();
	}
	public void putValues(String key, HashSet<String> values){
		editor.putStringSet(key, values);
		editor.commit();
	}
	public HashSet<String> getValues(String key){
		return (HashSet<String>) sp.getStringSet(key, null);
	}
	public void remove(String key){
		editor.remove(key);
		editor.commit();
	}
	public void putValue(String key,String value){
		editor.putString(key, value);
		editor.commit();
	}
	public String getValue(String key){
		return sp.getString(key, null);
	}
}
