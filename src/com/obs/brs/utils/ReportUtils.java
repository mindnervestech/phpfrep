package com.obs.brs.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class ReportUtils  {
	public final static String JRM_BEAN_COLLECTION = "Jasper_Bean_Collection";
	public final static String JRM_DB_CONNECTION = "Jasper_con_SQL";
	public final static String JRM_HSQL_SESSION = "Jasper_HSQL_Session";
	public final static String JRM_QUERY = "Jasper_Query";
	public final static String JRM_JRXML_PATH = "Jasper_JRXML_Path";
	public final static String JRM_IS_BEAN_COLLECTION = "Is_Bean_Collection";
	public final static String JRM_OUT_HTML = "Jasper_OutHTML";
	public final static String JRM_OUT_PDF = "Jasper_OutPDF";
	public final static String JRM_OUT_XLS = "Jasper_OutXLS";
	public final static String JRM_OUT_CSV = "Jasper_OutCSV";
	public final static String JRM_OUT_HTML_FILE = "Jasper_OutHTMLFile";
	public final static String JRM_OUT_PDF_FILE = "Jasper_OutPDFFile";
	public final static String JRM_OUT_CSV_FILE = "Jasper_OutCSVFile";
	public final static String JRM_OUT_XLS_FILE = "Jasper_OutXLSFile";
	public final static String JRM_HTML_START_PAGE_INDEX = "Jasper_HTML_Start_Page_Index";
	public final static String JRM_HTML_END_PAGE_INDEX = "Jasper_HTML_End_Page_Index";
	public final static String JRM_LOGO_PATH = "PARAM_LOGOPATH";
	public final static String JRM_HEADER_VALUE = "Jasper_Header";
	public final static String JRM_IS_HEADER = "Is_Jasper_Header";

	public static boolean reportGenerator(Map paramMap) {
			JasperPrint jasperPrint = returnReportPrint( paramMap );
			//paramMap.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);
			JasperPrint jasperPrintPDF = returnReportPrint( paramMap );
			JRAbstractExporter jrExporter = null;
			try {
				
				if( "true".equals( paramMap.get( JRM_OUT_HTML))){
					jrExporter = new JRHtmlExporter();
					jrExporter.setParameter( JRExporterParameter.JASPER_PRINT, jasperPrint );
					jrExporter.setParameter( JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.FALSE );
					//if( "true".equals( paramMap.get( JRM_IS_HEADER ))){
					//	jrExporter.setParameter( JRHtmlExporterParameter.HTML_HEADER, paramMap.get( JRM_HEADER_VALUE ));
					//}
					if( paramMap.get( JRM_HTML_END_PAGE_INDEX ) != null ){
						jrExporter.setParameter( JRExporterParameter.END_PAGE_INDEX, paramMap.get( JRM_HTML_END_PAGE_INDEX ));	
					}
					jrExporter.setParameter( JRExporterParameter.OUTPUT_FILE, paramMap.get( JRM_OUT_HTML_FILE ));
					jrExporter.setParameter( JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE );
					jrExporter.exportReport();
					jrExporter = null;
				} 
				if( "true".equals( paramMap.get( JRM_OUT_PDF ))){
					jrExporter = new JRPdfExporter();
					jrExporter.setParameter( JRExporterParameter.JASPER_PRINT, jasperPrintPDF );
					jrExporter.setParameter( JRExporterParameter.OUTPUT_FILE, paramMap.get( JRM_OUT_PDF_FILE ));
					jrExporter.exportReport();
					jrExporter = null;
				} 
			} catch ( Exception e ) {
				e.printStackTrace();
				return false;
			}finally{
				//	root.setLevel( level );
				//jasperPrint = null;
			}
			return true;
		}
	private static JasperPrint returnReportPrint( Map paramMap ) {
		JasperPrint jasperPrint = null;
		String xmlPath = ( String )paramMap.get( JRM_JRXML_PATH );
		InputStream isjrXML = null;
		JasperReport jasperReport = null;
		JasperDesign jasperDesign = null;
		try{
			isjrXML = new FileInputStream( xmlPath );
			jasperDesign = JRXmlLoader.load( isjrXML );
			if( "true".equals( paramMap.get(JRM_IS_BEAN_COLLECTION ))){
				jasperReport = JasperCompileManager.compileReport( jasperDesign );
				JRBeanCollectionDataSource jrBeanDS = new JRBeanCollectionDataSource( (List) paramMap.get( JRM_BEAN_COLLECTION ));
				jasperPrint = JasperFillManager.fillReport( jasperReport, paramMap, jrBeanDS );
			}
		}catch( Exception ex ) {
			String connectMsg = "Could not create the report stream " + ex.getMessage() + " " + ex.getLocalizedMessage();
			System.out.println( connectMsg );
		}finally{
			try {
				if( isjrXML != null )
					isjrXML.close();
			} catch ( Exception e ) {
				System.out.println( "Error when close jrxml stream "+ e );
			}
		}
		return jasperPrint;
	}
	
	static String getHTMLJavaScript( boolean isPrint, boolean isPDF, String pdfPath ,boolean isXLS, String xlsPath,boolean isCSV,String csvPath ){
		
		String html = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8' /><script> ";
		String htmlFooter = "</script></head>";
		String sJScript = "var urlPDF = '" + pdfPath + "?radom=' + Math.random();"+
		"var urlXLS = '" + xlsPath + "?radom=' + Math.random();"+
		"var urlCSV = '" + csvPath + "?radom=' + Math.random();"+
		"if( "+ isPrint +" ){"+
			"window.print();" +
		"}"+
		"if( "+ isPDF + " ){"+
			"window.open( urlPDF, 'mywindow','menubar=0,resizable=1,width=500,height=250' );" +
		"}"+
		"if( "+ isXLS + " ){"+
			"window.open( urlXLS, 'mywindow','' );" +
		"}"+
		"if( "+ isCSV + " ){"+
		"window.open( urlCSV, 'mywindow','' );" +
		"}";
		return html + sJScript + htmlFooter;
		
	}
}
