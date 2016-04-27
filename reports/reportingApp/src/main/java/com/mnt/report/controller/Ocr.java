package com.mnt.report.controller;

import java.io.File;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Ocr {

	public String doOCR(File imageFile){
		  
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
