package com.obs.brs.service;

import java.util.List;
import java.util.Map;

import com.obs.brs.model.ParentImage;
/**
 * 
 * @author Jeevanantham
 *
 */
public interface IParentImageService {

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
	
	public ParentImage getParentImageByName(String name);
	
	/**
	 * Get ParentImage List
	 * 
	 */
	public List<ParentImage> getParentImage();

	List<ParentImage> getParentImageByFilter(String filter);
	List<ParentImage>getParentImageForSciByFilter(String filter);

	public void updateParentImagesStatus(Map<Long, Boolean> selectedIds);
	public void updateParentImagesStatusToGallery(Map<Long, Boolean> selectedIds);
	public void updateParentImagesStatusLive(Map<Long, Boolean> selectedIds);
	public void updateOcrTextIDDupDetails(String id,Long liveId);
	
}
