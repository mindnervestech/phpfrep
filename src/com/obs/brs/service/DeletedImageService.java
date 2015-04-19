package com.obs.brs.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.obs.brs.dao.IDeletedImageDAO;
import com.obs.brs.model.DeletedImage;

/**
 * 
 * @author rameshbabu_s
 *
 */
@Transactional(readOnly = true)
public class DeletedImageService implements IDeletedImageService{

	IDeletedImageDAO deletedImageDAO;
	/*
	 *  get DeletedImageDAO
	 */
	public IDeletedImageDAO getDeletedImageDAO() {
		return deletedImageDAO;
	}
    /*
     * set DeletedImageDAO
     */
	public void setDeletedImageDAO(IDeletedImageDAO deletedImageDAO) {
		this.deletedImageDAO = deletedImageDAO;
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeletedImageService#addDeletedImage(com.obs.brs.model.DeletedImage)
     */
	@Override
	@Transactional(readOnly = false)
	public Long addDeletedImage(DeletedImage deletedImage) {
		// TODO Auto-generated method stub
		return getDeletedImageDAO().addDeletedImage(deletedImage);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeletedImageService#updateDeletedImage(com.obs.brs.model.DeletedImage)
     */
	@Override
	@Transactional(readOnly = false)
	public void updateDeletedImage(DeletedImage deletedImage) {
		// TODO Auto-generated method stub
		getDeletedImageDAO().updateDeletedImage(deletedImage);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeletedImageService#deleteDeletedImage(com.obs.brs.model.DeletedImage)
     */
	@Override
	@Transactional(readOnly = false)
	public void deleteDeletedImage(DeletedImage deletedImage) {
		// TODO Auto-generated method stub
		getDeletedImageDAO().deleteDeletedImage(deletedImage);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeletedImageService#getDeletedImageById(long)
     */
	@Override
	@Transactional(readOnly = false)
	public DeletedImage getDeletedImageById(long id) {
		// TODO Auto-generated method stub
		return getDeletedImageDAO().getDeletedImageById(id);
	}
    /*
     * (non-Javadoc)
     * @see com.obs.brs.service.IDeletedImageService#getDeletedImage()
     */
	@Override
	@Transactional(readOnly = false)
	public List<DeletedImage> getDeletedImage() {
		// TODO Auto-generated method stub
		return getDeletedImageDAO().getDeletedImage();
	}

}
