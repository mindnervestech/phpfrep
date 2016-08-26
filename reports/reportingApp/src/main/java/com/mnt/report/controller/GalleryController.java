package com.mnt.report.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.tartarus.snowball.ext.LovinsStemmer;

import sun.misc.BASE64Decoder;
import sun.util.locale.StringTokenIterator;

import com.google.gson.Gson;
import com.mnt.report.util.CommonUtils;
import com.mysql.jdbc.PreparedStatement;





@Controller
@RequestMapping("/gallery")
public class GalleryController {
	static { ImageIO.scanForPlugins(); }	
	DecimalFormat decimalFormat=new DecimalFormat("#.##");
	
	@Autowired
    private JdbcTemplate jt;
	
	@Autowired
	private CommonUtils util;

	@Value("${fullImagePath}")
	String fullImagePath;
	
   

    

	
	@RequestMapping(value="/uploadImages",method=RequestMethod.POST)
	@ResponseBody
	public void getAllParentImageImg(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, java.io.IOException {
		
		
		
		/*MultipartFormData body = request().body().asMultipartFormData();
		*/
		boolean isMultipart;
		isMultipart = ServletFileUpload.isMultipartContent(request);
		response.setContentType("text/html");
		java.io.PrintWriter out = response.getWriter( );
		if( !isMultipart ){
			
			return;
		}
		
	
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		
		factory.setRepository(new File("D:/temp"));
		
		ServletFileUpload upload = new ServletFileUpload(factory);
		String filePath;
		
		try{ 
			String userId="3";
			/*filePath = CommonProperties.getBasePath()+CommonProperties.getImageContextPath()+CommonProperties.getParentImageTempPath();
			*/
			
			filePath = "D:/temp";
			
			File file = new File(filePath);
			if(!file.exists()){				
				file.mkdirs();
			}
		
			// Parse the request to get file items.
			List fileItems = upload.parseRequest(request);
			
		
			// Process the uploaded file items
			Iterator i = fileItems.iterator();
			
			while ( i.hasNext () ) 
			{
				
			
				FileItem fi = (FileItem)i.next();
				if ( !fi.isFormField () )	
				{
					
					String  extension="";
					// Get the uploaded file parameters
					String fieldName = fi.getFieldName();
					String fileName = fi.getName();
					String contentType = fi.getContentType();
					boolean isInMemory = fi.isInMemory();
					long sizeInBytes = fi.getSize();
					String[] split = fileName.split("\\.");
					extension = split[split.length - 1];
					String fileString = split[split.length - 2];
					fileString=fileString.replaceAll(" ", "_");
					fileName=fileString+"."+extension;
					// Write the file
					if( fileName.lastIndexOf("\\") >= 0 ){
						file = new File( filePath + "/"+
								fileName.substring( fileName.lastIndexOf("\\"))) ;
					}else{
						file = new File( filePath +  "/"+
								fileName.substring(fileName.lastIndexOf("\\")+1)) ;
					}
					fi.write( file ) ;
					
				}
				
				}
			out.println("</body>");
			out.println("</html>");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
		
		
		
		
	}
	
	
	@RequestMapping(value="/all_publication_list", method=RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> allPublicationList(){
	
		
	
		
		List<Map<String,Object>> allPublication=new ArrayList<Map<String,Object>>();
		String publicationsql="select p.DC_PUBLICATION_TITLE from tbl_publication p where p.DC_PUBLICATION_TYPE='2'";
		
		allPublication=jt.queryForList(publicationsql);
		
		return allPublication;
		
	}
	
	@RequestMapping(value="/create_thumnail_images", method=RequestMethod.GET)
	@ResponseBody
	public void createThumbanail(){
		
	
		
		String query = "select DN_ID,DC_IMAGENAME,DN_PARENT_IMAGE_ID from tbl_child_image";
		List<Map<String,Object>> results =  jt.queryForList(query);
		
		int childCount=0;
		
		int childCountOrg=0;
		if(results.size()>0){
			for (Map<String, Object> map : results) {
				
				String fileName=map.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
				
			
				
				File thumbFile = new File(fullImagePath+"/"+"child"+"/"+map.get("DN_PARENT_IMAGE_ID").toString()+"/"+map.get("DN_ID").toString()+"/"+fileName);
				File thumbFileOrg = new File(fullImagePath+"/"+"child"+"/"+map.get("DN_PARENT_IMAGE_ID").toString()+"/"+map.get("DN_ID").toString()+"/"+map.get("DC_IMAGENAME").toString());
				
				
				 if (thumbFileOrg.exists()) {
					 childCountOrg++;
			        if (!thumbFile.exists()) {
			        	//thumbFile.mkdirs();
			        	childCount++;
			        	try {	
							Thumbnails.of(new File(fullImagePath+"/"+"child"+"/"+map.get("DN_PARENT_IMAGE_ID").toString()+"/"+map.get("DN_ID").toString()+"/"+map.get("DC_IMAGENAME")))
				        	.width(200).keepAspectRatio(true)
				        	.outputFormat("jpg")
				        	.toFile(fullImagePath+"/"+"child"+"/"+map.get("DN_PARENT_IMAGE_ID").toString()+"/"+map.get("DN_ID").toString()+"/"+map.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg");
						
							
			        	} catch (IOException e1) {
							
							e1.printStackTrace();
							
						}

			        	
			        }
				 }
		    
			}

		}
			
		
	}
	
	
	
	
	@RequestMapping(value="/create_thumnail_images_parent", method=RequestMethod.GET)
	@ResponseBody
	public void createThumbanailParent(){
		
		
		
		String query = "select DN_ID,DC_IMAGENAME from tbl_parent_image";
		List<Map<String,Object>> results =  jt.queryForList(query);
		
		int childCount=0;
		
		int childCountOrg=0;
		if(results.size()>0){
			for (Map<String, Object> map : results) {
				
				String fileName=map.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
				
			
				
				File thumbFile = new File(fullImagePath+"/"+"parent"+"/"+map.get("DN_ID").toString()+"/"+fileName);
				File thumbFileOrg = new File(fullImagePath+"/"+"parent"+"/"+map.get("DN_ID").toString()+"/"+map.get("DC_IMAGENAME").toString());
				
				
				 if (thumbFileOrg.exists()) {
					 childCountOrg++;
			        if (!thumbFile.exists()) {
			        	//thumbFile.mkdirs();
			        	childCount++;
			        	try {	
							Thumbnails.of(new File(fullImagePath+"/"+"parent"+"/"+map.get("DN_ID").toString()+"/"+map.get("DC_IMAGENAME").toString()))
				        	.width(200).keepAspectRatio(true)
				        	.outputFormat("jpg")
				        	.toFile(fullImagePath+"/"+"parent"+"/"+map.get("DN_ID").toString()+"/"+fileName);
						
							
			        	} catch (IOException e1) {
							
							e1.printStackTrace();
							
						}

			        	
			        }
				 }
		    
			}

		}
			
	
	}
	
	
/*	@RequestMapping(value="/create_thumnail_images", method=RequestMethod.GET)
	@ResponseBody
	public void createThumbanail(){
		
		System.out.println("in create thumb images");
		System.out.println("Method executed at every 1 hour. Current time is :: "+ new Date());
		
		String query = "select DN_ID,DC_IMAGENAME,DN_PARENT_IMAGE_ID from tbl_child_image";
	//	String sqllike="select DN_ID,DC_IMAGENAME,DN_PARENT_IMAGE_ID from tbl_child_image where DC_IMAGENAME LIKE '%png' ";
		List<Map<String,Object>> results =  jt.queryForList(query);
	//	System.out.println("size o flist is "+results.size());
		if(results.size()>0){
			for (Map<String, Object> map : results) {
				
				File thumbFile = new File(fullImagePath+"/"+"child"+"/"+map.get("DN_PARENT_IMAGE_ID").toString()+"/"+map.get("DN_ID").toString()+"/"+map.get("DC_IMAGENAME"));
		    //	System.out.println("thumbFile "+thumbFile.getAbsolutePath());
		    	try {	
					Thumbnails.of(new File(fullImagePath+"/"+"child"+"/"+map.get("DN_PARENT_IMAGE_ID").toString()+"/"+map.get("DN_ID").toString()+"/"+map.get("DC_IMAGENAME")))
		        	.width(200).keepAspectRatio(true)
		        	.outputFormat(map.get("DC_IMAGENAME").toString().split("\\.")[1])
		        	.toFile(fullImagePath+"/"+"child"+"/"+map.get("DN_PARENT_IMAGE_ID").toString()+"/"+map.get("DN_ID").toString()+"/"+map.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb."+map.get("DC_IMAGENAME").toString().split("\\.")[1]);
				} catch (IOException e1) {
					System.out.println("problame to create thumnail");
					e1.printStackTrace();
					
				}

			}

		}


	}*/
	
	@RequestMapping(value="/parentlist", method=RequestMethod.GET)
	@ResponseBody
	public List getGalleryParentList(){
		String query = "select * from tbl_parent_image";
		List<Map<String,Object>> results =  jt.queryForList(query);
		return results;
	}
	
	@RequestMapping(value="/duplicateImageList", method=RequestMethod.GET)
	@ResponseBody
	public List getDuplicateImageList(){
		
		List<Map<String,Object>> parentImageListfornewSci = new ArrayList<Map<String,Object>>();
	    List<String> duplicateNames= new ArrayList<String>();
	    
		Set<String> duplicateNamess = new HashSet<String>();
		
		List<ImagesVM> duplicateImageVm= new ArrayList<ImagesVM>();
		int count=0;
			String sql="select * from tbl_parent_image m where m.DN_STATUS=0 ORDER BY m.DN_ID ";
			parentImageListfornewSci=jt.queryForList(sql);
			for(Map<String, Object> pImage : parentImageListfornewSci){
				if(!duplicateNamess.add(pImage.get("DC_IMAGENAME").toString())) {
					if(!duplicateNames.contains(pImage.get("DC_IMAGENAME").toString())){
						ImagesVM imagesVM= new ImagesVM();
						
						imagesVM.setDuplicate(true);
						
						if(pImage.get("DN_ID")!=null){
							imagesVM.setDN_ID(Long.valueOf(pImage.get("DN_ID").toString()));
						}
						if(pImage.get("DC_IMAGENAME")!=null){
							imagesVM.setDC_IMAGENAME(pImage.get("DC_IMAGENAME").toString());
						}
						if(pImage.get("DN_STATUS")!=null){
							imagesVM.setDN_STATUS(Integer.parseInt(pImage.get("DN_STATUS").toString()));
						}
						
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
						Object d=pImage.get("DD_ISSUE_DATE");
						if(d!=null){
							String dateInString =d.toString();
							try {
								Date date = formatter.parse(dateInString);
								imagesVM.setDD_ISSUE_DATE(date);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						if(pImage.get("DC_SECTION")!=null){
							imagesVM.setDC_SECTION(pImage.get("DC_SECTION").toString());
						}
						if(pImage.get("DC_SECTION_OTHER")!=null){
							imagesVM.setDC_SECTION_OTHER(pImage.get("DC_SECTION_OTHER").toString());
						}
						
						if(pImage.get("DC_SECTION_SPECIAL_REGIONAL")!=null){
							imagesVM.setDC_SECTION_SPECIAL_REGIONAL(pImage.get("DC_SECTION_SPECIAL_REGIONAL").toString());
						}
						if(pImage.get("DC_SECTION_SPECIAL_TOPIC")!=null){
							imagesVM.setDC_SECTION_SPECIAL_TOPIC(pImage.get("DC_SECTION_SPECIAL_TOPIC").toString());
						}
						if(pImage.get("DC_HEIGHT")!=null){
							imagesVM.setDC_HEIGHT(pImage.get("DC_HEIGHT").toString());
						}
						if(pImage.get("DC_WIDTH")!=null){
							imagesVM.setDC_WIDTH(pImage.get("DC_WIDTH").toString());
						}
						
						String id=pImage.get("DN_ID").toString();
						List<Map<String,Object>> childImageList = new ArrayList<Map<String,Object>>();
						String sql1="select m.DC_IMAGENAME,m.DN_ID from tbl_child_image m where m.DN_PARENT_IMAGE_ID="+id;
						childImageList=jt.queryForList(sql1);
						List<ChildImageVm> arrayList= new ArrayList<ChildImageVm>();
						for (Map<String, Object> map : childImageList) {
							ChildImageVm childVm= new ChildImageVm();
							
							if(map.get("DN_ID")!=null){
								childVm.setDN_ID(Long.valueOf(map.get("DN_ID").toString()));
							}
							if(map.get("DC_IMAGENAME")!=null){
								childVm.setDC_IMAGENAME(map.get("DC_IMAGENAME").toString());
							}
							
						    arrayList.add(childVm);
						}
						imagesVM.getListVm().addAll(arrayList);
						
						duplicateImageVm.add(imagesVM);
						
					   //duplicateNames.add(pImage.get("DC_IMAGENAME").toString());
				    }
				}
			}
		Collections.reverse(duplicateImageVm);	
		return duplicateImageVm;
	}
	
	
	@RequestMapping(value="/set_jobId_status", method=RequestMethod.GET)
	@ResponseBody
	public void setJobIdStatus(){
		

		
		String sql = "select d.DN_ID, d.DN_PARENT_IMAGE_ID from tbl_de_job d";
		List<Map<String,Object>> resultsList =  jt.queryForList(sql);
		
		int statusOne=0;
		int starusZero=0;
		for (Map<String, Object> map : resultsList) {
			
			String 	sqlForJobId = "select * from tbl_de_data d where d.DB_DELETED=0 and d.DE_JOB_ID='"+map.get("DN_ID").toString()+"'";
			List<Map<String,Object>> deDataList =  jt.queryForList(sqlForJobId);
			
			if(deDataList.size()!=0){
			
				long compleated = 0;
				for(Map<String, Object> data : deDataList){
					
					String temp=String.valueOf(data.get("DN_DECOMPANY_ID"));
					
					if(temp!=null){
						compleated++;
					}
					
				}
				
				if(compleated==deDataList.size()){
					String sqlUpdat="Update tbl_de_job set DN_STATUS=1 Where DN_ID="+map.get("DN_ID").toString();
					jt.execute(sqlUpdat);
					
					statusOne++;
					
				}else{
					/*String sqlUpdatStatusZere="Update tbl_de_job set DN_STATUS=0 Where DN_ID="+map.get("DN_ID").toString();
					jt.execute(sqlUpdatStatusZere);
					starusZero++;
					*/
					
					
					
				}
				
			}
			else {
				
				try {
					if(map.get("DN_PARENT_IMAGE_ID") != null ) {
						String sqlUpdatStatusZere="Update tbl_parent_image set DN_STATUS=0 Where DN_ID="+map.get("DN_PARENT_IMAGE_ID").toString();
						jt.execute(sqlUpdatStatusZere);
					}
					
					String	sqlUpdatStatus="Update tbl_de_job set DN_STATUS=0 Where DN_ID="+map.get("DN_ID").toString();
					jt.execute(sqlUpdatStatus);
					starusZero++;
				} catch (Exception e) {
					e.printStackTrace();
					
				}
				
			}
			
		}
		
	
	}
	
	
	@RequestMapping(value="/get_publication_title", method=RequestMethod.GET)
	@ResponseBody
	public List getPublicatation(){
	
		
		String sql = "select * from tbl_publication where DB_DELETED=0 and DC_PUBLICATION_TYPE=2 ORDER BY DC_PUBLICATION_TITLE ASC";
		List<Map<String,Object>> results =  jt.queryForList(sql);
		
//date chackbox
	
		
		return results;
	}
	
	
	
	
	@RequestMapping(value="/edit_image_detail/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Map getEditImageDetail(@PathVariable("id") String id){
		
		Long imageId=Long.parseLong(id);
		String sql="select * from tbl_parent_image where DN_ID="+id;
		Map<String,Object> m=jt.queryForMap(sql);
		
		String titleName=null;
		if(m.get("DC_PUBLICATION_TITLE")!=null){
			String title=m.get("DC_PUBLICATION_TITLE").toString();
			
			String sql2="select DC_PUBLICATION_TITLE from tbl_publication where DN_ID="+title;
			titleName=jt.queryForObject(sql2, String.class);
		}
		String sectionName=null;
		if(m.get("DC_SECTION")!=null){
			 Long sectionId=Long.parseLong(m.get("DC_SECTION").toString());
			 String sql3="select DC_PUBLICATION_TITLE from tbl_publication where DN_ID="+sectionId;
			 sectionName=jt.queryForObject(sql3, String.class);
		}
		
		m.put("titleName", titleName);
		m.put("section",sectionName);
		return m;
		
	}
	
	@RequestMapping(value="/searchComment/{comment}", method=RequestMethod.GET)
	@ResponseBody
	public List<CommentVm> getSearchComment(@PathVariable("comment") String comment){
		
		
		String sql="select Distinct(t.DC_SECTION_SPECIAL_REGIONAL) as com from tbl_parent_image t where t.DC_SECTION_SPECIAL_REGIONAL like '%"+comment+"%'"+
					" union "+
                    " select DISTINCT(t.DC_SECTION_OTHER) as com from tbl_parent_image t where t.DC_SECTION_OTHER like '%"+comment+"%'"+
                    " union "+
                    "select DISTINCT(t.DC_SECTION_SPECIAL_TOPIC) as com from tbl_parent_image t where t.DC_SECTION_SPECIAL_TOPIC like '%"+comment+"%'";
		
		List<Map<String,Object>> results =  jt.queryForList(sql);
		List<CommentVm> cVm= new ArrayList<CommentVm>();
		if(results.size()>0){
			for (Map<String, Object> map : results) {
				CommentVm cm= new CommentVm();
				cm.comment=map.get("com").toString();
				cVm.add(cm);
			}
		}
		return cVm;
		
	}
	

	
	
	@RequestMapping(value="/save_comment", method=RequestMethod.POST)
	@ResponseBody
	public int save_comment(@RequestBody ParentImageVm idVm){
		
		Long imageId=Long.parseLong(idVm.getId().toString());
		
		String section=null;
		String comment=idVm.getTitle();
		
		
		String sql="select t.DC_SECTION from tbl_parent_image t where t.DN_ID="+imageId;
		section=jt.queryForObject(sql, String.class);
		
		Long sectionId=Long.parseLong(section);
		
		String sql1="select t.DC_PUBLICATION_TITLE from tbl_publication t where t.DN_ID="+sectionId;
		String publicationTitle=jt.queryForObject(sql1, String.class);
	
		
		int k=0;
		
		if(publicationTitle.equals("Special-Topic")){
			String sql2="update tbl_parent_image t set t.DC_SECTION_SPECIAL_TOPIC='"+comment+"' where t.DN_ID="+imageId;
			jt.execute(sql2);
			k=1;
		}else if(publicationTitle.equals("Special-Regional")) {
			String sql3="update tbl_parent_image t set t.DC_SECTION_SPECIAL_REGIONAL='"+comment+"' where t.DN_ID="+imageId;
			jt.execute(sql3);
			k=2;
		} else {
			String sql4="update tbl_parent_image t set t.DC_SECTION_OTHER='"+comment+"' where t.DN_ID="+imageId;
			jt.execute(sql4);
			k=3;
		}
		
		
		return k;
				
	}

	@RequestMapping(value="/get_publication_sector", method=RequestMethod.GET)
	@ResponseBody
	public List getPublicatationSector(){
	

		String SQL = "select * from tbl_publication where DB_DELETED=0 and DC_PUBLICATION_TYPE=3 ORDER BY DC_PUBLICATION_TITLE ASC";
		List<Map<String,Object>> results =  jt.queryForList(SQL);
		return results;
	}
	
	
	@RequestMapping(value="/refresh_crop_image/{id}", method=RequestMethod.GET)
	@ResponseBody
	public ImagesVM refreshCropImage(@PathVariable("id") String id){
		
		/*Map<String,Object> childImageList = new HashMap<String, Object>();
		String sql="select m.DC_IMAGENAME from tbl_child_image m where m.DN_PARENT_IMAGE_ID="+id;
		childImageList=jt.queryForMap(sql);
		*/
		String imageIdforCrop=id;
		ImagesVM li= new ImagesVM();
		String url="/webapp/get-all-parent-image?id=";
		
		int n=0;
		int m=0;
		
		Map<String,Object> pImage = new HashMap<String, Object>();
		String sql="select m.DN_ID,m.DC_IMAGENAME,m.DD_CREATED_ON,m.DN_CREATED_BY,u.DC_FIRSTNAME,m.DN_DELETED_BY,m.DD_DELETED_ON,m.DB_DELETED,m.DD_ISSUE_DATE,m.DC_PAGE,m.DC_PUBLICATION_TITLE,m.DC_SECTION,m.DC_SECTION_OTHER,m.DC_SECTION_SPECIAL_REGIONAL,m.DC_SECTION_SPECIAL_TOPIC,m.DC_HEIGHT,m.DC_WIDTH from tbl_parent_image m inner join tbl_user u on m.DN_CREATED_BY=u.DN_ID and m.DN_ID='"+imageIdforCrop+"' limit 1";
		pImage=jt.queryForMap(sql);
		
		List<String> duplicate= new ArrayList<String>();
		Set<String> duplicateNamess = new HashSet<String>();
		
			ImagesVM imagesVM= new ImagesVM();
			
			if(pImage.get("DN_ID")!=null){
			
				imagesVM.setDN_ID(Long.valueOf(pImage.get("DN_ID").toString()));
			}
			if(pImage.get("DC_IMAGENAME")!=null){
					
				if(!duplicateNamess.add(pImage.get("DC_IMAGENAME").toString())) {
				
					imagesVM.setDuplicate(true);
					n++;
				}else{
					imagesVM.setDuplicate(false);
					m++;
				}	
				imagesVM.setDC_IMAGENAME(pImage.get("DC_IMAGENAME").toString());
				
				String thumImageName=pImage.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
				imagesVM.setThumb(thumImageName);
			   
			}
			if(pImage.get("DN_STATUS")!=null){
				imagesVM.setDN_STATUS(Integer.parseInt(pImage.get("DN_STATUS").toString()));
			}
			
			String publicationTitle=null;
			if(pImage.get("DC_PUBLICATION_TITLE")!=null){
				String sql11="select t.DC_PUBLICATION_TITLE from tbl_publication t where t.DN_ID="+pImage.get("DC_PUBLICATION_TITLE").toString();
				publicationTitle=jt.queryForObject(sql11, String.class);
				imagesVM.setDC_PUBLICATION_TITLE(publicationTitle);
				
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Object d=pImage.get("DD_ISSUE_DATE");
			if(d!=null){
				String dateInString1 =d.toString();
				try {
					Date date1 = formatter.parse(dateInString1);
					imagesVM.setDD_ISSUE_DATE(date1);
					imagesVM.setDateissue(formatter.format(date1));
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			Object d2=pImage.get("DD_CREATED_ON");
			if(d2!=null){
				String dateInString2 =d2.toString();
				try {
					Date date2 = formatter.parse(dateInString2);
					imagesVM.setDD_CREATED_ON(date2);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			
			if(pImage.get("DC_FIRSTNAME")!=null){
				imagesVM.setCREATED_BY(pImage.get("DC_FIRSTNAME").toString());
			}
			
			String section=null;
			if(pImage.get("DC_SECTION")!=null){
				String sql22="select t.DC_PUBLICATION_TITLE from tbl_publication t where t.DN_ID="+pImage.get("DC_SECTION").toString();
				section=jt.queryForObject(sql22, String.class);
				imagesVM.setDC_SECTION(section);
			}
			if(pImage.get("DC_SECTION_OTHER")!=null){
				imagesVM.setDC_SECTION_OTHER(pImage.get("DC_SECTION_OTHER").toString());
			}
			
			if(pImage.get("DC_SECTION_SPECIAL_REGIONAL")!=null){
				imagesVM.setDC_SECTION_SPECIAL_REGIONAL(pImage.get("DC_SECTION_SPECIAL_REGIONAL").toString());
			}
			if(pImage.get("DC_SECTION_SPECIAL_TOPIC")!=null){
				imagesVM.setDC_SECTION_SPECIAL_TOPIC(pImage.get("DC_SECTION_SPECIAL_TOPIC").toString());
			}
			if(pImage.get("DC_HEIGHT")!=null){
				imagesVM.setDC_HEIGHT(pImage.get("DC_HEIGHT").toString());
			}
			if(pImage.get("DC_WIDTH")!=null){
				imagesVM.setDC_WIDTH(pImage.get("DC_WIDTH").toString());
			}
			if(pImage.get("DC_PAGE")!=null){
				imagesVM.setDC_PAGE(pImage.get("DC_PAGE").toString());
			}
			
			imagesVM.setImageUrl(url+Long.parseLong(pImage.get("DN_ID").toString()));
			
			String idChild=pImage.get("DN_ID").toString();
			List<Map<String,Object>> childImageList = new ArrayList<Map<String,Object>>();
			String sql1="select m.DC_IMAGENAME,m.DN_ID from tbl_child_image m where m.DN_PARENT_IMAGE_ID="+idChild;
			childImageList=jt.queryForList(sql1);
			List<ChildImageVm> arrayList= new ArrayList<ChildImageVm>();
			for (Map<String, Object> map : childImageList) {
				ChildImageVm childVm= new ChildImageVm();
				
				if(map.get("DN_ID")!=null){
					childVm.setDN_ID(Long.valueOf(map.get("DN_ID").toString()));
				}
				if(map.get("DC_IMAGENAME")!=null){
				
					childVm.setDC_IMAGENAME(map.get("DC_IMAGENAME").toString());
					String thumChildImage=map.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
					childVm.setChildThumb(thumChildImage);
				   
				}
				
			    arrayList.add(childVm);
			}
			imagesVM.getListVm().addAll(arrayList);
		
		return imagesVM;
	}
	
	
	@RequestMapping(value="/get_child_images/{id}", method=RequestMethod.GET)
	@ResponseBody
	public List getChildImages(@PathVariable("id") String id){
		
		List<Map<String,Object>> childImageList = new ArrayList<Map<String,Object>>();
		String sql="select m.DC_IMAGENAME from tbl_child_image m where m.DN_PARENT_IMAGE_ID="+id;
		childImageList=jt.queryForList(sql);
		return childImageList;
	}	
	
	@RequestMapping(value="/save_crop_image", method=RequestMethod.POST)
	@ResponseBody
	public CropImageVm saveCropImage(@RequestBody CropImageVm cropImageVm) throws IOException{
	
		
		
		Double x1,x2,y1,y2,w,h;
		
		x1=Double.parseDouble(cropImageVm.getX1());
		x2=Double.parseDouble(cropImageVm.getX2());
		y1=Double.parseDouble(cropImageVm.getY1());
		y2=Double.parseDouble(cropImageVm.getY2());
		w=Double.parseDouble(cropImageVm.getW());
		h=Double.parseDouble(cropImageVm.getH());
	   
		int	x11=x1.intValue();
		int x22=x2.intValue();
		int y11=y1.intValue();
		int y22=y2.intValue();
		int w1=w.intValue();
		int h1=h.intValue();
		
		
		
		String sqlImagName="select DC_IMAGENAME from tbl_parent_image where DN_ID="+cropImageVm.getId();
		String imageName=jt.queryForObject(sqlImagName, String.class);
		  
		String sqlcreate="INSERT INTO tbl_child_image (DN_PARENT_IMAGE_ID,DN_CREATED_BY) VALUES('"+cropImageVm.getId()+"','"+cropImageVm.getLoginUserId()+"')";
		jt.execute(sqlcreate);
		
		final long childid=util.getMaxId(jt, "tbl_child_image");
		int r = (int) (Math.random() * (1000 - 100)) + 100;
		String fileimageName="crp_"+r+"_"+childid+".png";
		createDir(fullImagePath,cropImageVm.getId(),childid);
		File file = new File(fullImagePath+"/" + "parent" + "/"+cropImageVm.getId()+"/"+imageName);
		File thumbFile = new File(fullImagePath+"/"+"child"+"/"+cropImageVm.getId()+"/"+childid+"/"+fileimageName);
		BufferedImage originalImage = ImageIO.read(file);
		BufferedImage croppedImage = originalImage.getSubimage(x11, y11, w1,h1);
//		Thumbnails.of(croppedImage).size(w, h).toFile(file);
    	Thumbnails.of(croppedImage).size(w1, h1).toFile(thumbFile);
    	
    	
    	try {	
			Thumbnails.of(new File(fullImagePath+"/"+"child"+"/"+cropImageVm.getId()+"/"+childid+"/"+fileimageName))
        	.width(200).keepAspectRatio(true)
        	.outputFormat("jpg")
        	.toFile(fullImagePath+"/"+"child"+"/"+cropImageVm.getId()+"/"+childid+"/"+fileimageName.split("\\.")[0]+"_thumb.jpg");
		} catch (IOException e1) {
		
			e1.printStackTrace();
		}
    	
    	
    	final String heightCM=decimalFormat.format(((double)h1*2.54)/300);
		final String widthCM=decimalFormat.format(((double)w1*2.54)/300);	
		
    	
		
		
		File newChild = new File(fullImagePath+"/"+"child"+"/"+cropImageVm.getId()+"/"+childid+"/"+fileimageName); 
		ImageIO.write(croppedImage,"png",newChild );
		
		final String result = null; //doOCR(newChild);
		

		
    	String updateSql="update tbl_child_image set DC_IMAGENAME='"+fileimageName+"',DD_CREATED_ON=now(),DC_HEIGHT='"+heightCM+"',DC_WIDTH='"+widthCM+"' where DN_ID="+childid;
    	jt.execute(updateSql);

    	
    	String sqlforjobId="select t.DN_ID from tbl_de_job t where t.DN_PARENT_IMAGE_ID="+cropImageVm.getId()+" limit 1";
    	final String jobId=jt.queryForObject(sqlforjobId, String.class);
    	
    		
    	
    	File chilFile = new File(fullImagePath+"/"+"child"+"/"+cropImageVm.getId()+"/"+childid+"/"+fileimageName);
  
    	
    	/*String sqlforupdatededata="INSERT INTO tbl_de_data (DC_CURRENCY,DC_OCR_TEXT,DN_CHILD_IMAGE_ID,DN_CREATED_BY,DD_CREATED_ON,DN_PARENT_IMAGE_ID,DE_JOB_ID,DC_LENGTH,DC_WIDTH) VALUES('0','"+result+"','"+childid+"','"+cropImageVm.getLoginUserId()+"',now(),'"+cropImageVm.getId()+"','"+jobId+"','"+heightCM+"','"+widthCM+"')";
    	jt.execute(sqlforupdatededata);
    	*/
    	
    	
    	Calendar calendar = Calendar.getInstance();
	    final java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
    	String sqlforupdatededata="INSERT INTO tbl_de_data (DC_CURRENCY,DC_OCR_TEXT,DN_CHILD_IMAGE_ID,DN_CREATED_BY,DD_CREATED_ON,DN_PARENT_IMAGE_ID,DE_JOB_ID,DC_LENGTH,DC_WIDTH)  VALUES (?,?,?, ?, ?, ?, ?, ?, ?)";
    	
    	
    	
    	final Long idChild=cropImageVm.getLoginUserId();
    	final Long iddd=cropImageVm.getId();
    	
    	Boolean flag=jt.execute(sqlforupdatededata,new PreparedStatementCallback<Boolean>(){  
    
			@Override
			public Boolean doInPreparedStatement(java.sql.PreparedStatement ps)
					throws SQLException, DataAccessException {
				ps.setString (1, "0");
				ps.setString(2,result);
				ps.setLong (3, childid);
				ps.setLong(4, idChild);
				ps.setDate(5, startDate);
				ps.setLong(6, iddd);
				ps.setString(7, jobId);
				ps.setString(8, heightCM);
				ps.setString(9, widthCM);
	    	        return ps.execute(); 
			}  
    	    });
    	
    	
    	
    	
    	CropImageVm cropVm=new CropImageVm();

    	cropVm.setDN_ID(childid);
    	cropVm.setDC_IMAGENAME(fileimageName);
    	String thumbimagename=fileimageName.split("\\.")[0]+"_thumb.jpg";
    	cropVm.setChildThumb(thumbimagename);
    	
       return cropVm;
       
	}

	private String doOCR(File thumbFile) {
		  
		
				String result = null;
				try {
					if(thumbFile.exists()){
						Tesseract instance = Tesseract.getInstance();
						instance.setDatapath("/usr/local/share/tessdata");
						result = instance.doOCR(thumbFile);
					}
				} catch (TesseractException e) {
					e.printStackTrace();
					System.err.println(e.getMessage());
				}
				return result;
	}


	@RequestMapping(value="/save_whole_crop_image", method=RequestMethod.POST)
	@ResponseBody
	public CropImageVm SaveWholeCropImage(@RequestBody final CropImageVm cropImageVm) throws IOException, SQLException, ClassNotFoundException{
		
		final Long imageId=Long.parseLong(cropImageVm.getId().toString());

		String sqlImagName="select DC_IMAGENAME from tbl_parent_image where DN_ID="+imageId;
		String imageName=jt.queryForObject(sqlImagName, String.class);
		
		String parentImageThumb=imageName.split("\\.")[0]+"_thumb.jpg";
		
		String sqlcreate="INSERT INTO tbl_child_image (DN_PARENT_IMAGE_ID,DN_CREATED_BY) VALUES("+imageId+",'"+cropImageVm.getLoginUserId()+"')";
		jt.execute(sqlcreate);
		
		final long childid=util.getMaxId(jt, "tbl_child_image");
		
		int r = (int) (Math.random() * (1000 - 100)) + 100;
		String fileimageName="crp_"+r+"_"+childid+".png";
		String thumbimagename=fileimageName.split("\\.")[0]+"_thumb.jpg";
		
		createDir(fullImagePath,imageId,childid);
		File file = new File(fullImagePath+"/" + "parent" + "/"+imageId+"/"+imageName);
		File filethumb = new File(fullImagePath+"/" + "parent" + "/"+imageId+"/"+parentImageThumb);
		
		File thumbFile = new File(fullImagePath+"/"+"child"+"/"+imageId+"/"+childid+"/"+fileimageName);
		File thumbFileChild = new File(fullImagePath+"/"+"child"+"/"+imageId+"/"+childid+"/"+thumbimagename);
	
		BufferedImage originalImage = ImageIO.read(file);
		int height=originalImage.getHeight();
		int width=originalImage.getWidth();
		
//		BufferedImage croppedImage = originalImage.getSubimage(0, 0, width,height);

		Thumbnails.of(originalImage).size(width, height).toFile(thumbFile);
		
		try {
		    FileUtils.copyFile(filethumb, thumbFileChild);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	

		final String heightCM=decimalFormat.format(((double)height*2.54)/300);
		final String widthCM=decimalFormat.format(((double)width*2.54)/300);	
		
		
		
    	/*final String heightCM=decimalFormat.format(((double)height/308.89)*2.54*0.9575);
		final String widthCM=decimalFormat.format(((double)width/308.89)*2.54*0.9575);	*/
    	
//		File newthumbFile = new File(fullImagePath+"/"+"child"+"/"+imageId+"/"+childid+"/"+fileimageName);
//		ImageIO.write(originalImage,"png",newthumbFile );
		
		final String result = null; //doOCR(newthumbFile);
		
	//	final String result=null;
	
    	String updateSql="update tbl_child_image set DC_IMAGENAME='"+fileimageName+"',DD_CREATED_ON=now(),DC_HEIGHT='"+heightCM+"',DC_WIDTH='"+widthCM+"' where DN_ID="+childid;
		jt.execute(updateSql);
       

    	String sqlforjobId="select t.DN_ID from tbl_de_job t where t.DN_PARENT_IMAGE_ID="+imageId+" limit 1";
    	final String jobId=jt.queryForObject(sqlforjobId, String.class);
    	
    	
   // 	File childImage = new File(fullImagePath+"/"+"child"+"/"+imageId+"/"+childid+"/"+fileimageName);
    	
    	/*String sqlforupdatededata1="INSERT INTO tbl_de_data (DC_CURRENCY,DC_OCR_TEXT,DN_CHILD_IMAGE_ID,DN_CREATED_BY,DD_CREATED_ON,DN_PARENT_IMAGE_ID,DE_JOB_ID,DC_LENGTH,DC_WIDTH) VALUES('0','"+result+"','"+childid+"','"+cropImageVm.getLoginUserId()+"',now(),'"+imageId+"','"+jobId+"','"+heightCM+"','"+widthCM+"')";
    	jt.execute(sqlforupdatededata1);
    	*/
        
    	Calendar calendar = Calendar.getInstance();
	    final java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
    	String sqlforupdatededata="INSERT INTO tbl_de_data (DC_CURRENCY,DC_OCR_TEXT,DN_CHILD_IMAGE_ID,DN_CREATED_BY,DD_CREATED_ON,DN_PARENT_IMAGE_ID,DE_JOB_ID,DC_LENGTH,DC_WIDTH)  VALUES (?, ?,?, ?, ?, ?, ?, ?, ?)";
    	
    	Boolean flag=jt.execute(sqlforupdatededata,new PreparedStatementCallback<Boolean>(){  
			@Override
			public Boolean doInPreparedStatement(java.sql.PreparedStatement ps)
					throws SQLException, DataAccessException {
				ps.setString (1, "0");
				ps.setString(2,result);
				ps.setLong (3, childid);
				ps.setLong(4, cropImageVm.getLoginUserId());
				ps.setDate(5, startDate);
				ps.setLong(6, imageId);
				ps.setString(7, jobId);
				ps.setString(8, heightCM);
				ps.setString(9, widthCM);
	    	        return ps.execute(); 
			}  
    	    });
    	
    	
    	CropImageVm cropVm=new CropImageVm();
    	cropVm.setDN_ID(childid);
    	cropVm.setDC_IMAGENAME(fileimageName);
    	
    	cropVm.setChildThumb(thumbimagename);
        
        return cropVm;
        
	}
	
	public static void createDir(String path, long id,long childid) {
		
        File file3 = new File(path+"child" + "/" + id+"/"+childid+"/");
        if (!file3.exists()) {
            file3.mkdirs();
        }
    }
	
	@RequestMapping(value="/delete_parent_image/{id}/{userId}", method=RequestMethod.POST)
	@ResponseBody
	public void deleteParentImage(@PathVariable("id") String id,@PathVariable("userId") String userId){
		
		
		
		String sqlparent="select * from tbl_parent_image t where t.DN_ID="+id+" limit 1";
		Map<String,Object> results =  jt.queryForMap(sqlparent);
        String imageid=null;
        String imageName=null;
        String deletedBy=userId;
        if(results.get("DN_ID")!=null){
        	imageid=results.get("DN_ID").toString();
        }
        if(results.get("DC_IMAGENAME")!=null){
        	imageName=results.get("DC_IMAGENAME").toString();
        }
		
		String sqldeteteChild="insert into tbl_deleted_image (DN_IMAGEID,DC_IMAGENAME,DN_DELETED_BY,DD_DELETED_ON,DB_ISCHILD) VALUES('"+id+"','"+imageName+"','"+deletedBy+"',now(),'0')";
		jt.execute(sqldeteteChild);
		
		String sql="DELETE from tbl_child_image  where DN_PARENT_IMAGE_ID="+id;
		jt.execute(sql);
		
		String sqldeleteparentdedata="delete FROM tbl_de_data where DN_PARENT_IMAGE_ID="+id;
		jt.execute(sqldeleteparentdedata);
		
		String sqldeleteDedata="delete from tbl_de_job where DN_PARENT_IMAGE_ID="+id;
		jt.execute(sqldeleteDedata);
		
		String sqldeleteparent="delete from tbl_parent_image where DN_ID="+id;
		jt.execute(sqldeleteparent);
	}

	
	/*@RequestMapping(value="/get_filter_image_history/{dateFrom}/{dateTo}", method=RequestMethod.POST)
	@ResponseBody
	public void getAllFilterImages(@PathVariable("dateFrom") String dateFrom,@PathVariable("dateTo") String dateTo){
		System.out.println("in get All filter Images");
		System.out.println("date from id" +dateFrom);
		System.out.println("date to is "+dateTo);
		
		
	}*/
	
	
	
	

	
	
	@RequestMapping(value="/delete_child_image/{id}/{userId}", method=RequestMethod.POST)
	@ResponseBody
	public void delete_child_image(@PathVariable("id") String id,@PathVariable("userId") String userId){
		
	
		
		
		
		
		String sqlchild="select * from tbl_child_image t where t.DN_ID="+id+" limit 1";
		Map<String,Object> results =  jt.queryForMap(sqlchild);
		
		
		
		
        String imageid=null;
        String imageName=null;
        String deletedBy=userId;
        if(results.get("DN_ID")!=null){
        	imageid=results.get("DN_ID").toString();
        }
        if(results.get("DC_IMAGENAME")!=null){
        	imageName=results.get("DC_IMAGENAME").toString();
        }
        if(results.get("DC_IMAGENAME")!=null){
        	imageName=results.get("DC_IMAGENAME").toString();
        }
		
       
		String sqldeteteChild="insert into tbl_deleted_image (DN_IMAGEID,DC_IMAGENAME,DN_DELETED_BY,DD_DELETED_ON,DB_ISCHILD) VALUES('"+imageid+"','"+imageName+"','"+deletedBy+"',now(),'1')";
		jt.execute(sqldeteteChild);
		
		
		
		String sql="DELETE from tbl_child_image  where DN_ID="+id;
		jt.execute(sql);
		

		
		String sqldeletededata="delete FROM tbl_de_data  where DN_CHILD_IMAGE_ID="+id;
		jt.execute(sqldeletededata);
		
		
	}
	
	@RequestMapping(value="/move_to_global", method=RequestMethod.POST)
	@ResponseBody
	public String moveToGlobal(@RequestBody List<IdVm> idVm){
		
	
		for(IdVm liveVM : idVm) {
			String sql="update tbl_de_data set DC_IS_GLOBAL='1' where DN_CHILD_IMAGE_ID="+liveVM.getId();
			jt.execute(sql);
			
			String sqlChild="update tbl_child_image set DC_IS_GLOBAL='1' where DN_ID="+liveVM.getId();
			jt.execute(sqlChild);
		}
		
		return "success";
	}
	
	@RequestMapping(value="/move_to_transcription", method=RequestMethod.POST)
	@ResponseBody
	public void moveToTranscription(@RequestBody List<IdVm> idVm){
		
		
		for(IdVm liveVM : idVm) {
			String sql="update tbl_parent_image m set m.DN_STATUS=2 where m.DN_ID="+liveVM.getId();
			jt.execute(sql);
			
			String sqlforDejob="update tbl_de_job t set t.DN_STATUS=0 where t.DN_PARENT_IMAGE_ID="+liveVM.getId();
			jt.execute(sqlforDejob);
			
		}
		
	}
	
	@RequestMapping(value="/moveToAvertorial_parent/{id}", method=RequestMethod.POST)
	@ResponseBody
	public void moveToAdvertorialParent(@PathVariable("id") String id){
		
		
		
			System.out.println("in moveToAdvertorialParent");
			
			String sqlForPublication="select p.DN_ID from tbl_publication p where p.DC_PUBLICATION_TITLE='Advertorial'";
			int pubId=jt.queryForInt(sqlForPublication);
			
			
			String sql="update tbl_parent_image set DN_STATUS=2,DC_SECTION='"+pubId+"' where DN_ID="+id;
			jt.execute(sql);
			
			String sqlforDejob="update tbl_de_job t set t.DN_STATUS=2 where t.DN_PARENT_IMAGE_ID="+id;
			jt.execute(sqlforDejob);
			
	};
	
	@RequestMapping(value="/moveToTranscription_parent/{id}", method=RequestMethod.POST)
	@ResponseBody
	public void moveToTranscriptionParent(@PathVariable("id") String id){
		
		
			String sql="update tbl_parent_image m set m.DN_STATUS=2 where m.DN_ID="+id;
			jt.execute(sql);
			
			String sqlforDejob="update tbl_de_job t set t.DN_STATUS=0 where t.DN_PARENT_IMAGE_ID="+id;
			jt.execute(sqlforDejob);
			
	};
	
	@RequestMapping(value="/move_to_advertorial", method=RequestMethod.POST)
	@ResponseBody
	public void moveToAdvertorial(@RequestBody List<IdVm> idVm){
		
		String sqlForPublication="select p.DN_ID from tbl_publication p where p.DC_PUBLICATION_TITLE='Advertorial'";
		int pubId=jt.queryForInt(sqlForPublication);
		
		for(IdVm liveVM : idVm) {
			String sql="update tbl_parent_image m set m.DN_STATUS=2 ,m.DC_SECTION='"+pubId+"' where m.DN_ID="+liveVM.getId();
			jt.execute(sql);
			
			
			String sqlForDeJob="update tbl_de_job set DN_STATUS=2 where DN_PARENT_IMAGE_ID="+liveVM.getId();
			jt.execute(sqlForDeJob);
		}
		
	}
	
	@RequestMapping(value="/update_edited_image", method=RequestMethod.POST)
	@ResponseBody
	public void updateEditedImage(@RequestBody ParentImageVm idVm) throws ParseException{
		
		String json = new Gson().toJson(idVm);
		
		String titlePublication=idVm.getTitle();
		String publicationTitlkaId=null;
		if(idVm.getTitle()!=null){
			
			String sql1="select DN_ID from tbl_publication where DC_PUBLICATION_TITLE='"+titlePublication+"'";
			publicationTitlkaId=jt.queryForObject(sql1, String.class);
			/*String sql2="update tbl_parent_image set DC_PUBLICATION_TITLE="+id+" where DN_ID="+idVm.getId();
			jt.execute(sql2);*/
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    Date convertedCurrentDate = sdf.parse(idVm.getDate());
	    String d=sdf.format(convertedCurrentDate);
	    java.sql.Date sqlDate = java.sql.Date.valueOf(d);
		
		String sql3="select dN_ID from tbl_publication where DC_PUBLICATION_TITLE='"+idVm.getSection()+"' limit 1";
		long sectorId=jt.queryForLong(sql3);
		
		String sql2="update tbl_parent_image set DC_PUBLICATION_TITLE="+publicationTitlkaId+",DD_ISSUE_DATE='"+sqlDate+"',DC_PAGE='"+idVm.getPage()+"'"+",DC_SECTION='"+sectorId+"'"+" where DN_ID="+idVm.getId();
		jt.execute(sql2);
		
	
	}
	
	
	@RequestMapping(value="/get_filter_task/{searchTask}", method=RequestMethod.GET)
	@ResponseBody
	public List<TaskVm> getfilterTask(@PathVariable("searchTask") String searchTask){
		

		
		List<Map<String,Object>> taskList = new ArrayList<Map<String,Object>>();
		
		String sqlq="select t.DN_ID,t.DN_NAME,t.DN_DESCRIPTION,t.DN_STATUS,t.DD_CREATED_BY,"+
				 " t.DD_CREATED_ON,u.DC_FIRSTNAME,u.DC_LASTNAME "+ 
                " from tbl_task t inner join tbl_user u on t.DD_CREATED_BY=u.DN_ID  ORDER BY t.DN_ID desc";
		
		String sql=null;
		if(searchTask.equalsIgnoreCase("All")){
			
			sql="select t.DN_ID,t.DN_NAME,t.DN_DESCRIPTION,t.DN_STATUS,t.DD_CREATED_BY,"+
				 " t.DD_CREATED_ON,u.DC_FIRSTNAME,u.DC_LASTNAME "+ 
                " from tbl_task t inner join tbl_user u on t.DD_CREATED_BY=u.DN_ID  ORDER BY t.DN_ID desc";
		}else{
			
			sql="select t.DN_ID,t.DN_NAME,t.DN_DESCRIPTION,t.DN_STATUS,t.DD_CREATED_BY, "+
					 "t.DD_CREATED_ON,u.DC_FIRSTNAME,u.DC_LASTNAME "+
                     " from tbl_task t inner join tbl_user u on t.DD_CREATED_BY=u.DN_ID and "+
                     " t.DN_STATUS like '%"+searchTask+"%'  ORDER BY t.DN_ID desc";
			
		/* sql="select * from tbl_task t where t.DN_STATUS like '%"+searchTask+"%'";*/
		}
		taskList=jt.queryForList(sql);

		List<TaskVm> li= new ArrayList<TaskVm>();
		for (Map<String, Object> pImage : taskList) {

			TaskVm tVm= new TaskVm();
			
			
			if(pImage.get("DN_NAME")!=null){

				tVm.name=pImage.get("DN_NAME").toString();
			}
			if(pImage.get("DN_DESCRIPTION")!=null){

				tVm.desc=pImage.get("DN_DESCRIPTION").toString();
			}
			if(pImage.get("DN_STATUS")!=null){

				tVm.status=pImage.get("DN_STATUS").toString();
			}
			if(pImage.get("DD_CREATED_BY")!=null){

				tVm.createdBy=pImage.get("DD_CREATED_BY").toString();
			}
			if(pImage.get("DD_CREATED_ON")!=null){

				tVm.createdOn=pImage.get("DD_CREATED_ON").toString();
			}
			if(pImage.get("DC_FIRSTNAME")!=null){

				tVm.firstName=pImage.get("DC_FIRSTNAME").toString();
			}
			if(pImage.get("DC_LASTNAME")!=null){

				tVm.lastName=pImage.get("DC_LASTNAME").toString();
			}
			if(pImage.get("DN_ID")!=null){
			
			//	tVm.id=Long.parseLong(pImage.get("DN_ID").toString());


				List<TaskImageVm> arrayList= new ArrayList<TaskImageVm>();

				Long id=Long.parseLong(pImage.get("DN_ID").toString());
				tVm.id=id;

				List<Map<String,Object>> taskImageList = new ArrayList<Map<String,Object>>();
				String sqlforChild="select * from tbl_task_image t where t.DN_TASK_ID="+id;
				taskImageList=jt.queryForList(sqlforChild);
				for (Map<String, Object> map : taskImageList) {
					TaskImageVm vm = new TaskImageVm();
					if(map.get("DN_ID")!=null){

						vm.taskImageId=Long.parseLong(map.get("DN_ID").toString());
					}
					if(map.get("DN_IMAGENAME")!=null){

						String image=map.get("DN_IMAGENAME").toString();
						vm.imageName=image;
						vm.thumbImageName=image.split("\\.")[0]+"_thumb.jpg";

					}
					arrayList.add(vm);
				}

				tVm.getListVm().addAll(arrayList);
			}


			li.add(tVm);

		}
		
		
		
		return li;

		
	}
	
	
	@RequestMapping(value="/get_filter_image/{id}", method=RequestMethod.GET)
	@ResponseBody
	public List<ImagesVM> getfilterimage(@PathVariable("id") String filter){
		
		Long filterId=Long.parseLong(filter);
		List<ImagesVM> li= new ArrayList<ImagesVM>();
		String url="/webapp/get-all-parent-image?id=";
		int stat=Integer.parseInt(filter);
		
		
		int n=0;
		int m=0;
		
		List<Map<String,Object>> parentImageList = new ArrayList<Map<String,Object>>();
		String sql="select m.DN_ID,m.DN_STATUS,m.DC_IMAGENAME,m.DD_CREATED_ON,m.DN_CREATED_BY,u.DC_FIRSTNAME,m.DN_DELETED_BY,m.DD_DELETED_ON,m.DB_DELETED,m.DD_ISSUE_DATE,m.DC_PAGE,m.DC_PUBLICATION_TITLE,m.DC_SECTION,m.DC_SECTION_OTHER,m.DC_SECTION_SPECIAL_REGIONAL,m.DC_SECTION_SPECIAL_TOPIC,m.DC_HEIGHT,m.DC_WIDTH from tbl_parent_image m inner join tbl_user u on m.DN_CREATED_BY=u.DN_ID  ORDER BY m.DN_ID";
		parentImageList=jt.queryForList(sql);
		
		List<String> duplicate= new ArrayList<String>();
		Set<String> duplicateNamess = new HashSet<String>();
		int l=0;
		
		
		for (Map<String, Object> pImage : parentImageList) {
			
			ImagesVM imagesVM= new ImagesVM();
			if(pImage.get("DC_IMAGENAME")!=null){
				
				if(!duplicateNamess.add(pImage.get("DC_IMAGENAME").toString())) {
					
					imagesVM.setDuplicate(true);
					n++;
				}else{
					imagesVM.setDuplicate(false);
					m++;
				}	
				
				imagesVM.setDC_IMAGENAME(pImage.get("DC_IMAGENAME").toString());
				String thumImageName=pImage.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
				imagesVM.setThumb(thumImageName);
			}
			
			if(pImage.get("DN_STATUS")!=null){
				if(Integer.parseInt(pImage.get("DN_STATUS").toString())==stat){
							
					imagesVM.setDN_STATUS(Integer.parseInt(pImage.get("DN_STATUS").toString()));
					
					l++;
					
					if(pImage.get("DN_ID")!=null){
						
						imagesVM.setDN_ID(Long.valueOf(pImage.get("DN_ID").toString()));
					}
					
					String publicationTitle=null;
					if(pImage.get("DC_PUBLICATION_TITLE")!=null){
						String sql11="select t.DC_PUBLICATION_TITLE from tbl_publication t where t.DN_ID="+pImage.get("DC_PUBLICATION_TITLE").toString();
						publicationTitle=jt.queryForObject(sql11, String.class);
						imagesVM.setDC_PUBLICATION_TITLE(publicationTitle);
						
					}
					
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Object d=pImage.get("DD_ISSUE_DATE");
					
					if(d!=null){
					
						String dateInString1 =d.toString();
						try {
							Date date1 = formatter.parse(dateInString1);
							imagesVM.setDD_ISSUE_DATE(date1);
						
							imagesVM.setDateissue(formatter.format(date1));
							
							
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					
					Object d2=pImage.get("DD_CREATED_ON");
					if(d2!=null){
						String dateInString2 =d2.toString();
						try {
							Date date2 = formatter.parse(dateInString2);
							imagesVM.setDD_CREATED_ON(date2);
							imagesVM.setCreateDate(formatter.format(date2));
							
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					
					
					if(pImage.get("DC_FIRSTNAME")!=null){
						imagesVM.setCREATED_BY(pImage.get("DC_FIRSTNAME").toString());
					}
					
					String section=null;
					if(pImage.get("DC_SECTION")!=null){
						String sql22="select t.DC_PUBLICATION_TITLE from tbl_publication t where t.DN_ID="+pImage.get("DC_SECTION").toString();
						section=jt.queryForObject(sql22, String.class);
						imagesVM.setDC_SECTION(section);
					}
					if(pImage.get("DC_SECTION_OTHER")!=null){
						imagesVM.setDC_SECTION_OTHER(pImage.get("DC_SECTION_OTHER").toString());
					}
					
					if(pImage.get("DC_SECTION_SPECIAL_REGIONAL")!=null){
						imagesVM.setDC_SECTION_SPECIAL_REGIONAL(pImage.get("DC_SECTION_SPECIAL_REGIONAL").toString());
					}
					if(pImage.get("DC_SECTION_SPECIAL_TOPIC")!=null){
						imagesVM.setDC_SECTION_SPECIAL_TOPIC(pImage.get("DC_SECTION_SPECIAL_TOPIC").toString());
					}
					if(pImage.get("DC_HEIGHT")!=null){
						imagesVM.setDC_HEIGHT(pImage.get("DC_HEIGHT").toString());
					}
					if(pImage.get("DC_WIDTH")!=null){
						imagesVM.setDC_WIDTH(pImage.get("DC_WIDTH").toString());
					}
					if(pImage.get("DC_PAGE")!=null){
						imagesVM.setDC_PAGE(pImage.get("DC_PAGE").toString());
					}
					
					
					imagesVM.setImageUrl(url+Long.parseLong(pImage.get("DN_ID").toString()));

					
					
					String id=pImage.get("DN_ID").toString();
					List<Map<String,Object>> childImageList = new ArrayList<Map<String,Object>>();
					String sql1="select m.DC_IMAGENAME,m.DN_ID from tbl_child_image m where m.DN_PARENT_IMAGE_ID="+id;
					childImageList=jt.queryForList(sql1);
					List<ChildImageVm> arrayList= new ArrayList<ChildImageVm>();
					for (Map<String, Object> map : childImageList) {
						ChildImageVm childVm= new ChildImageVm();
						
						if(map.get("DN_ID")!=null){
							childVm.setDN_ID(Long.valueOf(map.get("DN_ID").toString()));
						}
						if(map.get("DC_IMAGENAME")!=null){
							childVm.setDC_IMAGENAME(map.get("DC_IMAGENAME").toString());
							String thumChildImageName=map.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
							childVm.setChildThumb(thumChildImageName);
							
						}
					
					    arrayList.add(childVm);
					}
					imagesVM.getListVm().addAll(arrayList);
					li.add(imagesVM);
					
				}
			}

		}
		
		
		Collections.reverse(li);
		String json = new Gson().toJson(li);
		return li;
	
	}
	
	
	
/*	
	@RequestMapping(value="/all_image_history", method=RequestMethod.POST)
	@ResponseBody
	public List getAllImageHistory(@RequestBody ImageHistoryVm Ivm) throws ParseException{

		String[] arrTemp=Ivm.getDateFrom().split("/");
		String dateFromT=arrTemp[2]+"-"+arrTemp[0]+"-"+arrTemp[1];
		String[] arrTempTo=Ivm.getDateTo().split("/");
		String dateToT=arrTempTo[2]+"-"+arrTempTo[0]+"-"+arrTempTo[1];
		

		List<ImageHistoryVm> listImage= new ArrayList<ImageHistoryVm>();
		List<Map<String,Object>> imageHistoryList = new ArrayList<Map<String,Object>>();
		
		String sql="select * from tbl_parent_image t where date(t.DD_CREATED_ON) BETWEEN '"+dateFromT+"' and '"+dateToT+"'";
		imageHistoryList=jt.queryForList(sql);
	
		
		int i=1;
		for (Map<String, Object> pImage : imageHistoryList) {
			ImageHistoryVm vm= new ImageHistoryVm();
			
			vm.setId(i);
			i++;
			if(pImage.get("DN_ID")!=null){
			
				vm.setJobId(Long.parseLong(pImage.get("DN_ID").toString()));
			}
			if(pImage.get("DC_IMAGENAME")!=null){
				
				vm.setImageName(pImage.get("DC_IMAGENAME").toString());
			}
			if(pImage.get("DD_CREATED_ON")!=null){

				vm.setCreatedDate(pImage.get("DD_CREATED_ON").toString());
			}
			if(pImage.get("DD_ISSUE_DATE")!=null){

				vm.setIssueDate(pImage.get("DD_ISSUE_DATE").toString());
			}
			
			listImage.add(vm);
		}
		return listImage;
	}*/
	
	
	@RequestMapping(value="/all_image_history", method=RequestMethod.POST)
	@ResponseBody
	public List getAllImageHistory(@RequestBody ImageHistoryVm Ivm) throws ParseException{

		String[] arrTemp=Ivm.getDateFrom().split("/");
		String dateFromT=arrTemp[2]+"-"+arrTemp[0]+"-"+arrTemp[1];
		String[] arrTempTo=Ivm.getDateTo().split("/");
		String dateToT=arrTempTo[2]+"-"+arrTempTo[0]+"-"+arrTempTo[1];
		

		List<Map<String,Object>> imageHistoryList = new ArrayList<Map<String,Object>>();
		
		/*String sql="select * from tbl_parent_image t where date(t.DD_CREATED_ON) BETWEEN '"+dateFromT+"' and '"+dateToT+"'";
		imageHistoryList=jt.queryForList(sql);*/
		
		String sqlForHistory="select *,date(t.DD_CREATED_ON) as date from tbl_parent_image t "
				+ "where date(t.DD_CREATED_ON) BETWEEN '"+dateFromT+"' and '"+dateToT+"' "
				+ "order by date(t.DD_CREATED_ON) desc";
	
		imageHistoryList=jt.queryForList(sqlForHistory);
		return imageHistoryList;
	}
	
	
	@RequestMapping(value="/get_filter_image_history", method=RequestMethod.POST)
	@ResponseBody
	public List getFilterImageHistory(@RequestBody ImageHistoryVm Ivm) throws ParseException{
	
		String[] arrTemp=Ivm.getDateFrom().split("/");
		String dateFromT=arrTemp[2]+"-"+arrTemp[0]+"-"+arrTemp[1];
		String[] arrTempTo=Ivm.getDateTo().split("/");
		String dateToT=arrTempTo[2]+"-"+arrTempTo[0]+"-"+arrTempTo[1];

		List<ImageHistoryVm> listImage= new ArrayList<ImageHistoryVm>();
		List<Map<String,Object>> imageHistoryList = new ArrayList<Map<String,Object>>();
		
		/*String sql="select * from tbl_parent_image t where date(t.DD_CREATED_ON) BETWEEN '"+dateFromT+"' and '"+dateToT+"'";
		imageHistoryList=jt.queryForList(sql);*/
		
		
		String sqlForHistory="select *,date(t.DD_CREATED_ON) as date from tbl_parent_image t "
				+ "where date(t.DD_CREATED_ON) BETWEEN '"+dateFromT+"' and '"+dateToT+"' "
				+ "order by date(t.DD_CREATED_ON) desc";
	
		imageHistoryList=jt.queryForList(sqlForHistory);
	
		return imageHistoryList;
	}
	

	@RequestMapping(value="/get_all_dates/{month}/{year}/{publication}", method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> getAllDates(@PathVariable("month") String month,@PathVariable("year") String year,@PathVariable("publication") String publication ){
	
		String sql=" select DISTINCT(t.DD_ISSUE_DATE) from tbl_publication p,tbl_parent_image t where  p.DN_ID=t.DC_PUBLICATION_TITLE "
                    +" and p.DC_PUBLICATION_TITLE='"+publication+"' and MONTH(t.DD_ISSUE_DATE)='"+month+"' and YEAR(t.DD_ISSUE_DATE)='"+year+"' ORDER by t.DD_ISSUE_DATE";
		
		List<Map<String,Object>> allDateList = new ArrayList<Map<String,Object>>();
		allDateList=jt.queryForList(sql);
		return allDateList;
		
	}
	
	@RequestMapping(value="/get_all_list_by_date/{month}/{year}/{publication}/{date}", method=RequestMethod.POST)
	@ResponseBody
	public List<ImagesVM> getAllListByDate(@PathVariable("month") String month,@PathVariable("year") String year,@PathVariable("publication") String publication,@PathVariable("date") String date ){
	
		List<ImagesVM> resultList= new ArrayList<ImagesVM>();
		List<Map<String,Object>> parentImageList = new ArrayList<Map<String,Object>>();
		String sql="select p.DN_ID,u.DC_FIRSTNAME,u.DC_LASTNAME,p.DC_IMAGENAME,p.DN_STATUS,p.DC_PUBLICATION_TITLE"
				+ ",p.DD_ISSUE_DATE,t.DC_PUBLICATION_TITLE as publication " 
				+" from tbl_parent_image p,tbl_publication t,tbl_user u where p.DC_PUBLICATION_TITLE=t.DN_ID  "
				+ "and p.DN_CREATED_BY=u.DN_ID "
				+ " and DATE(p.DD_ISSUE_DATE)='"+date+"' and t.DC_PUBLICATION_TITLE='"+publication+"' ORDER BY p.DC_PAGE";
		parentImageList=jt.queryForList(sql);

		for (Map<String, Object> pImage : parentImageList) {

			ImagesVM imagesVM= new ImagesVM();
			if(pImage.get("DC_IMAGENAME")!=null){

				imagesVM.setDC_IMAGENAME(pImage.get("DC_IMAGENAME").toString());
				String thumImageName=pImage.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
				imagesVM.setThumb(thumImageName);
			}

			if(pImage.get("DC_FIRSTNAME")!=null){
				imagesVM.setCREATED_BY(pImage.get("DC_FIRSTNAME").toString());
			}

			if(pImage.get("publication")!=null){
				imagesVM.publication=pImage.get("publication").toString();
					
			}
			
			if(pImage.get("DN_ID")!=null){
				imagesVM.setDN_ID(Long.valueOf(pImage.get("DN_ID").toString()));
			}

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Object d=pImage.get("DD_ISSUE_DATE");

			if(d!=null){
				String dateInString1 =d.toString();
				try {
					Date date1 = formatter.parse(dateInString1);
					imagesVM.setDD_ISSUE_DATE(date1);
					imagesVM.setDateissue(formatter.format(date1));

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			

			String id=pImage.get("DN_ID").toString();
			List<Map<String,Object>> childImageList = new ArrayList<Map<String,Object>>();
			String sql1="select m.DC_IMAGENAME,m.DN_ID from tbl_child_image m where m.DC_IS_GLOBAL='0' and m.DN_PARENT_IMAGE_ID="+id;
			childImageList=jt.queryForList(sql1);
			List<ChildImageVm> arrayList= new ArrayList<ChildImageVm>();
			for (Map<String, Object> map : childImageList) {
				ChildImageVm childVm= new ChildImageVm();

				if(map.get("DN_ID")!=null){
					childVm.setDN_ID(Long.valueOf(map.get("DN_ID").toString()));
				}
				if(map.get("DC_IMAGENAME")!=null){
					childVm.setDC_IMAGENAME(map.get("DC_IMAGENAME").toString());
					String thumChildImageName=map.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
					childVm.setChildThumb(thumChildImageName);

				}

				arrayList.add(childVm);
			}
			
			imagesVM.getListVm().addAll(arrayList);

			resultList.add(imagesVM);
		}

		return resultList;
		
	}
	
	
	@RequestMapping(value="/get_publication_gallery_images/{month}/{year}/{publication}", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, List<ImagesVM>> getPublicatioGalleryImages(@PathVariable("month") String month,@PathVariable("year") String year,@PathVariable("publication") String publication ){



		List<ImagesVM> usList= new ArrayList<ImagesVM>();
		List<ImagesVM> ukList= new ArrayList<ImagesVM>();
		
		List<Map<String,Object>> parentImageList = new ArrayList<Map<String,Object>>();
		String sql="select p.DN_ID,u.DC_FIRSTNAME,u.DC_LASTNAME,p.DC_IMAGENAME,p.DN_STATUS,p.DC_PUBLICATION_TITLE"
				+ ",p.DD_ISSUE_DATE,t.DC_PUBLICATION_TITLE as publication " 
				+" from tbl_parent_image p,tbl_publication t,tbl_user u where p.DC_PUBLICATION_TITLE=t.DN_ID  "
				+ "and p.DN_CREATED_BY=u.DN_ID "
				+ " and month(p.DD_ISSUE_DATE)='"+month+"' and year(p.DD_ISSUE_DATE)='"+year+"' and t.DC_PUBLICATION_TITLE "
				+ "like '%"+publication+"%'";
		parentImageList=jt.queryForList(sql);

		for (Map<String, Object> pImage : parentImageList) {

			ImagesVM imagesVM= new ImagesVM();
			if(pImage.get("DC_IMAGENAME")!=null){

				imagesVM.setDC_IMAGENAME(pImage.get("DC_IMAGENAME").toString());
				String thumImageName=pImage.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
				imagesVM.setThumb(thumImageName);
			}

			if(pImage.get("DC_FIRSTNAME")!=null){
				imagesVM.setCREATED_BY(pImage.get("DC_FIRSTNAME").toString());
			}

			if(pImage.get("DN_ID")!=null){
				imagesVM.setDN_ID(Long.valueOf(pImage.get("DN_ID").toString()));
			}

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Object d=pImage.get("DD_ISSUE_DATE");

			if(d!=null){
				String dateInString1 =d.toString();
				try {
					Date date1 = formatter.parse(dateInString1);
					imagesVM.setDD_ISSUE_DATE(date1);
					imagesVM.setDateissue(formatter.format(date1));

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			String id=pImage.get("DN_ID").toString();
			List<Map<String,Object>> childImageList = new ArrayList<Map<String,Object>>();
			String sql1="select m.DC_IMAGENAME,m.DN_ID from tbl_child_image m where m.DN_PARENT_IMAGE_ID="+id;
			childImageList=jt.queryForList(sql1);
			List<ChildImageVm> arrayList= new ArrayList<ChildImageVm>();
			for (Map<String, Object> map : childImageList) {
				ChildImageVm childVm= new ChildImageVm();

				if(map.get("DN_ID")!=null){
					childVm.setDN_ID(Long.valueOf(map.get("DN_ID").toString()));
				}
				if(map.get("DC_IMAGENAME")!=null){
					childVm.setDC_IMAGENAME(map.get("DC_IMAGENAME").toString());
					String thumChildImageName=map.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
					childVm.setChildThumb(thumChildImageName);

				}

				arrayList.add(childVm);
			}
			imagesVM.getListVm().addAll(arrayList);

			if(pImage.get("publication")!=null){
				
				imagesVM.publication=pImage.get("publication").toString();
				if(pImage.get("publication").toString().contains("UK")){
					ukList.add(imagesVM);
				}else{
					usList.add(imagesVM);
				}

			}

		}

		
		Map<String, List<ImagesVM>> vm= new HashMap<String, List<ImagesVM>>();

		vm.put("us", usList);
		vm.put("uk", ukList);
		
		return vm;
	}
	
	@RequestMapping(value="/get_publicatio_images/{month}/{year}", method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> getPublicatioImages(@PathVariable("month") String month,@PathVariable("year") String year){
	
		System.out.println("in getPublicatioImages");
		
		String sqlAll="select p.DN_ID,p.DD_ISSUE_DATE,pb.DC_PUBLICATION_TITLE,p.DC_IMAGENAME,p.DN_STATUS as parrentStatus,d.DN_STATUS as jobStatus "
				+ "from tbl_parent_image p ,tbl_de_job d,tbl_publication pb where p.DN_ID=d.DN_PARENT_IMAGE_ID and p.DC_PUBLICATION_TITLE=pb.DN_ID "
				+ "and MONTH(p.DD_ISSUE_DATE)='"+month+"' and year(p.DD_ISSUE_DATE)='"+year+"' group "
				+ "by p.DD_ISSUE_DATE,p.DN_ID;";
		
		List<Map<String, Object>> allData=jt.queryForList(sqlAll);
		return allData;
	}
	
	
	@RequestMapping(value="/update_task", method=RequestMethod.POST)
	@ResponseBody
	public String updateTask( HttpServletRequest request){
		

		String name=request.getParameter("name");
		String desc=request.getParameter("desc");
		String status=request.getParameter("status");
		String id=request.getParameter("id");
		
		long taskid=Long.parseLong(id);
		
	
		String description=desc.replaceAll("\\'", "\\\\'");
		
	
		
		
		String sql="update tbl_task  set DN_NAME='"+name+"',DN_DESCRIPTION='"+description+"',DN_STATUS='"+status+"' where DN_ID="+taskid;
	   
		jt.execute(sql);
		
		MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
		if(!mRequest.getFileMap().isEmpty()){
			for (MultipartFile file : mRequest.getFileMap().values()) {
			
				
				String sqlimage="INSERT into tbl_task_image (DN_TASK_ID) VALUES('"+id+"')";
				jt.execute(sqlimage);
				
				long taskImageId = jt.queryForLong("select max(DN_ID) from tbl_task_image");
				
				String fileName=file.getOriginalFilename();

				File file3 = new File(fullImagePath+"taskImages/"+id+"/" +taskImageId+"/");
				if (!file3.exists()) {
					file3.mkdirs();
				}

				String fullpath = fullImagePath+"taskImages/"+id+"/" +taskImageId+"/"+fileName;
				File savefile = new File(fullpath);
				try {
					file.transferTo(savefile);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {	
					Thumbnails.of(new File(fullpath))
					.width(200).keepAspectRatio(true)
					.outputFormat("jpg")
					.toFile(fullImagePath+"taskImages/"+id+"/"+taskImageId+"/"+fileName.split("\\.")[0]+"_thumb.jpg");
					
					String s=fullImagePath+"taskImages/"+id+"/"+taskImageId+"/"+fileName.split("\\.")[0]+"_thumb.jpg";
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				
				String filePath = saveFileToSystem(file,taskid,taskImageId); 
				String sqlupdate="update tbl_task_image set DN_IMAGENAME ='"+file.getOriginalFilename()+"' where DN_ID="+taskImageId;
				jt.execute(sqlupdate);
				
			}			
		}
		
	   
		return "success";
		
	}
	
	@RequestMapping(value="/save_task", method=RequestMethod.POST)
	@ResponseBody
	public String saveTask( HttpServletRequest request){

		String name=request.getParameter("name");
		String desc=request.getParameter("desc");
		String status=request.getParameter("status");
		String createdBy=request.getParameter("createdBy");
		
		String description=desc.replaceAll("\\'", "\\\\'");

		String sql="insert into tbl_task (DN_NAME,DN_DESCRIPTION,DN_STATUS,DD_CREATED_BY,DD_CREATED_ON) VALUES('"+name+"','"+description+"','"+status+"','"+createdBy+"',CURRENT_DATE())";
		jt.execute(sql);

		long taskId = jt.queryForLong("select max(DN_ID) from tbl_task");

		MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
		if(!mRequest.getFileMap().isEmpty()){
			for (MultipartFile file : mRequest.getFileMap().values()) {

				String sqlimage="INSERT into tbl_task_image (DN_TASK_ID) VALUES("+taskId+")";
				jt.execute(sqlimage);

				long taskImageId = jt.queryForLong("select max(DN_ID) from tbl_task_image");


				String[] str1 = file.getOriginalFilename().split(Pattern.quote("."));

				String extention = "."+str1[str1.length-1];
				String contenttype=file.getContentType();
				String fileName=file.getOriginalFilename();


				File file3 = new File(fullImagePath+"taskImages/"+taskId+ "/" +taskImageId+"/");
				if (!file3.exists()) {
					file3.mkdirs();
				}

				String fullpath = fullImagePath+"taskImages/"+taskId+ "/" +taskImageId+"/"+fileName;
				File savefile = new File(fullpath);
				try {
					file.transferTo(savefile);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {	
					Thumbnails.of(new File(fullpath))
					.width(200).keepAspectRatio(true)
					.outputFormat("jpg")
					.toFile(fullImagePath+"taskImages/"+taskId+"/"+taskImageId+"/"+fileName.split("\\.")[0]+"_thumb.jpg");
					
					String s=fullImagePath+"taskImages/"+taskId+"/"+taskImageId+"/"+fileName.split("\\.")[0]+"_thumb.jpg";
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				String sqlupdate="update tbl_task_image set DN_IMAGENAME ='"+fileName+"' where DN_ID="+taskImageId;
				jt.execute(sqlupdate);

			}			
		}

		return "success";

	}
	
	
	@RequestMapping(value="/update_faq", method=RequestMethod.POST)
	@ResponseBody
	public String updateFAQ( HttpServletRequest request){
		
		String Operation=request.getParameter("Operation");
		String desc=request.getParameter("desc");
		String data=request.getParameter("data");
		String alert=request.getParameter("alert");
		String id=request.getParameter("id");

		String description=desc.replaceAll("\\'", "\\\\'");
		
		long faqId=Long.parseLong(id);

		String sql="update tbl_faq set DN_OPERATION='"+Operation+"',DN_ALERT='"+alert+"',DN_DESCRIPTION='"+description+"',DN_DATAFIELD='"+data+"' where DN_ID="+faqId;
	   
		jt.execute(sql);
		
		MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
		if(!mRequest.getFileMap().isEmpty()){
			for (MultipartFile file : mRequest.getFileMap().values()) {

				String sqlimage=" insert INTO tbl_faq_images (DN_FAQ_ID) VALUES('"+faqId+"')";
				jt.execute(sqlimage);
				
				long faqImageId = jt.queryForLong("select max(DN_ID) from tbl_faq_images");
				
				String fileName=file.getOriginalFilename();
				
				
				File file3 = new File(fullImagePath+"faqImages/"+faqId+ "/" +faqImageId+"/");
				if (!file3.exists()) {
					file3.mkdirs();
				}

				String fullpath = fullImagePath+"faqImages/"+faqId+"/"+faqImageId+"/"+fileName;
				File savefile = new File(fullpath);
				try {
					file.transferTo(savefile);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				
				try {	
					Thumbnails.of(new File(fullpath))
					.width(200).keepAspectRatio(true)
					.outputFormat("jpg")
					.toFile(fullImagePath+"faqImages/"+faqId+"/"+faqImageId+"/"+fileName.split("\\.")[0]+"_thumb.jpg");
					
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				
				String sqlupdate="update tbl_faq_images set DN_IMAGENAME ='"+file.getOriginalFilename()+"' where DN_ID="+faqImageId;
				jt.execute(sqlupdate);
				
			}			
		}
		
	   
		return "success";
		
	}


	
	
	@RequestMapping(value="/save_faq", method=RequestMethod.POST)
	@ResponseBody
	public String saveFAQ( HttpServletRequest request){
		
		String Operation=request.getParameter("Operation");
		String desc=request.getParameter("desc");
		String data=request.getParameter("data");
		String createdBy=request.getParameter("createdBy");
		String alert=request.getParameter("alert");

		String description=desc.replaceAll("\\'", "\\\\'");
		
		
		String sql="insert INTO tbl_faq (DN_OPERATION,DN_ALERT,DN_DESCRIPTION,DN_DATAFIELD,DD_CREATED_BY,DD_CREATED_ON) VALUES('"+Operation+"','"+alert+"','"+description+"','"+data+"','"+createdBy+"',CURRENT_DATE())";
		jt.execute(sql);
		
		
		long faqId = jt.queryForLong("select max(DN_ID) from tbl_faq");
		
		MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
		if(!mRequest.getFileMap().isEmpty()){
			
			for (MultipartFile file : mRequest.getFileMap().values()) {
				
				String sqlimage="insert into tbl_faq_images (DN_FAQ_ID) VALUES("+faqId+")";
				jt.execute(sqlimage);
				
				long faqImageId = jt.queryForLong("select max(DN_ID) from tbl_faq_images");
				
				String fileName=file.getOriginalFilename();
			
				
				File file3 = new File(fullImagePath+"faqImages/"+faqId+ "/" +faqImageId+"/");
				if (!file3.exists()) {
					file3.mkdirs();
				}

				String fullpath = fullImagePath+"faqImages/"+faqId+"/"+faqImageId+"/"+fileName;
				File savefile = new File(fullpath);
				try {
					file.transferTo(savefile);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				
				try {	
					Thumbnails.of(new File(fullpath))
					.width(200).keepAspectRatio(true)
					.outputFormat("jpg")
					.toFile(fullImagePath+"faqImages/"+faqId+"/"+faqImageId+"/"+fileName.split("\\.")[0]+"_thumb.jpg");
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				String sqlupdate="update tbl_faq_images set DN_IMAGENAME ='"+file.getOriginalFilename()+"' where DN_ID="+faqImageId;
				jt.execute(sqlupdate);
				
			}			
		}
		
		
		return "success";
		
	}

	



	private String saveFileToSystem(MultipartFile file, long taskId, long taskImageId){



		String s=fullImagePath;

		File file3 = new File(fullImagePath+"taskImages/"+taskId+ "/" +taskImageId+"/");
		if (!file3.exists()) {
			file3.mkdirs();
		}

		String fullpath = fullImagePath+"taskImages/"+taskId+ "/" +taskImageId+"/"+file.getOriginalFilename();
		File savefile = new File(fullpath);
		try {
			file.transferTo(savefile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return savefile.getAbsolutePath();
	}


	private String saveFileToSystemfaq(MultipartFile file, long taskId, long taskImageId){

		String s=fullImagePath;

		File file3 = new File(fullImagePath+"faqImages/"+taskId+ "/" +taskImageId+"/");
		if (!file3.exists()) {
			file3.mkdirs();
		}


		String fullpath = fullImagePath+"faqImages/"+taskId+ "/" +taskImageId+"/"+file.getOriginalFilename();
		File savefile = new File(fullpath);
		try {
			file.transferTo(savefile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return savefile.getAbsolutePath();
	}


	@RequestMapping(value="/get_all_faq", method=RequestMethod.GET)
	@ResponseBody
	public List getAllFAQ(){

		List<Map<String,Object>> taskList = new ArrayList<Map<String,Object>>();
		/*String sql="select * from tbl_task t ORDER BY t.DN_ID desc";*/
		String sql="select t.DN_ID,t.DN_DESCRIPTION,t.DN_OPERATION,t.DD_CREATED_BY,t.DD_CREATED_ON,u.DC_FIRSTNAME ,"
				+ " u.DC_LASTNAME,t.DN_ALERT,t.DN_DATAFIELD from tbl_faq t inner join tbl_user u "
				+ " on t.DD_CREATED_BY=u.DN_ID  ORDER BY t.DN_ID desc ";
		taskList=jt.queryForList(sql);

		List<TaskVm> li= new ArrayList<TaskVm>();
		for (Map<String, Object> pImage : taskList) {

			TaskVm tVm= new TaskVm();


			if(pImage.get("DN_DESCRIPTION")!=null){

				tVm.desc=pImage.get("DN_DESCRIPTION").toString();
			}
			if(pImage.get("DN_OPERATION")!=null){

				tVm.operation=pImage.get("DN_OPERATION").toString();
			}
			if(pImage.get("DN_ALERT")!=null){

				tVm.alert=pImage.get("DN_ALERT").toString();
			}
			if(pImage.get("DN_DATAFIELD")!=null){

				tVm.dataField=pImage.get("DN_DATAFIELD").toString();
			}
			if(pImage.get("DD_CREATED_BY")!=null){

				tVm.createdBy=pImage.get("DD_CREATED_BY").toString();
			}
			if(pImage.get("DD_CREATED_ON")!=null){

				tVm.createdOn=pImage.get("DD_CREATED_ON").toString();
			}
			if(pImage.get("DC_FIRSTNAME")!=null){

				tVm.firstName=pImage.get("DC_FIRSTNAME").toString();
			}
			if(pImage.get("DC_LASTNAME")!=null){

				tVm.lastName=pImage.get("DC_LASTNAME").toString();
			}


			if(pImage.get("DN_ID")!=null){

				List<TaskImageVm> arrayList= new ArrayList<TaskImageVm>();

				Long id=Long.parseLong(pImage.get("DN_ID").toString());
				tVm.id=id;

				List<Map<String,Object>> taskImageList = new ArrayList<Map<String,Object>>();
				String sqlforChild="select * from tbl_faq_images f where f.DN_FAQ_ID="+id;
				taskImageList=jt.queryForList(sqlforChild);
				for (Map<String, Object> map : taskImageList) {
					TaskImageVm vm = new TaskImageVm();
					if(map.get("DN_ID")!=null){

						vm.taskImageId=Long.parseLong(map.get("DN_ID").toString());
					}
					if(map.get("DN_IMAGENAME")!=null){
						String image=map.get("DN_IMAGENAME").toString();
						vm.imageName=image;
						vm.thumbImageName=image.split("\\.")[0]+"_thumb.jpg";
					}
					arrayList.add(vm);
				}

				tVm.getListVm().addAll(arrayList);
			}

			li.add(tVm);
		}
		return li;
	}


	
	@RequestMapping(value="/get_all_task", method=RequestMethod.GET)
	@ResponseBody
	public List getAllTask(){
		
		List<Map<String,Object>> taskList = new ArrayList<Map<String,Object>>();
		/*String sql="select * from tbl_task t ORDER BY t.DN_ID desc";*/
		String sql="select t.DN_ID,t.DN_NAME,t.DN_DESCRIPTION,t.DN_STATUS,t.DD_CREATED_BY,"+
				 " t.DD_CREATED_ON,u.DC_FIRSTNAME,u.DC_LASTNAME "+ 
                 " from tbl_task t inner join tbl_user u on t.DD_CREATED_BY=u.DN_ID  ORDER BY t.DN_ID desc";
		taskList=jt.queryForList(sql);

		List<TaskVm> li= new ArrayList<TaskVm>();
		for (Map<String, Object> pImage : taskList) {

			TaskVm tVm= new TaskVm();
			
			
			if(pImage.get("DN_NAME")!=null){

				tVm.name=pImage.get("DN_NAME").toString();
			}
			if(pImage.get("DN_DESCRIPTION")!=null){

				tVm.desc=pImage.get("DN_DESCRIPTION").toString();
			}
			if(pImage.get("DN_STATUS")!=null){

				tVm.status=pImage.get("DN_STATUS").toString();
			}
			if(pImage.get("DD_CREATED_BY")!=null){

				tVm.createdBy=pImage.get("DD_CREATED_BY").toString();
			}
			if(pImage.get("DD_CREATED_ON")!=null){

				tVm.createdOn=pImage.get("DD_CREATED_ON").toString();
			}
			if(pImage.get("DC_FIRSTNAME")!=null){

				tVm.firstName=pImage.get("DC_FIRSTNAME").toString();
			}
			if(pImage.get("DC_LASTNAME")!=null){

				tVm.lastName=pImage.get("DC_LASTNAME").toString();
			}
			
			
			if(pImage.get("DN_ID")!=null){

				List<TaskImageVm> arrayList= new ArrayList<TaskImageVm>();
			
				Long id=Long.parseLong(pImage.get("DN_ID").toString());
				tVm.id=id;
				
				List<Map<String,Object>> taskImageList = new ArrayList<Map<String,Object>>();
				String sqlforChild="select * from tbl_task_image t where t.DN_TASK_ID="+id;
				taskImageList=jt.queryForList(sqlforChild);
				for (Map<String, Object> map : taskImageList) {
					TaskImageVm vm = new TaskImageVm();
					if(map.get("DN_ID")!=null){
						
						vm.taskImageId=Long.parseLong(map.get("DN_ID").toString());
					}
					if(map.get("DN_IMAGENAME")!=null){
						
						String image=map.get("DN_IMAGENAME").toString();
						vm.imageName=image;
						vm.thumbImageName=image.split("\\.")[0]+"_thumb.jpg";
						
					}
					arrayList.add(vm);
				}
				
				tVm.getListVm().addAll(arrayList);
			}
			
			li.add(tVm);

		}
		
		
		
		return li;
	}
	
	@RequestMapping(value="/all_image_list", method=RequestMethod.GET)
	@ResponseBody
	public List getAllImagelist(){
		List<ImagesVM> li= new ArrayList<ImagesVM>();
		String url="/webapp/get-all-parent-image?id=";
		
		int n=0;
		int m=0;
		
		List<Map<String,Object>> parentImageList = new ArrayList<Map<String,Object>>();
		String sql="select m.DN_ID,m.DN_STATUS,m.DC_IMAGENAME,m.DD_CREATED_ON,m.DN_CREATED_BY,u.DC_FIRSTNAME,m.DN_DELETED_BY,m.DD_DELETED_ON,m.DB_DELETED,m.DD_ISSUE_DATE,m.DC_PAGE,m.DC_PUBLICATION_TITLE,m.DC_SECTION,m.DC_SECTION_OTHER,m.DC_SECTION_SPECIAL_REGIONAL,m.DC_SECTION_SPECIAL_TOPIC,m.DC_HEIGHT,m.DC_WIDTH from tbl_parent_image m inner join tbl_user u on m.DN_CREATED_BY=u.DN_ID  ORDER BY m.DN_ID";
		parentImageList=jt.queryForList(sql);
		
		List<String> duplicate= new ArrayList<String>();
		Set<String> duplicateNamess = new HashSet<String>();
			
		int l=0;
		
		for (Map<String, Object> pImage : parentImageList) {
			
			ImagesVM imagesVM= new ImagesVM();
			if(pImage.get("DC_IMAGENAME")!=null){
				
				if(!duplicateNamess.add(pImage.get("DC_IMAGENAME").toString())) {
					
					imagesVM.setDuplicate(true);
					n++;
				}else{
					imagesVM.setDuplicate(false);
					m++;
				}	
				
				imagesVM.setDC_IMAGENAME(pImage.get("DC_IMAGENAME").toString());
				String thumImageName=pImage.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
				imagesVM.setThumb(thumImageName);
			}
			
			if(pImage.get("DN_STATUS")!=null){
				if(Integer.parseInt(pImage.get("DN_STATUS").toString())==0){
					imagesVM.setDN_STATUS(Integer.parseInt(pImage.get("DN_STATUS").toString()));
					l++;
					if(pImage.get("DN_ID")!=null){
						imagesVM.setDN_ID(Long.valueOf(pImage.get("DN_ID").toString()));
					}
					String publicationTitle=null;
					if(pImage.get("DC_PUBLICATION_TITLE")!=null){
						String sql11="select t.DC_PUBLICATION_TITLE from tbl_publication t where t.DN_ID="+pImage.get("DC_PUBLICATION_TITLE").toString();
						publicationTitle=jt.queryForObject(sql11, String.class);
						imagesVM.setDC_PUBLICATION_TITLE(publicationTitle);
					}
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Object d=pImage.get("DD_ISSUE_DATE");
					
					if(d!=null){
						String dateInString1 =d.toString();
						try {
							Date date1 = formatter.parse(dateInString1);
							imagesVM.setDD_ISSUE_DATE(date1);
							imagesVM.setDateissue(formatter.format(date1));
							
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					
					Object d2=pImage.get("DD_CREATED_ON");
					if(d2!=null){
						String dateInString2 =d2.toString();
						try {
							Date date2 = formatter.parse(dateInString2);
							imagesVM.setDD_CREATED_ON(date2);
							imagesVM.setCreateDate(formatter.format(date2));
							
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					if(pImage.get("DC_FIRSTNAME")!=null){
						imagesVM.setCREATED_BY(pImage.get("DC_FIRSTNAME").toString());
					}
					String section=null;
					if(pImage.get("DC_SECTION")!=null){
						String sql22="select t.DC_PUBLICATION_TITLE from tbl_publication t where t.DN_ID="+pImage.get("DC_SECTION").toString();
						section=jt.queryForObject(sql22, String.class);
						imagesVM.setDC_SECTION(section);
					}
					if(pImage.get("DC_SECTION_OTHER")!=null){
						imagesVM.setDC_SECTION_OTHER(pImage.get("DC_SECTION_OTHER").toString());
					}
					
					if(pImage.get("DC_SECTION_SPECIAL_REGIONAL")!=null){
						imagesVM.setDC_SECTION_SPECIAL_REGIONAL(pImage.get("DC_SECTION_SPECIAL_REGIONAL").toString());
					}
					if(pImage.get("DC_SECTION_SPECIAL_TOPIC")!=null){
						imagesVM.setDC_SECTION_SPECIAL_TOPIC(pImage.get("DC_SECTION_SPECIAL_TOPIC").toString());
					}
					if(pImage.get("DC_HEIGHT")!=null){
						imagesVM.setDC_HEIGHT(pImage.get("DC_HEIGHT").toString());
					}
					if(pImage.get("DC_WIDTH")!=null){
						imagesVM.setDC_WIDTH(pImage.get("DC_WIDTH").toString());
					}
					if(pImage.get("DC_PAGE")!=null){
						imagesVM.setDC_PAGE(pImage.get("DC_PAGE").toString());
					}
					
					imagesVM.setImageUrl(url+Long.parseLong(pImage.get("DN_ID").toString()));
					
					String id=pImage.get("DN_ID").toString();
					List<Map<String,Object>> childImageList = new ArrayList<Map<String,Object>>();
					String sql1="select m.DC_IMAGENAME,m.DN_ID from tbl_child_image m where m.DN_PARENT_IMAGE_ID="+id;
					childImageList=jt.queryForList(sql1);
					List<ChildImageVm> arrayList= new ArrayList<ChildImageVm>();
					for (Map<String, Object> map : childImageList) {
						ChildImageVm childVm= new ChildImageVm();
						
						if(map.get("DN_ID")!=null){
							childVm.setDN_ID(Long.valueOf(map.get("DN_ID").toString()));
						}
						if(map.get("DC_IMAGENAME")!=null){
							childVm.setDC_IMAGENAME(map.get("DC_IMAGENAME").toString());
							String thumChildImageName=map.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
							childVm.setChildThumb(thumChildImageName);
							
						}
					
					    arrayList.add(childVm);
					}
					imagesVM.getListVm().addAll(arrayList);
					li.add(imagesVM);
					
				}
			}

		}
		Collections.reverse(li);
		String json = new Gson().toJson(li);
		return li;
	}
	
	
}