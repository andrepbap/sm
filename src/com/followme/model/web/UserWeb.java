package com.followme.model.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.followme.utils.HttpConnection;

import android.util.Log;

public abstract class UserWeb {

	private static JSONObject jo;
	
	private static final String TAG = UserWeb.class.getSimpleName();
	
	public static String login(String email, String password){
		String url = ServerParams.getApiUrl() + "user/login";
		
		jo = new JSONObject();
		
		try {
			jo.put("apiKey", ServerParams.getEncryptedApiKey());
			jo.put("email", email);
			jo.put("password", password);

		} catch (JSONException e1) {
			Log.e(TAG, "login error");
		}

		return HttpConnection.getSetDataWeb(url, jo.toString());
	}
	
	public static String saveFacebookUser(String facebookId, String userName, String email, String photo_patch){
		String url = ServerParams.getApiUrl() + "user/save-facebook-user";
		
		jo = new JSONObject();
		
		try {
			jo.put("apiKey", ServerParams.getEncryptedApiKey());
			jo.put("facebookId", facebookId);
			jo.put("userName", userName);
			jo.put("email", email);
			jo.put("photo_patch", photo_patch);
		} catch (JSONException e1) {
			Log.e(TAG, "login error");
		}
		Log.i(TAG, jo.toString());
		return HttpConnection.getSetDataWeb(url, jo.toString());
	}
	
	public static String atualizaPosicao(int idUser, Double latitude, Double longitude) {
		String url = ServerParams.getApiUrl() + "user/" + idUser + "/set-position";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		
		jo = new JSONObject();
		try {
			jo.put("apiKey", ServerParams.getEncryptedApiKey());
			jo.put("latitude", latitude);
			jo.put("longitude", longitude);
			jo.put("date", currentDateandTime);

		} catch (JSONException e1) {
			Log.e(TAG, "refresh position error");
		}

		return HttpConnection.getSetDataWeb(url, jo.toString());
	}
	
	public static String getGroups(int idUser){
		String url = ServerParams.getApiUrl() + "user/"+ idUser +"/get-groups";
		
		jo = new JSONObject();
		try {
			jo.put("apiKey", ServerParams.getEncryptedApiKey());

		} catch (JSONException e1) {
			Log.e(TAG, "get groups error");
		}
		
		return HttpConnection.getSetDataWeb(url, jo.toString());
	}
	
	public static String register(String name, String email, String password) {
		String url = ServerParams.getApiUrl() + "user/save";
		
		jo = new JSONObject();
		try {
			jo.put("apiKey", ServerParams.getEncryptedApiKey());
			jo.put("userName", name);
			jo.put("email", email);
			jo.put("password", password);
		} catch (JSONException e1) {
			Log.e(TAG, "register error");
		}

		return HttpConnection.getSetDataWeb(url, jo.toString());
	}
	
}
