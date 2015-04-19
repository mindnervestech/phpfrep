package com.obs.brs.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.obs.brs.dao.IChildImageDAO;
import com.obs.brs.model.ChildImage;

/**
 * 
 * @author Jeevanantham
 *
 */
@Transactional(readOnly = true)
public class ChildImageService implements IChildImageService{

	IChildImageDAO childImageDAO;
	
	/*
	 * @return the childImageDAO
	 */
	public IChildImageDAO getChildImageDAO() {
		return childImageDAO;
	}
	/*
	 * @param childImageDAO the childImageDAO to set
	 */
	public void setChildImageDAO(IChildImageDAO childImageDAO) {
		this.childImageDAO = childImageDAO;
	}

	/* (non-Javadoc)
	 * @see com.obs.brs.service.ChildImageService#addChildImage(com.obs.brs.model.ChildImage)
	 */
	@Override
	@Transactional(readOnly = false)
	public Long addChildImage(ChildImage childImage) {
		return getChildImageDAO().addChildImage(childImage);
	}

	/* (non-Javadoc)
	 * @see com.obs.brs.service.ChildImageService#updateChildImage(com.obs.brs.model.ChildImage)
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateChildImage(ChildImage childImage) {
		getChildImageDAO().updateChildImage(childImage);
	}

	/* (non-Javadoc)
	 * @see com.obs.brs.service.ChildImageService#deleteChildImage(com.obs.brs.model.ChildImage)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteChildImage(ChildImage childImage) {
		getChildImageDAO().deleteChildImage(childImage);
	}

	/* (non-Javadoc)
	 * @see com.obs.brs.service.ChildImageService#getChildImageById(com.obs.brs.model.ChildImage)
	 */
	@Override
	public ChildImage getChildImageById(long id) {
		return getChildImageDAO().getChildImageById(id);
	}

	/* (non-Javadoc)
	 * @see com.obs.brs.service.ChildImageService#getChildImage(com.obs.brs.model.ChildImage)
	 */
	@Override
	public List<ChildImage> getChildImage() {
		return getChildImageDAO().getChildImage();
	}
	
	/* (non-Javadoc)
	 * @see com.obs.brs.service.ChildImageService#getChildImage(com.obs.brs.model.ChildImage)
	 */
	@Override
	public List<ChildImage> getChildImagesByParent(long parentId) {
		return getChildImageDAO().getChildImagesByParent(parentId);
	}
	

}
