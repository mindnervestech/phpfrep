package com.mnt.report.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.jdbc.core.JdbcTemplate;
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



@Controller
@RequestMapping("/gallery")
public class GalleryController {
	
	DecimalFormat decimalFormat=new DecimalFormat("#.##");
	
	@Autowired
    private JdbcTemplate jt;
	
	@Autowired
	private CommonUtils util;

	@Value("${fullImagePath}")
	String fullImagePath;
	
	
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
			String sql="select * from tbl_parent_image m where m.DN_STATUS=0 ORDER BY m.DN_ID desc";
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
	
	@RequestMapping(value="/save_comment", method=RequestMethod.POST)
	@ResponseBody
	public String save_comment(@RequestBody ParentImageVm idVm){
		System.out.println("in save_comment");
		Long imageId=Long.parseLong(idVm.getId().toString());
		String section=null;
		String comment=idVm.getTitle();
		
		String sql="select t.DC_SECTION from tbl_parent_image t where t.DN_ID="+imageId;
		section=jt.queryForObject(sql, String.class);
		
		Long sectionId=Long.parseLong(section);
		
		String sql1="select t.DC_PUBLICATION_TITLE from tbl_publication t where t.DN_ID="+sectionId;
		String publicationTitle=jt.queryForObject(sql1, String.class);
		System.out.println(publicationTitle);
		
		if(publicationTitle.equals("Special-Topic")){
			String sql2="update tbl_parent_image t set t.DC_SECTION_SPECIAL_TOPIC='"+comment+"' where t.DN_ID="+imageId;
			jt.execute(sql2);
		}else if(publicationTitle.equals("Special-Regional")) {
			String sql3="update tbl_parent_image t set t.DC_SECTION_SPECIAL_REGIONAL="+comment+" where t.DN_ID="+imageId;
			jt.execute(sql3);
		} else {
			String sql4="update tbl_parent_image t set t.DC_SECTION_OTHER='"+comment+"' where t.DN_ID="+imageId;
			jt.execute(sql4);
		}
		
		return "success";
				
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
		
		long childid=util.getMaxId(jt, "tbl_child_image");
		int r = (int) (Math.random() * (1000 - 100)) + 100;
		String fileimageName="crp_"+r+"_"+childid+".png";
		createDir(fullImagePath,cropImageVm.getId(),childid);
		File file = new File(fullImagePath+"/" + "parent" + "/"+cropImageVm.getId()+"/"+imageName);
		File thumbFile = new File(fullImagePath+"/"+"child"+"/"+cropImageVm.getId()+"/"+childid+"/"+fileimageName);
		BufferedImage originalImage = ImageIO.read(file);
		BufferedImage croppedImage = originalImage.getSubimage(x11, y11, w1,h1);
//		Thumbnails.of(croppedImage).size(w, h).toFile(file);
    	Thumbnails.of(croppedImage).size(w1, h1).toFile(thumbFile);
    	
    	String heightCM=decimalFormat.format(((double)h1/96)*2.54*0.9575);
		String widthCM=decimalFormat.format(((double)w1/96)*2.54*0.9575);	
    	
    	
    	String updateSql="update tbl_child_image set DC_IMAGENAME='"+fileimageName+"',DD_CREATED_ON=now(),DC_HEIGHT='"+heightCM+"',DC_WIDTH='"+widthCM+"' where DN_ID="+childid;
    	jt.execute(updateSql);

    	
    	String sqlforjobId="select t.DN_ID from tbl_de_job t where t.DN_PARENT_IMAGE_ID="+cropImageVm.getId()+" limit 1";
    	String jobId=jt.queryForObject(sqlforjobId, String.class);
    	System.out.println("job id id "+jobId);
    	
    	String result=null;
    	try {
    		result = new Ocr().doOCR(thumbFile);
		} catch (Exception e) {
			System.out.println("problane in ocr result");
		}
    	
    //	String hedline=null;
    	
    	String sqlforupdatededata="INSERT INTO tbl_de_data (DC_CURRENCY,DC_OCR_TEXT,DN_CHILD_IMAGE_ID,DN_CREATED_BY,DD_CREATED_ON,DN_PARENT_IMAGE_ID,DE_JOB_ID,DC_LENGTH,DC_WIDTH) VALUES('0','"+result+"','"+childid+"','"+cropImageVm.getLoginUserId()+"',now(),'"+cropImageVm.getId()+"','"+jobId+"','"+heightCM+"','"+widthCM+"')";
    	jt.execute(sqlforupdatededata);
    		
    	CropImageVm cropVm=new CropImageVm();
    	cropVm.setId(cropImageVm.getId());
    	cropVm.setChildId(childid);
    	cropVm.setImageName(fileimageName);

       return cropVm;
       
	}

