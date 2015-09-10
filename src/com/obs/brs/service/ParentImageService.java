package com.obs.brs.service;


import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.obs.brs.dao.IParentImageDAO;
import com.obs.brs.model.ParentImage;

/**
 * 
 * @author Jeevanantham
 *
 */
@Transactional(readOnly = true)
public class ParentImageService implements IParentImageService {

	IParentImageDAO parentImageDAO;
	
   /*
    * get parentImageDAO
    */
	public IParentImageDAO getParentImageDAO() {
		return parentImageDAO;
	}
     /*
      * @param parentImageDAO the parentImageDAO to set
      */
	public void setParentImageDAO(IParentImageDAO parentImageDAO) {
		this.parentImageDAO = parentImageDAO;
	}

	/* (non-Javadoc)
	 * @see com.obs.brs.service.ParentImageService#addParentImage(com.obs.brs.model.ParentImage)
	 */
	@Override
	@Transactional(readOnly = false)
	public Long addParentImage(ParentImage parentImage) {
		return getParentImageDAO().addParentImage(parentImage);
	}

	/* (non-Javadoc)
	 * @see com.obs.brs.service.ParentImageService#updateParentImage(com.obs.brs.model.ParentImage)
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateParentImage(ParentImage parentImage) {
		getParentImageDAO().updateParentImage(parentImage);
	}

	/* (non-Javadoc)
	 * @see com.obs.brs.service.ParentImageService#deleteParentImage(com.obs.brs.model.ParentImage)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteParentImage(ParentImage parentImage) {
		getParentImageDAO().deleteParentImage(parentImage);
	}

	/* (non-Javadoc)
	 * @see com.obs.brs.service.ParentImageService#getParentImageById(com.obs.brs.model.ParentImage)
	 */
	@Override
	public ParentImage getParentImageById(long id) {
		return getParentImageDAO().getParentImageById(id);
	}

	/* (non-Javadoc)
	 * @see com.obs.brs.service.ParentImageService#getParentImage(com.obs.brs.model.ParentImage)
	 */
	@Override
	public List<ParentImage> getParentImage() {
		return getParentImageDAO().getParentImage();
	}
	
	@Override
	public List<ParentImage> getParentImageByFilter(String filter){
		return getParentImageDAO().getParentImageByFilter(filter);
	}
	@Override
	public ParentImage getParentImageByName(String name) {
		return getParentImageDAO().getParentImageByName(name);
	}
	


}
