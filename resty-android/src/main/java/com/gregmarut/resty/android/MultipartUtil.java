package com.gregmarut.resty.android;

import com.gregmarut.resty.http.request.MultipartEntity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

public class MultipartUtil
{
	private static final String MIME_OCTET_STREAM = "application/octet-stream";
	
	public static void writeMultipart(final HttpURLConnection connection, final MultipartEntity multipartEntity) throws IOException
	{
		final String twoHyphens = "--";
		final String boundary = "=====" + System.currentTimeMillis() + "=====";
		final String lineEnd = "\r\n";
		
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		
		DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
		outputStream.writeBytes(twoHyphens + boundary + lineEnd);
		outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + multipartEntity.getFileName() + "\"" + lineEnd);
		outputStream.writeBytes("Content-Type: " + MIME_OCTET_STREAM + lineEnd);
		
		outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
		
		outputStream.writeBytes(lineEnd);
		
		outputStream.write(multipartEntity.getContents(), 0, multipartEntity.getContents().length);
		
		outputStream.writeBytes(lineEnd);
		
		//for each of the additional params
		for (Map.Entry<String, String> entry : multipartEntity.getParams().entrySet())
		{
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + lineEnd);
			outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(entry.getValue());
			outputStream.writeBytes(lineEnd);
		}
		
		outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	}
}
