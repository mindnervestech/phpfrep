package com.mnt.report.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Lazy(false)
@Component
public class Scheduler {
	

	@Autowired
    private JdbcTemplate jt;
	
	@Value("${fullImagePath}")
	String fullImagePath;
	
	//@Scheduled(fixedRate =90000, initialDelay= 14000)
//	@Scheduled(cron="*/5 * * * * ?")
	
	
	//@Scheduled(fixedDelay =20000)
//	@Scheduled(fixedRate =   90000)
	@Scheduled(fixedRate =   300000)
	public void generateOcr(){
		System.out.println("scheduler run at every 5 minute..... ");

		String sql="select d.DN_ID,d.DN_CHILD_IMAGE_ID,d.DN_PARENT_IMAGE_ID,c.DC_IMAGENAME from tbl_de_data d inner join tbl_child_image c on d.DN_CHILD_IMAGE_ID=c.DN_ID and d.DC_OCR_TEXT IS NULL";
		List<Map<String,Object>> results =  jt.queryForList(sql);
		System.out.println("size o flist is "+results.size());

		int  blankIdRecord=0;
		int recordUpdated=0;
		int clearRecorg=0;
		for (Map<String, Object> map : results) {
			blankIdRecord++;

			if(map.get("DN_CHILD_IMAGE_ID")!=null && map.get("DN_PARENT_IMAGE_ID")!=null && map.get("DC_IMAGENAME")!=null){
				clearRecorg++;

				final Long childImageId=Long.parseLong(map.get("DN_CHILD_IMAGE_ID").toString());
				File newChild = new File(fullImagePath+"/"+"child"+"/"+map.get("DN_PARENT_IMAGE_ID").toString()+"/"+map.get("DN_CHILD_IMAGE_ID").toString()+"/"+map.get("DC_IMAGENAME").toString()); 

				if (newChild.exists()) {
					
					clearRecorg++;
					final String resultOcr ="testing.....";
					try {
						BufferedImage image = ImageIO.read(newChild);
						/*	final String resultOcr =doOCR(newChild);*/
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					
					String sqlUpdate="update tbl_de_data d set d.DC_OCR_TEXT=? where d.DN_CHILD_IMAGE_ID=?";
					//		jt.execute(sqlUpdate);

					Boolean flag=jt.execute(sqlUpdate,new PreparedStatementCallback<Boolean>(){  

						@Override
						public Boolean doInPreparedStatement(java.sql.PreparedStatement ps)
								throws SQLException, DataAccessException {
							ps.setString (1, resultOcr);
							ps.setLong (2, childImageId);
							return ps.execute(); 
						}  
					});

					recordUpdated++;
				}	
			}

		}
		
	//	System.out.println("null row are "+blankIdRecord);
	//	System.out.println("clear record are "+clearRecorg);
		System.out.println("recorg updated are "+recordUpdated);
	}
	

	
}
