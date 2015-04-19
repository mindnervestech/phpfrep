package com.obs.brs.service;
import java.util.List;
import com.obs.brs.model.DeletedImage;
/**
 * 
 * @author rameshbabu_s
 *
 */
public interface IDeletedImageService {
	/**
	 * Add DeletedImage
	 * 
	 * @param  DeletedImage DeletedImage
	 */
	public Long addDeletedImage(DeletedImage deletedImage);
	
	/**
	 * Update DeletedImage
	 * 
	 * @param  DeletedImage DeletedImage
	 */
	public void updateDeletedImage(DeletedImage deletedImage);
	
	/**
	 * Delete DeletedImage
	 * 
	 * @param  DeletedImage DeletedImage
	 */
	public void deleteDeletedImage(DeletedImage deletedImage);
	
	/**
	 * Get DeletedImage
	 * 
	 * @param  int DeletedImage Id
	 */
	public DeletedImage getDeletedImageById(long id);
	
	/**
	 * Get DeletedImage List
	 * 
	 */
	public List<DeletedImage> getDeletedImage();
}
