package com.common.util;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class DownLoad {
	
	static DownLoad download;
	
	public static void down(HttpServletRequest request , HttpServletResponse response, String path, String fileName) throws Exception{
		
		File file = new File(path + fileName);

		if (file.exists()) {

			byte buff[] = new byte[2048];
			response.reset();
			String sClient = request.getHeader("User-Agent");

			try {
				if (sClient.indexOf("MSIE 5.5") > -1) {
					response.setHeader("Content-Disposition", "filename="
							+ fileName + ";");
				} else {
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName + ";");
				}

				BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
				BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());
				int read = 0;

				while ((read = fin.read(buff)) != -1) {
					outs.write(buff, 0, read);
				}

				if (outs != null)
					outs.close();
				if (fin != null)
					fin.close();
			} catch (IOException e) {
				response.setContentType("text/html");
				throw new Exception("Error IOException : "+ e.toString());
			}
		} else {
			response.setContentType("text/html");
			throw new Exception("File Not Found : "+path+fileName );
		}
	}
	
public static void down(HttpServletRequest request , HttpServletResponse response, String path, String fileName, String newFileName) throws Exception{
		
		File file = new File(path + fileName);

		if (file.exists()) {

			byte buff[] = new byte[2048];
			response.reset();
			String sClient = request.getHeader("User-Agent");

			try {
				if (sClient.indexOf("MSIE 5.5") > -1) {
					response.setHeader("Content-Disposition", "filename="
							+ new String(newFileName.getBytes("KSC5601"), "8859_1") + ";");
				} else {
					response.setHeader("Content-Disposition",
							"attachment; filename=" + new String(newFileName.getBytes("KSC5601"), "8859_1") + ";");
				}

				BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
				BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());
				int read = 0;

				while ((read = fin.read(buff)) != -1) {
					outs.write(buff, 0, read);
				}

				if (outs != null)
					outs.close();
				if (fin != null)
					fin.close();
			} catch (IOException e) {
				response.setContentType("text/html");
				throw new Exception("Error IOException : "+ e.toString());
			}
		} else {
			response.setContentType("text/html");
			throw new Exception("File Not Found : "+path+fileName );
		}
	}

	public static void downNoEncoding(HttpServletRequest request , HttpServletResponse response, String path, String fileName, String newFileName) throws Exception{
	
	File file = new File(path + fileName);

	if (file.exists()) {

		byte buff[] = new byte[2048];
		response.reset();
		String sClient = request.getHeader("User-Agent");

		try {
			if (sClient.indexOf("MSIE 5.5") > -1) {
				response.setHeader("Content-Disposition", "filename="
						+ newFileName + ";");
			} else {
				response.setHeader("Content-Disposition",
						"attachment; filename=" + newFileName + ";");
			}

			BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());
			int read = 0;

			while ((read = fin.read(buff)) != -1) {
				outs.write(buff, 0, read);
			}

			if (outs != null)
				outs.close();
			if (fin != null)
				fin.close();
		} catch (IOException e) {
			response.setContentType("text/html");
			throw new Exception("Error IOException : "+ e.toString());
		}
	} else {
		response.setContentType("text/html");
		throw new Exception("File Not Found : "+path+fileName );
	}
	}
}
