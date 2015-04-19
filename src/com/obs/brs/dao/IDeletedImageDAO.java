package com.obs.brs.dao;

import java.util.List;

import com.obs.brs.model.DeletedImage;

public interface IDeletedImageDAO {
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
