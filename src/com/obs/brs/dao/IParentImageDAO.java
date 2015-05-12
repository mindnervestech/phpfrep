package com.obs.brs.dao;

import java.util.List;

import com.obs.brs.model.ParentImage;

/**
 * 
 * @author Jeevanantham
 *
 */
public interface IParentImageDAO {
	/**
	 * Add ParentImage
	 * 
	 * @param  ParentImage parentImage
	 */
	public Long addParentImage(ParentImage parentImage);
	
	/**
	 * Update ParentImage
	 * 
	 * @param  ParentImage parentImage
	 */
	public void updateParentImage(ParentImage parentImage);
	
	/**
	 * Delete ParentImage
	 * 
	 * @param  ParentImage parentImage
	 */
	public void deleteParentImage(ParentImage parentImage);
	
	/**
	 * Get ParentImage
	 * 
	 * @param  int ParentImage Id
	 */
	public ParentImage getParentImageById(long id);
	
	/**
	 * Get ParentImage List
	 * 
	 */
	public List<ParentImage> getParentImage();

	List<ParentImage> getParentImageByFilter(String filter);
}
