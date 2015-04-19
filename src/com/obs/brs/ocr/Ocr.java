package com.obs.brs.ocr;

import java.io.File;


import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Ocr {
	
	/*static final boolean WINDOWS = System.getProperty("os.name").toLowerCase().startsWith("Linux");
	public static final String LIB_NAME = "libtesseract302";
	public static final String LIB_NAME_NON_WIN = "/usr/local/lib/libtesseract.so";
	public static final TessAPI INSTANCE = (TessAPI) Native.loadLibrary(WINDOWS ? LIB_NAME : LIB_NAME_NON_WIN, TessAPI.class);*/
	/**
	 * read text from the Image using Tesseract OCR lib
	 * @param File
	 * @return String
	 */
	public String doOCR(File imageFile){
		  //System.setProperty("jna.library.path", "/usr/local/lib/libtesseract.so");
		String result = null;
		try {
			if(imageFile.exists()){
				Tesseract instance = Tesseract.getInstance();
				result = instance.doOCR(imageFile);
			}
		} catch (TesseractException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return result;
	}
}