	@RequestMapping(value="/save_whole_crop_image", method=RequestMethod.POST)
	@ResponseBody
	public CropImageVm SaveWholeCropImage(@RequestBody CropImageVm cropImageVm) throws IOException{
		System.out.println("in SaveWholeCropImage..........,");
		Long imageId=Long.parseLong(cropImageVm.getId().toString());

		String sqlImagName="select DC_IMAGENAME from tbl_parent_image where DN_ID="+imageId;
		String imageName=jt.queryForObject(sqlImagName, String.class);
		  
		String sqlcreate="INSERT INTO tbl_child_image (DN_PARENT_IMAGE_ID,DN_CREATED_BY) VALUES("+imageId+",'"+cropImageVm.getLoginUserId()+"')";
		jt.execute(sqlcreate);
		
		long childid=util.getMaxId(jt, "tbl_child_image");
		
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
    	
    	String heightCM=decimalFormat.format(((double)height/96)*2.54*0.9575);
		String widthCM=decimalFormat.format(((double)width/96)*2.54*0.9575);	
    	
    	
    	
    	
    	String updateSql="update tbl_child_image set DC_IMAGENAME='"+fileimageName+"',DD_CREATED_ON=now(),DC_HEIGHT='"+heightCM+"',DC_WIDTH='"+widthCM+"' where DN_ID="+childid;
		jt.execute(updateSql);
        System.out.println("successfully created");
        
        

    	String sqlforjobId="select t.DN_ID from tbl_de_job t where t.DN_PARENT_IMAGE_ID="+imageId+" limit 1";
    	String jobId=jt.queryForObject(sqlforjobId, String.class);
    	System.out.println("job id id "+jobId);
    	
    	
    	String result=null;
    	
    	try {
    	 result = new Ocr().doOCR(thumbFile);
		} catch (Exception e) {
			System.out.println("problame in ocr result");
		}
    	
    	String sqlforupdatededata="INSERT INTO tbl_de_data (DC_CURRENCY,DC_OCR_TEXT,DN_CHILD_IMAGE_ID,DN_CREATED_BY,DD_CREATED_ON,DN_PARENT_IMAGE_ID,DE_JOB_ID,DC_LENGTH,DC_WIDTH) VALUES('0','"+result+"','"+childid+"','"+cropImageVm.getLoginUserId()+"',now(),'"+imageId+"','"+jobId+"','"+heightCM+"','"+widthCM+"')";
    	jt.execute(sqlforupdatededata);
    	
        
        CropImageVm cropVm=new CropImageVm();
        cropVm.setId(imageId);
        cropVm.setChildId(childid);
        cropVm.setImageName(fileimageName);
        
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
		
		String sqldeteteChild="insert into tbl_deleted_image (DN_ID,DN_IMAGEID,DC_IMAGENAME,DN_DELETED_BY,DD_DELETED_ON,DB_ISCHILD) VALUES('"+id+"','"+id+"','"+imageName+"','"+deletedBy+"',now(),'0')";
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

	@RequestMapping(value="/delete_child_image/{id}/{userId}", method=RequestMethod.POST)
	@ResponseBody
	public void delete_child_image(@PathVariable("id") String id,@PathVariable("userId") String userId){
		
	
		
		System.out.println("in delete child image");
		System.out.println("userId"+userId);
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
		
		String sqldeteteChild="insert into tbl_deleted_image (DN_ID,DN_IMAGEID,DC_IMAGENAME,DN_DELETED_BY,DD_DELETED_ON,DB_ISCHILD) VALUES('"+imageid+"','"+imageid+"','"+imageName+"','"+deletedBy+"',now(),'1')";
		jt.execute(sqldeteteChild);
		
		
		String sql="DELETE from tbl_child_image  where DN_ID="+id;
		jt.execute(sql);
		
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
		}
		System.out.println("record updated");
	}
	
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
	
	
	@RequestMapping(value="/get_filter_image/{id}", method=RequestMethod.GET)
	@ResponseBody
	public List<ImagesVM> getfilterimage(@PathVariable("id") String filter){
		System.out.println("in get filterr images");
		Long filterId=Long.parseLong(filter);
		List<ImagesVM> li= new ArrayList<ImagesVM>();
		String url="/webapp/get-all-parent-image?id=";
		
		int n=0;
		int m=0;
		
		List<Map<String,Object>> parentImageList = new ArrayList<Map<String,Object>>();
		String sql="select m.DN_ID,m.DC_IMAGENAME,m.DD_CREATED_ON,m.DN_CREATED_BY,u.DC_FIRSTNAME,m.DN_DELETED_BY,m.DD_DELETED_ON,m.DB_DELETED,m.DD_ISSUE_DATE,m.DC_PAGE,m.DC_PUBLICATION_TITLE,m.DC_SECTION,m.DC_SECTION_OTHER,m.DC_SECTION_SPECIAL_REGIONAL,m.DC_SECTION_SPECIAL_TOPIC,m.DC_HEIGHT,m.DC_WIDTH from tbl_parent_image m inner join tbl_user u on m.DN_CREATED_BY=u.DN_ID and m.DN_STATUS="+filterId+" ORDER BY m.DN_ID desc";
		parentImageList=jt.queryForList(sql);
		
		List<String> duplicate= new ArrayList<String>();
		Set<String> duplicateNamess = new HashSet<String>();
		
		
		for (Map<String, Object> pImage : parentImageList) {
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
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
			Object d=pImage.get("DD_ISSUE_DATE");
			if(d!=null){
				String dateInString1 =d.toString();
				try {
					Date date1 = formatter.parse(dateInString1);
					imagesVM.setDD_ISSUE_DATE(date1);
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
			
			/*if(pImage.get("DC_SECTION")!=null){
				imagesVM.setDC_SECTION(pImage.get("DC_SECTION").toString());
			}*/
			

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
			String sql1="select m.DC_IMAGENAME,m.DN_ID from tbl_child_image m where m.DN_PARENT_IMAGE_ID="+id+" ORDER BY m.DN_ID desc";
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
				/*if(map.get("DN_PARENT_IMAGE_ID")!=null){
					childVm.setDN_PARENT_IMAGE_ID(Long.parseLong(map.get("DN_PARENT_IMAGE_ID").toString()));
				}
				if(map.get("isCompleted")!=null){
					childVm.setIsCompleted(map.get("isCompleted").toString());
				}
				if(map.get("DC_HEIGHT")!=null){
					childVm.setDC_HEIGHT(map.get("DC_HEIGHT").toString());
				}
				if(map.get("DC_WIDTH")!=null){
					childVm.setDC_WIDTH(map.get("DC_WIDTH").toString());
				}*/
			    arrayList.add(childVm);
			}
			imagesVM.getListVm().addAll(arrayList);
			li.add(imagesVM);
		}
		String json = new Gson().toJson(li);
	//	System.out.println("...."+json);
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
		String sql="select m.DN_ID,m.DC_IMAGENAME,m.DD_CREATED_ON,m.DN_CREATED_BY,u.DC_FIRSTNAME,m.DN_DELETED_BY,m.DD_DELETED_ON,m.DB_DELETED,m.DD_ISSUE_DATE,m.DC_PAGE,m.DC_PUBLICATION_TITLE,m.DC_SECTION,m.DC_SECTION_OTHER,m.DC_SECTION_SPECIAL_REGIONAL,m.DC_SECTION_SPECIAL_TOPIC,m.DC_HEIGHT,m.DC_WIDTH from tbl_parent_image m inner join tbl_user u on m.DN_CREATED_BY=u.DN_ID and m.DN_STATUS=0 ORDER BY m.DN_ID desc";
		parentImageList=jt.queryForList(sql);
		
		List<String> duplicate= new ArrayList<String>();
		Set<String> duplicateNamess = new HashSet<String>();
		
		for (Map<String, Object> pImage : parentImageList) {
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
				/*if(map.get("DN_PARENT_IMAGE_ID")!=null){
					childVm.setDN_PARENT_IMAGE_ID(Long.parseLong(map.get("DN_PARENT_IMAGE_ID").toString()));
				}
				if(map.get("isCompleted")!=null){
					childVm.setIsCompleted(map.get("isCompleted").toString());
				}
				if(map.get("DC_HEIGHT")!=null){
					childVm.setDC_HEIGHT(map.get("DC_HEIGHT").toString());
				}
				if(map.get("DC_WIDTH")!=null){
					childVm.setDC_WIDTH(map.get("DC_WIDTH").toString());
				}*/
			    arrayList.add(childVm);
			}
			imagesVM.getListVm().addAll(arrayList);
			li.add(imagesVM);
		}
		String json = new Gson().toJson(li);
		return li;
	}
}