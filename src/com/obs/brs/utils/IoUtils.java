package com.obs.brs.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;

import org.apache.log4j.Logger;

import com.obs.brs.session.manager.FacesUtils;

public class IoUtils {
	private static final int DEFAULT_BUFFER_SIZE = 1024;

	private int bufferSize = 1024;

	//Logger Instantiated for Log Management
	private static final Logger log					= Logger.getLogger(IoUtils.class);

	public IoUtils() {
		this(DEFAULT_BUFFER_SIZE);
	}

	public IoUtils(int bufferSize) { 
		this.bufferSize = bufferSize;
	}

	public String read(String filePath) throws IOException {
		FileInputStream       inputStream   = new FileInputStream(new File(filePath));
		ByteArrayOutputStream outputStream  = new ByteArrayOutputStream();
		copy(inputStream, outputStream);
		return outputStream.toString();
	}

	public int copy(InputStream inputStream, OutputStream outputStream) throws IOException {
		byte[] buffer = new byte[bufferSize];
		int totalBytesRead = 0;
		int bytesRead      = 0;
		while (-1 != (bytesRead = inputStream.read(buffer))) {
			outputStream.write(buffer, 0, bytesRead);
			totalBytesRead += bytesRead;
		}
		return totalBytesRead;
	}  

	public static String extractBytes (String ImagePath) throws IOException {

		String encodedImage = null;
		byte[] imageInByte= extractBytesWithoutEncode(ImagePath);
		if(imageInByte!=null){
			//encodedImage = new BASE64Encoder().encode(imageInByte);
		}
		return encodedImage;
	}

	public static byte[] extractBytesWithoutEncode (String ImagePath) throws IOException {
		byte[] imageInByte =  null;
		File fnew=new File(ImagePath);
		if(fnew.isFile()){
			BufferedImage originalImage=ImageIO.read(fnew);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			ImageIO.write(originalImage, getExtection(ImagePath), baos );
			imageInByte=baos.toByteArray();
		}
		return imageInByte;
	}

	public static void deleteFolderImages(String path){
		if(new File(path).exists()){
			File[] files = new File(path).listFiles();
			if(files!=null) {
				for(File file : files)
				{
					file.delete ();
				}
			}	
		}

	}

	public static String getExtection(String fileName){
		int pos = fileName.lastIndexOf('.');
		String ext = fileName.substring(pos+1);
		return ext;
	}

	// SK: Deletes all files and sub directories under dir.
	// Returns true if all deletions were successful.
	// If a deletion fails, the method stops attempting to delete and returns false.
	public static boolean deleteDir(File dir) {
		boolean isDelete = true;
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				File file = new File(dir.getPath()+"/"+children[i]);
				file.delete();
			}
		}
		return isDelete;
	}

	//string reader
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	//read json from url
	public static String readJsonFromUrl(String url) throws Exception {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			return jsonText;
		} finally {
			is.close();
		}
	}

	// SK: Write Images
	public static boolean writeImages(String imagePath,String imageName, InputStream data,Boolean logoFlag)
	{
		boolean flag = false;
		byte[] mediaData;
		BufferedImage image = null;
		ByteArrayInputStream imageStream = null;
		try {
			mediaData = new byte[data.available()];
			// Write File //
			File photoFile = new File(imagePath);
			if(!photoFile.exists()) // Path Exists
				photoFile.mkdirs();
			data.read(mediaData);
			imageStream = new ByteArrayInputStream(mediaData);
			image = ImageIO.read(imageStream);
			if(logoFlag==true){
				flag = ImageIO.write(image, "png", new File(photoFile+"/"+imageName));
			}else{
				flag = ImageIO.write(image, "jpg", new File(photoFile+"/"+imageName));
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error While writeImages:"+e);
		}
		finally
		{
			if(imageStream != null & image != null)
			{
				try {
					imageStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					log.error("Error While writeImages:"+e);
				}
				image.flush();
			}
		}
		return flag;
	}

	// SK: Write Images
	public static boolean copyImages(String sourcePath,String targetPath,String targetName)
	{
		boolean flag = false;
	BufferedImage image = null;
		try {
			String[] split = sourcePath.split("\\.");
			String ext = split[split.length - 1];
			
			image = ImageIO.read(new File(sourcePath));

			flag = ImageIO.write(image, ext, new File(targetPath+"/"+targetName));
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error While writeImages:"+e);
		}
		finally
		{
			if(image != null)
			{
				image.flush();
			}
		}
		return flag;
	}


	// SK: Copy Folder and it's Files Src to Destination
	public static void copyFolder(File src, File dest) throws IOException{
		if(src.isDirectory()){

			//if directory not exists, create it
			if(!dest.exists()){
				dest.mkdir();
				log.info("Directory copied from "
						+ src + "  to " + dest);
			}

			//list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				//construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				//recursive copy
				copyFolder(srcFile,destFile);
			}

		}else{
			//if file, then copy it
			//Use bytes stream to support all file types
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			//copy the file content in bytes
			while ((length = in.read(buffer)) > 0){
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
			log.info("File copied from " + src + " to " + dest);
		}
	}

	// SK: Delete directory and the files under
	public static void deleteDirectory(File path){
		if (path == null)
			return;
		if (path.exists())
		{
			for(File f : path.listFiles())
			{
				if(f.isDirectory()){
					deleteDirectory(f);
					f.delete();
				}
				else{
					f.delete();
				}
			}
			path.delete();
		}
	}

	/**
	 * download document from given path
	 * @param path
	 * @param filename
	 */
	public static void documentDownload(String path, String filename){
		String extension = null;
		try {
			extension = getExtection(filename);
			FacesUtils facesUtils = new FacesUtils();
			HttpServletResponse response = facesUtils.getResponse();
			String type = getContentType(extension);
			response.setContentType(type);
			response.setHeader("Content-disposition", "attachment; filename=" + filename);
			OutputStream out = response.getOutputStream();
			FileInputStream in = new FileInputStream(path+filename);
			byte[] buffer = new byte[4096];
			int length;
			while ((length = in.read(buffer)) > 0){
				out.write(buffer, 0, length);
			}
			in.close();
			out.flush();
			facesUtils.getContext().responseComplete();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	/**
	 * return content type of file
	 * @param extension
	 * @return
	 */	
	private static String getContentType(String extension)
	{
		if(extension.equals("pdf"))
		{
			return "application/pdf";
		}
		if(extension.equals("xls") || extension.equals("xlsx"))
		{
			return"application/vnd.ms-excel";
		}
		if(extension.equals("doc") || extension.equals("docx"))
		{
			return"application/msword";
		}
		if(extension.equals("gif"))
		{
			return "image/gif";
		}
		if(extension.equals("png"))
		{
			return "image/png";
		}
		if(extension.equals("jpg") || extension.equals("jpeg"))
		{
			return "image/jpeg";
		}
		if(extension.equals("tif") || extension.equals("tiff"))
		{
			return "image/tiff";
		}
		return "";
	}
}
