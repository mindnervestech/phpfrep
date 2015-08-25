package com.obs.brs.managed.bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.obs.brs.model.DeCompany;
import com.obs.brs.service.IDeService;

//@FacesConverter("companyConverter")
@ManagedBean(name="companyConverter")
@RequestScoped
public class CompanyConverter implements Converter {
	@ManagedProperty(value ="#{DeService}")
	IDeService deService;
	public IDeService getDeService() {
		return deService;
	}


	public void setDeService(IDeService deService) {
		this.deService = deService;
	}


	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		System.out.println("value ==  " + value);
        if(value != null && value.trim().length() > 0) {
            try {
            	//DeService service = (DeService) fc.getExternalContext().getApplicationMap().get("DeService");
                return deService.getDeCompanyById(Integer.parseInt(value));
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid company."));
            }
        }
        else {
            return null;
        }
    }
		
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((DeCompany) object).getId());
        }
        else {
            return null;
        }
    }   
}
