package Http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

import Http.global;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import sun.misc.BASE64Encoder;

public class MyThread extends Thread{
	@Override
	public void run() {
		post();
	}
	
	public synchronized void addSuccess(int size) {
		global.success += size;
	}
	
	public synchronized void addFailed(int size) {
		global.failed += size;
	}
	
	public void post() {
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
		TreeMap<String, String> bd = new TreeMap<String, String>();
		bd.put("Account", "string");
		bd.put("Password", "string");
		
		for(int i=0;i<50;i++) {
			String bds = getBody(bd);
			RequestBody body = RequestBody.create(mediaType, bds);
			Request request = new Request.Builder()
					.url("http://192.168.0.230:30050/account/login")
					.post(body)
					.addHeader("Accept", "application/json")
					.addHeader("Content-Type", "application/json-patch+json")
					.build();
			try (Response response = client.newCall(request).execute()) {
			      System.out.println(response.body().string());
			      addSuccess(1);
			    } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					addFailed(1);
				}
			}
	}
	
	private String getSign(TreeMap<String, String> bd) {
		String sign = "";
		Boolean first = true;
		for(String key: bd.keySet()) {
			if(!first)
				sign += "&";
			else
				first = false;
			sign += key + "=" + bd.get(key);
		}
		String time = String.format("%s", System.currentTimeMillis());
		sign += "85653b8832ad55cd" + time;
		try {
			sign = Md5(sign);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time + sign;
	}
	
	private String getBody(TreeMap<String, String> bd) {
		String body = "{";
		for(String key: bd.keySet()) {
			body += "\"" + key + "\"" + ":\"" + bd.get(key) + "\"" + ",";
		}
		return body + "\"sign\":" + "\"" + getSign(bd) + "\"}"; 
	}
	
	private String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        //确定计算方法
        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }
	
	private String Md5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest md5=MessageDigest.getInstance("MD5");
		return toHex(md5.digest(str.getBytes("utf-8")));
	}
	
	private static String toHex(byte[] bytes) {

	    final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
	    StringBuilder ret = new StringBuilder(bytes.length * 2);
	    for (int i=0; i<bytes.length; i++) {
	        ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
	        ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
	    }
	    return ret.toString();
	}
}
