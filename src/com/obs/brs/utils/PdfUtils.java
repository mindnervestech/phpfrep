package com.obs.brs.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


public class PdfUtils {



	private String replaceCategoryReportHtmlBody(String pdfTemplatePath,String tableBody) throws Exception {
		HashMap<String,String> repaceContent = new HashMap<String,String>();
		repaceContent.put("${table_body}", tableBody);
		return replaceTokens(pdfTemplatePath, repaceContent);
	}

	/**
	 * replace content from template file. replace token and replace string are in map.
	 * @param filePath
	 * @param tokens
	 * @return message
	 * @throws Exception
	 */
	private String replaceTokens(String filePath, Map tokens) throws Exception {
		String message = new IoUtils().read(filePath);
		for (Iterator iterator = tokens.keySet().iterator(); iterator.hasNext();) {
			String token = (String) iterator.next();
			message = StringUtils.replace(message, token, (String)tokens.get(token));
		}
		return message;
	}

	/**
	 * create pdf from html content and download pdf file
	 * @param htmlContent
	 * @param path
	 * @param fileName
	 * @param title
	 * @throws Exception
	 */
	private void createPdfDocument(String htmlContent, String path, String fileName, String title) throws Exception{
		com.itextpdf.text.Document document =
				new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4);
		new File(path).mkdir();
		FileOutputStream fos = new FileOutputStream(path+"/"+fileName);
		com.itextpdf.text.pdf.PdfWriter pdfWriter =
				com.itextpdf.text.pdf.PdfWriter.getInstance(document, fos);
		document.open();
		document.addCreationDate();
		document.addTitle(title);
		com.itextpdf.text.html.simpleparser.HTMLWorker htmlWorker =
				new com.itextpdf.text.html.simpleparser.HTMLWorker(document);
		htmlWorker.parse(new StringReader(htmlContent.toString()));
		document.close();
		fos.close();
	}
}
