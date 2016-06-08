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
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
	
   

    

	
	@RequestMapping(value="/create_thumnail_images", method=RequestMethod.GET)
	@ResponseBody
	public void createThumbanail(){
		
		System.out.println("in create thumb images");
		
		String query = "select DN_ID,DC_IMAGENAME,DN_PARENT_IMAGE_ID from tbl_child_image";
		List<Map<String,Object>> results =  jt.queryForList(query);
		System.out.println("size o flist is "+results.size());
		int childCount=0;
		
		int childCountOrg=0;
		if(results.size()>0){
			for (Map<String, Object> map : results) {
				
				String fileName=map.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
				
			//	System.out.println("filename is "+fileName);
				
				File thumbFile = new File(fullImagePath+"/"+"child"+"/"+map.get("DN_PARENT_IMAGE_ID").toString()+"/"+map.get("DN_ID").toString()+"/"+fileName);
				File thumbFileOrg = new File(fullImagePath+"/"+"child"+"/"+map.get("DN_PARENT_IMAGE_ID").toString()+"/"+map.get("DN_ID").toString()+"/"+map.get("DC_IMAGENAME").toString());
				
				System.out.println("path of thumnail file is "+thumbFile.getAbsolutePath());
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
						
							System.out.println("thumnail created");
			        	} catch (IOException e1) {
							System.out.println("problame to create thumnail");
							e1.printStackTrace();
							
						}

			        	
			        }
				 }
		    
			}

		}
			
		System.out.println("child count is "+childCount);
		System.out.println("childCountOrg is "+childCountOrg);
	}
	
	
	
	
	@RequestMapping(value="/create_thumnail_images_parent", method=RequestMethod.GET)
	@ResponseBody
	public void createThumbanailParent(){
		
		System.out.println("in create thumb images parent");
		
		String query = "select DN_ID,DC_IMAGENAME from tbl_parent_image";
		List<Map<String,Object>> results =  jt.queryForList(query);
		System.out.println("size o flist is "+results.size());
		int childCount=0;
		
		int childCountOrg=0;
		if(results.size()>0){
			for (Map<String, Object> map : results) {
				
				String fileName=map.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
				
			//	System.out.println("filename is "+fileName);
				
				File thumbFile = new File(fullImagePath+"/"+"parent"+"/"+map.get("DN_ID").toString()+"/"+fileName);
				File thumbFileOrg = new File(fullImagePath+"/"+"parent"+"/"+map.get("DN_ID").toString()+"/"+map.get("DC_IMAGENAME").toString());
				
				System.out.println("path of thumnail file is "+thumbFile.getAbsolutePath());
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
						
							System.out.println("thumnail created");
			        	} catch (IOException e1) {
							System.out.println("problame to create thumnail");
							e1.printStackTrace();
							
						}

			        	
			        }
				 }
		    
			}

		}
			
		System.out.println("child count is "+childCount);
		System.out.println("childCountOrg is "+childCountOrg);
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
	
	
	
	@RequestMapping(value="/get_publication_title", method=RequestMethod.GET)
	@ResponseBody
	public List getPublicatation(){
		System.out.println("in publication title");
		
		String sql = "select * from tbl_publication where DB_DELETED=0 and DC_PUBLICATION_TYPE=2 ORDER BY DC_PUBLICATION_TITLE ASC";
		List<Map<String,Object>> results =  jt.queryForList(sql);
		
//date chackbox
	
		
		return results;
	}
	
	
	
	
	@RequestMapping(value="/edit_image_detail/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Map getEditImageDetail(@PathVariable("id") String id){
		System.out.println("in save edit image");
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
		System.out.println("in search comment ");
		
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
		System.out.println("in save_comment");
		Long imageId=Long.parseLong(idVm.getId().toString());
		System.out.println("imageId "+imageId);
		String section=null;
		String comment=idVm.getTitle();
		System.out.println("comment is"+comment);
		
		String sql="select t.DC_SECTION from tbl_parent_image t where t.DN_ID="+imageId;
		section=jt.queryForObject(sql, String.class);
		
		Long sectionId=Long.parseLong(section);
		
		String sql1="select t.DC_PUBLICATION_TITLE from tbl_publication t where t.DN_ID="+sectionId;
		String publicationTitle=jt.queryForObject(sql1, String.class);
		System.out.println(publicationTitle);
		
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
		System.out.println("in publication sector");

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
					System.out.println("in duplicate image ");
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
		System.out.println("in save crop image");
		
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
    	
    	System.out.println("befor thumbnail");
    	try {	
			Thumbnails.of(new File(fullImagePath+"/"+"child"+"/"+cropImageVm.getId()+"/"+childid+"/"+fileimageName))
        	.width(200).keepAspectRatio(true)
        	.outputFormat("jpg")
        	.toFile(fullImagePath+"/"+"child"+"/"+cropImageVm.getId()+"/"+childid+"/"+fileimageName.split("\\.")[0]+"_thumb.jpg");
		} catch (IOException e1) {
			System.out.println("problame to create thumnail");
			e1.printStackTrace();
		}
    	System.out.println("after thumbnail");
    	
    	final String heightCM=decimalFormat.format(((double)h1/96)*2.54*0.9575);
		final String widthCM=decimalFormat.format(((double)w1/96)*2.54*0.9575);	
    	
		
		
		File newChild = new File(fullImagePath+"/"+"child"+"/"+cropImageVm.getId()+"/"+childid+"/"+fileimageName); 
		ImageIO.write(croppedImage,"png",newChild );
		System.out.println("before ocr result");
		final String result = null; //doOCR(newChild);
		
		
    //	System.out.println("path is "+newChild.getAbsolutePath());
     //   System.out.println("ocr result is "+result); 
        
		
		
	//	final String result="testing";
		
	//	System.out.println("after ocr result");
		
		
    	String updateSql="update tbl_child_image set DC_IMAGENAME='"+fileimageName+"',DD_CREATED_ON=now(),DC_HEIGHT='"+heightCM+"',DC_WIDTH='"+widthCM+"' where DN_ID="+childid;
    	jt.execute(updateSql);

    	
    	String sqlforjobId="select t.DN_ID from tbl_de_job t where t.DN_PARENT_IMAGE_ID="+cropImageVm.getId()+" limit 1";
    	final String jobId=jt.queryForObject(sqlforjobId, String.class);
    	System.out.println("job id id "+jobId);
    		
    	
    	File chilFile = new File(fullImagePath+"/"+"child"+"/"+cropImageVm.getId()+"/"+childid+"/"+fileimageName);
    //	System.out.println("path is "+chilFile.getAbsolutePath());
    	
    	/*String sqlforupdatededata="INSERT INTO tbl_de_data (DC_CURRENCY,DC_OCR_TEXT,DN_CHILD_IMAGE_ID,DN_CREATED_BY,DD_CREATED_ON,DN_PARENT_IMAGE_ID,DE_JOB_ID,DC_LENGTH,DC_WIDTH) VALUES('0','"+result+"','"+childid+"','"+cropImageVm.getLoginUserId()+"',now(),'"+cropImageVm.getId()+"','"+jobId+"','"+heightCM+"','"+widthCM+"')";
    	jt.execute(sqlforupdatededata);
    	*/
    	
    	
    	Calendar calendar = Calendar.getInstance();
	    final java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
    	String sqlforupdatededata="INSERT INTO tbl_de_data (DC_CURRENCY,DC_OCR_TEXT,DN_CHILD_IMAGE_ID,DN_CREATED_BY,DD_CREATED_ON,DN_PARENT_IMAGE_ID,DE_JOB_ID,DC_LENGTH,DC_WIDTH)  VALUES (?,?,?, ?, ?, ?, ?, ?, ?)";
    	
    	System.out.println("before prepare statement");
    	
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
    	
    	System.out.println("after prepare statement");
    	System.out.println("flag is.."+flag);
    	
    	
    	CropImageVm cropVm=new CropImageVm();

    	cropVm.setDN_ID(childid);
    	cropVm.setDC_IMAGENAME(fileimageName);
    	String thumbimagename=fileimageName.split("\\.")[0]+"_thumb.jpg";
    	cropVm.setChildThumb(thumbimagename);
    	
       return cropVm;
       
	}

	private String doOCR(File thumbFile) {
		  
		System.out.println("in ocr result");
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
		System.out.println("in SaveWholeCropImage..........,");
		final Long imageId=Long.parseLong(cropImageVm.getId().toString());

		String sqlImagName="select DC_IMAGENAME from tbl_parent_image where DN_ID="+imageId;
		String imageName=jt.queryForObject(sqlImagName, String.class);
		  
		String sqlcreate="INSERT INTO tbl_child_image (DN_PARENT_IMAGE_ID,DN_CREATED_BY) VALUES("+imageId+",'"+cropImageVm.getLoginUserId()+"')";
		jt.execute(sqlcreate);
		
		final long childid=util.getMaxId(jt, "tbl_child_image");
		
		int r = (int) (Math.random() * (1000 - 100)) + 100;
		String fileimageName="crp_"+r+"_"+childid+".png";
		createDir(fullImagePath,imageId,childid);
		File file = new File(fullImagePath+"/" + "parent" + "/"+imageId+"/"+imageName);
		File thumbFile = new File(fullImagePath+"/"+"child"+"/"+imageId+"/"+childid+"/"+fileimageName);
		BufferedImage originalImage = ImageIO.read(file);
		int height=originalImage.getHeight();
		int width=originalImage.getWidth();
		
		BufferedImage croppedImage = originalImage.getSubimage(0, 0, width,height);

//		Thumbnails.of(croppedImage).size(w, h).toFile(file);
    	Thumbnails.of(croppedImage).size(width, height).toFile(thumbFile);
    	
    	System.out.println("befor thumbnail");
    	try {	
			Thumbnails.of(new File(fullImagePath+"/"+"child"+"/"+imageId+"/"+childid+"/"+fileimageName))
        	.width(200).keepAspectRatio(true)
        	.outputFormat("jpg")
        	.toFile(fullImagePath+"/"+"child"+"/"+imageId+"/"+childid+"/"+fileimageName.split("\\.")[0]+"_thumb.jpg");
		} catch (IOException e1) {
			System.out.println("problame to create thumnail");
			e1.printStackTrace();
		}
    	
    	
    	
    	
    	final String heightCM=decimalFormat.format(((double)height/96)*2.54*0.9575);
		final String widthCM=decimalFormat.format(((double)width/96)*2.54*0.9575);	
    	
		File newthumbFile = new File(fullImagePath+"/"+"child"+"/"+imageId+"/"+childid+"/"+fileimageName);
		
		
		
		ImageIO.write(croppedImage,"png",newthumbFile );
		
		System.out.println("before ocr result");
		
		final String result = null; //doOCR(newthumbFile);
		
	//	final String result=null;
	
    	String updateSql="update tbl_child_image set DC_IMAGENAME='"+fileimageName+"',DD_CREATED_ON=now(),DC_HEIGHT='"+heightCM+"',DC_WIDTH='"+widthCM+"' where DN_ID="+childid;
		jt.execute(updateSql);
        System.out.println("successfully created");

    	String sqlforjobId="select t.DN_ID from tbl_de_job t where t.DN_PARENT_IMAGE_ID="+imageId+" limit 1";
    	final String jobId=jt.queryForObject(sqlforjobId, String.class);
    	System.out.println("job id id "+jobId);
    	
    	File childImage = new File(fullImagePath+"/"+"child"+"/"+imageId+"/"+childid+"/"+fileimageName);
    	System.out.println("child image path is "+childImage.getAbsolutePath());
    	
    	
    	
    	/*String sqlforupdatededata1="INSERT INTO tbl_de_data (DC_CURRENCY,DC_OCR_TEXT,DN_CHILD_IMAGE_ID,DN_CREATED_BY,DD_CREATED_ON,DN_PARENT_IMAGE_ID,DE_JOB_ID,DC_LENGTH,DC_WIDTH) VALUES('0','"+result+"','"+childid+"','"+cropImageVm.getLoginUserId()+"',now(),'"+imageId+"','"+jobId+"','"+heightCM+"','"+widthCM+"')";
    	jt.execute(sqlforupdatededata1);
    	*/
        
    	Calendar calendar = Calendar.getInstance();
	    final java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
    	String sqlforupdatededata="INSERT INTO tbl_de_data (DC_CURRENCY,DC_OCR_TEXT,DN_CHILD_IMAGE_ID,DN_CREATED_BY,DD_CREATED_ON,DN_PARENT_IMAGE_ID,DE_JOB_ID,DC_LENGTH,DC_WIDTH)  VALUES (?, ?,?, ?, ?, ?, ?, ?, ?)";
    	
    	System.out.println("before prepare statement");
    	
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
    	System.out.println("after prepare statement");
    	System.out.println("flag is.."+flag);
    	
    	
    	CropImageVm cropVm=new CropImageVm();
    	cropVm.setDN_ID(childid);
    	cropVm.setDC_IMAGENAME(fileimageName);
    	String thumbimagename=fileimageName.split("\\.")[0]+"_thumb.jpg";
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
		
		System.out.println("in delete parent image");
		System.out.println("login user is "+userId);
		
		System.out.println("user id is"+id);
		
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
	
	@RequestMapping(value="/get_filter_image_history", method=RequestMethod.POST)
	@ResponseBody
	public List getFilterImageHistory(@RequestBody ImageHistoryVm Ivm) throws ParseException{
	
	//	System.out.println("get_filter_image_history");
	//	System.out.println("date from is"+Ivm.getDateFrom());
	//	System.out.println("date to is "+Ivm.getDateTo());
		
		String[] arrTemp=Ivm.getDateFrom().split("/");
	//	System.out.println("array size is"+arrTemp.length);
		
		String dateFromT=arrTemp[2]+"-"+arrTemp[0]+"-"+arrTemp[1];
		
	//	System.out.println("final date is "+dateFromT);
		
		
		String[] arrTempTo=Ivm.getDateTo().split("/");
	//	System.out.println("array size is"+arrTempTo.length);
		
		String dateToT=arrTempTo[2]+"-"+arrTempTo[0]+"-"+arrTempTo[1];
	//	System.out.println("final date to is "+dateToT);
		

		List<ImageHistoryVm> listImage= new ArrayList<ImageHistoryVm>();
		List<Map<String,Object>> imageHistoryList = new ArrayList<Map<String,Object>>();
		
		String sql="select * from tbl_parent_image t where date(t.DD_CREATED_ON) BETWEEN '"+dateFromT+"' and '"+dateToT+"'";
		
		imageHistoryList=jt.queryForList(sql);
	//	System.out.println("size of list is "+imageHistoryList.size());
		
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
	//	System.out.println("size of image history list is"+listImage);
	
	return listImage;
		
	}
	
	
	@RequestMapping(value="/delete_child_image/{id}/{userId}", method=RequestMethod.POST)
	@ResponseBody
	public void delete_child_image(@PathVariable("id") String id,@PathVariable("userId") String userId){
		
	
		
		System.out.println("in delete child image");
		System.out.println("userId"+userId);
		
		System.out.println("id is "+id);
		String sqlchild="select * from tbl_child_image t where t.DN_ID="+id+" limit 1";
		Map<String,Object> results =  jt.queryForMap(sqlchild);
		
		System.out.println("object id is"+results.get("DN_ID").toString());
		
		
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
		
    //    System.out.println("before insert row");
		String sqldeteteChild="insert into tbl_deleted_image (DN_IMAGEID,DC_IMAGENAME,DN_DELETED_BY,DD_DELETED_ON,DB_ISCHILD) VALUES('"+imageid+"','"+imageName+"','"+deletedBy+"',now(),'1')";
		jt.execute(sqldeteteChild);
		System.out.println("delete row created");
		
		
		String sql="DELETE from tbl_child_image  where DN_ID="+id;
		jt.execute(sql);
		
		System.out.println("deleted from child table ");
		
		String sqldeletededata="delete FROM tbl_de_data  where DN_CHILD_IMAGE_ID="+id;
		jt.execute(sqldeletededata);
		
		
	}
	
	@RequestMapping(value="/move_to_transcription", method=RequestMethod.POST)
	@ResponseBody
	public void moveToTranscription(@RequestBody List<IdVm> idVm){
		
		System.out.println("in move to transcription");
		for(IdVm liveVM : idVm) {
			String sql="update tbl_parent_image m set m.DN_STATUS=2 where m.DN_ID="+liveVM.getId();
			jt.execute(sql);
			
			String sqlforDejob="update tbl_de_job t set t.DN_STATUS=0 where t.DN_PARENT_IMAGE_ID="+liveVM.getId();
			jt.execute(sqlforDejob);
			
		}
		System.out.println("record updated");
	}
	
	@RequestMapping(value="/moveToTranscription_parent/{id}", method=RequestMethod.POST)
	@ResponseBody
	public void moveToTranscriptionParent(@PathVariable("id") String id){
		System.out.println("in move to transcription parent");
		
			String sql="update tbl_parent_image m set m.DN_STATUS=2 where m.DN_ID="+id;
			jt.execute(sql);
			
			String sqlforDejob="update tbl_de_job t set t.DN_STATUS=0 where t.DN_PARENT_IMAGE_ID="+id;
			jt.execute(sqlforDejob);
			
	};
	
	@RequestMapping(value="/move_to_advertorial", method=RequestMethod.POST)
	@ResponseBody
	public void moveToAdvertorial(@RequestBody List<IdVm> idVm){
		
		System.out.println("in move to advertorial");
		for(IdVm liveVM : idVm) {
			String sql="update tbl_parent_image m set m.DN_STATUS=2 where m.DN_ID="+liveVM.getId();
			jt.execute(sql);
			
			
			String sqlForDeJob="update tbl_de_job set DN_STATUS=2 where DN_PARENT_IMAGE_ID="+liveVM.getId();
			jt.execute(sqlForDeJob);
		}
		System.out.println("record updated");
	}
	
	@RequestMapping(value="/update_edited_image", method=RequestMethod.POST)
	@ResponseBody
	public void updateEditedImage(@RequestBody ParentImageVm idVm) throws ParseException{
		
		String json = new Gson().toJson(idVm);
		System.out.println("json is "+json);
		String titlePublication=idVm.getTitle();
		String publicationTitlkaId=null;
		if(idVm.getTitle()!=null){
			System.out.println(titlePublication);
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
		

		System.out.println("in search filter method");
		System.out.println("search text is"+searchTask);
		
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
			
			if(pImage.get("DN_ID")!=null){

				tVm.id=Long.parseLong(pImage.get("DN_ID").toString());
			}
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
			
			li.add(tVm);

		}
		
		
		
		return li;

		
	}
	
	
	@RequestMapping(value="/get_filter_image/{id}", method=RequestMethod.GET)
	@ResponseBody
	public List<ImagesVM> getfilterimage(@PathVariable("id") String filter){
		System.out.println("in get filterr images");
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
					System.out.println("in loop of status one");
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
					//	System.out.println("date in java is "+d.toString());
						String dateInString1 =d.toString();
						try {
							Date date1 = formatter.parse(dateInString1);
							imagesVM.setDD_ISSUE_DATE(date1);
							System.out.println("DD_ISSUE_DATE is "+date1);
							imagesVM.setDateissue(formatter.format(date1));
						//	System.out.println("Dateissue is "+formatter.format(date1));
							
							
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
	//	String json = new Gson().toJson(li);
	//	System.out.println("...."+json);
		return li;
	
	}
	
	
	@RequestMapping(value="/all_image_history", method=RequestMethod.POST)
	@ResponseBody
	public List getAllImageHistory(@RequestBody ImageHistoryVm Ivm) throws ParseException{
	
	//	System.out.println("all_image_history");
	//	System.out.println("date from is"+Ivm.getDateFrom());
	//	System.out.println("date to is "+Ivm.getDateTo());
		
		String[] arrTemp=Ivm.getDateFrom().split("/");
	//	System.out.println("array size is"+arrTemp.length);
		
		String dateFromT=arrTemp[2]+"-"+arrTemp[0]+"-"+arrTemp[1];
		
	//	System.out.println("final date is "+dateFromT);
		
		
		String[] arrTempTo=Ivm.getDateTo().split("/");
	//	System.out.println("array size is"+arrTempTo.length);
		
		String dateToT=arrTempTo[2]+"-"+arrTempTo[0]+"-"+arrTempTo[1];
	//	System.out.println("final date to is "+dateToT);
		

		List<ImageHistoryVm> listImage= new ArrayList<ImageHistoryVm>();
		List<Map<String,Object>> imageHistoryList = new ArrayList<Map<String,Object>>();
		
		String sql="select * from tbl_parent_image t where date(t.DD_CREATED_ON) BETWEEN '"+dateFromT+"' and '"+dateToT+"'";
		imageHistoryList=jt.queryForList(sql);
	//	System.out.println("size of list is "+imageHistoryList.size());
		
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
	//	System.out.println("size of image history list is"+listImage);
	
		return listImage;

	}
	
	
	
	/*@RequestMapping(value="/all_image_history", method=RequestMethod.GET)
	@ResponseBody
	public List getAllImageHistory(){
		
		List<ImageHistoryVm> listImage= new ArrayList<ImageHistoryVm>();
		List<Map<String,Object>> imageHistoryList = new ArrayList<Map<String,Object>>();
		String sql="select m.DN_ID,m.DC_IMAGENAME,m.DD_CREATED_ON,m.DD_ISSUE_DATE from tbl_parent_image m where DATEDIFF(now(),m.DD_CREATED_ON) < 7 ORDER  BY m.DD_CREATED_ON desc";
		imageHistoryList=jt.queryForList(sql);
		System.out.println("size of list is "+imageHistoryList.size());
		
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
		System.out.println("size of image history list is"+listImage);
	
	return listImage;
	}
	
*/	



	@RequestMapping(value="/get_publicatio_images/{month}/{year}", method=RequestMethod.POST)
	@ResponseBody
	public List<PublicationVm> getPublicatioImages(@PathVariable("month") String month,@PathVariable("year") String year){
	
		System.out.println("in getPublicatation title");
		
	//	System.out.println("month is "+month);
	//	System.out.println("yea is r_"+year);
		List<PublicationVm> li= new ArrayList<PublicationVm>();
		
		
		List<Map<String,Object>> imageList = new ArrayList<Map<String,Object>>();
		
		String sqlForTitle="select * from tbl_publication t where t.DC_PUBLICATION_TYPE=2";
		imageList=jt.queryForList(sqlForTitle);
		
		for (Map<String, Object> map : imageList) {
			
			
			List<Map<String,Object>> childImageList = new ArrayList<Map<String,Object>>();
			
			Long titleId=Long.parseLong(map.get("DN_ID").toString());
			
			/*String sqlForChildImages="select c.DN_ID,c.DC_IMAGENAME,c.DN_PARENT_IMAGE_ID from tbl_child_image c "+
			"where c.DN_PARENT_IMAGE_ID in(select p.DN_ID from tbl_parent_image p where MONTH(p.DD_ISSUE_DATE)='"+month+"'"+
			 " and year(p.DD_ISSUE_DATE)='"+year+"' and p.DC_PUBLICATION_TITLE='"+titleId+"')";*/
			
			
			
			String sqlforpublicationChildImages="select c.DN_ID,c.DC_IMAGENAME,c.DN_PARENT_IMAGE_ID, p.DN_STATUS "+
					 "as parrentStatus, j.DN_STATUS as jobStatus "+
					" from tbl_child_image c, tbl_parent_image p, tbl_de_job j"+
					" where "+
					" c.DN_PARENT_IMAGE_ID = p.DN_ID and "+
					" p.DN_ID = j.DN_PARENT_IMAGE_ID and "+
					" MONTH(p.DD_ISSUE_DATE)='"+month+"' and "+
					" year(p.DD_ISSUE_DATE)='"+year+"' and "+
					" p.DC_PUBLICATION_TITLE = '"+titleId+"' ORDER by p.DD_ISSUE_DATE";

			childImageList=jt.queryForList(sqlforpublicationChildImages);
			
			
			
			if(childImageList.size()>0){
				PublicationVm pVm= new PublicationVm();				
				pVm.setPublication(map.get("DC_PUBLICATION_TITLE").toString());
				
				List<ChildPublicationVm> arrayList= new ArrayList<ChildPublicationVm>();
				
				for (Map<String, Object> map2 : childImageList) {
					ChildPublicationVm cVm= new ChildPublicationVm();
					
					cVm.setDN_ID(Long.parseLong(map2.get("DN_ID").toString()));
					cVm.setDN_PARENT_IMAGE_ID(Long.parseLong(map2.get("DN_PARENT_IMAGE_ID").toString()));
					cVm.setDC_IMAGENAME(map2.get("DC_IMAGENAME").toString());
					
					
					String thumChildImageName=map2.get("DC_IMAGENAME").toString().split("\\.")[0]+"_thumb.jpg";
					cVm.setChildThumb(thumChildImageName);
					
					
					if(Integer.parseInt(map2.get("parrentStatus").toString())==0 &&
							Integer.parseInt(map2.get("jobStatus").toString())==0){
						
						cVm.setImageStatus(0);
						cVm.setStatus("Gallery");
						cVm.setColor("red");
						
					}else if(Integer.parseInt(map2.get("parrentStatus").toString())==2 &&
							Integer.parseInt(map2.get("jobStatus").toString())==1){
						
						cVm.setImageStatus(1);
						cVm.setStatus("Live");
						cVm.setColor("green");
						
						
					}else if(Integer.parseInt(map2.get("parrentStatus").toString())==2 &&
							Integer.parseInt(map2.get("jobStatus").toString())==0){
						
						cVm.setImageStatus(2);
						cVm.setStatus("Transcription");
						cVm.setColor("yellow");
						
					}else {
						cVm.setImageStatus(3);
						cVm.setStatus("Advertorial");
						cVm.setColor("blue");
						
					}
					
					arrayList.add(cVm);

				}
				pVm.getListVm().addAll(arrayList);
				li.add(pVm);
			}
			
			
		}
		
	//	String json = new Gson().toJson(li);
	//	System.out.println("json is.. "+json);
		
		return li;
	}
	

	@RequestMapping(value="/update_task", method=RequestMethod.POST)
	@ResponseBody
	public String updateTask(@RequestBody TaskVm taskVm){
		
//		System.out.println("in save task method");
//		System.out.println("name is "+taskVm.name);
//		System.out.println("desc is "+taskVm.desc);
//		System.out.println("status is "+taskVm.status);
//		System.out.println("usertId is "+taskVm.id);
		
		
		String sql="update tbl_task  set DN_NAME='"+taskVm.name+"',DN_DESCRIPTION='"+taskVm.desc+"',DN_STATUS='"+taskVm.status+"' where DN_ID="+taskVm.id;
	   
		jt.execute(sql);
	   
		return "success";
		
	}
	
	@RequestMapping(value="/save_task", method=RequestMethod.POST)
	@ResponseBody
	public String saveTask(@RequestBody TaskVm taskVm){
		
//		System.out.println("in save task method");
//		System.out.println("name is "+taskVm.name);
//		System.out.println("desc is "+taskVm.desc);
//		System.out.println("status is "+taskVm.status);
		
		String sql="insert into tbl_task (DN_NAME,DN_DESCRIPTION,DN_STATUS,DD_CREATED_BY,DD_CREATED_ON) VALUES('"+taskVm.name+"','"+taskVm.desc+"','"+taskVm.status+"','"+taskVm.createdBy+"',CURRENT_DATE())";
	   
		jt.execute(sql);
	   
		return "success";
		
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
			
			if(pImage.get("DN_ID")!=null){

				tVm.id=Long.parseLong(pImage.get("DN_ID").toString());
			}
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
			
			li.add(tVm);

		}
		
		
		
		return li;
	}
	
	@RequestMapping(value="/all_image_list", method=RequestMethod.GET)
	@ResponseBody
	public List getAllImagelist(){
		System.out.println("in all_image_list............");
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
					System.out.println("in loop of status one");
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
					//	System.out.println("date in java is "+d.toString());
						String dateInString1 =d.toString();
						try {
							Date date1 = formatter.parse(dateInString1);
							imagesVM.setDD_ISSUE_DATE(date1);
							System.out.println("DD_ISSUE_DATE is "+date1);
							imagesVM.setDateissue(formatter.format(date1));
						//	System.out.println("Dateissue is "+formatter.format(date1));
							
							
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
		System.out.println("count is "+l);
		Collections.reverse(li);
	//	String json = new Gson().toJson(li);
		return li;
	}
}
