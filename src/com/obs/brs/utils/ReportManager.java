package com.obs.brs.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Map;

//import net.sf.jasperreports.engine.JasperCompileManager;

/**
 * @author OptiSol
 *
 */
public class ReportManager {

	public static boolean callReportGenerator(Map paramMap) {
		boolean bFlag = ReportUtils.reportGenerator( paramMap );
		return bFlag;
	}
	public static void createReportFile( ByteArrayOutputStream baos, String outFilepath ) throws Exception{
		FileOutputStream os = new FileOutputStream( outFilepath );
		if( baos != null && baos.size() > 0 ){
			os.write( baos.toByteArray());
		}
		os.flush();
		os.close();
		baos.flush();
		baos.close();
	}
	
	public static void compileJasperReport( String srcjrXMLPath, String destjrXMLPath ){
		try {
			//JasperCompileManager.compileReportToFile( srcjrXMLPath, destjrXMLPath );
		} catch ( Exception e ) {
			System.out.println( "Error while compiling report of "+ srcjrXMLPath + " " + e );
		}
	}
	
	public static void generateNoPageDisplay( String sHTMLCode, String sOutPath )throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write( sHTMLCode.getBytes() );
		//ReportUtils.createReportFile( baos, sOutPath );
	}
	public static String getHTMLJavaScript ( boolean isPrint, boolean isPDF, String pdfPath , boolean isXLS, String xlsPath ,boolean isCSV,String csvPath )
	{
		return ReportUtils.getHTMLJavaScript(isPrint, isPDF, pdfPath, isXLS , xlsPath, isCSV, csvPath);
	}
	

}

