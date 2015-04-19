package com.obs.brs.service;

import java.util.List;

import com.obs.brs.model.ChildImage;

/**
 * 
 * @author Jeevanantham
 *
 */
public interface IChildImageService {

	/**
	 * Add ChildImage
	 * 
	 * @param  ChildImage childImage
	 */
	public Long addChildImage(ChildImage childImage);
	
	/**
	 * Update ChildImage
	 * 
	 * @param  ChildImage childImage
	 */
	public void updateChildImage(ChildImage childImage);
	
	/**
	 * Delete ChildImage
	 * 
	 * @param  ChildImage childImage
	 */
	public void deleteChildImage(ChildImage childImage);
	
	/**
	 * Get ChildImage
	 * 
	 * @param  int ChildImage Id
	 */
	public ChildImage getChildImageById(long id);
	
	/**
	 * Get ChildImage List
	 * 
	 */
	public List<ChildImage> getChildImage();
	
	/**
	 * Get ChildImage List from parent id
	 * 
	 */
	public List<ChildImage> getChildImagesByParent(long parentId);
}
