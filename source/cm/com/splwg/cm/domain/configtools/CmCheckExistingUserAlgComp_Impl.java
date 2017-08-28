package com.splwg.cm.domain.configtools;




import com.splwg.base.api.businessObject.BusinessObjectDispatcher;
import com.splwg.base.api.businessObject.BusinessObjectInstance;
import com.splwg.base.api.businessObject.BusinessObjectInstanceKey;
import com.splwg.base.api.businessObject.BusinessObjectStatusCode;
import com.splwg.base.api.lookup.BusinessObjectActionLookup;
import com.splwg.base.domain.common.businessObject.BusinessObject;
import com.splwg.base.domain.common.businessObject.BusinessObjectEnterStatusAlgorithmSpot;
import com.splwg.base.domain.common.businessObjectStatusReason.BusinessObjectStatusReason_Id;
import com.splwg.tax.api.lookup.BusinessObjectStatusTransitionConditionLookup;
import com.splwg.base.domain.security.user.User_Id;
import com.splwg.base.domain.security.user.User;
//import com.splwg.cm.api.lookup.CmUserSetupProcUserFlgLookup;
// a new note


/**
 * @author daisenthal
 *
 @AlgorithmComponent ()
 */
public class CmCheckExistingUserAlgComp_Impl
        extends CmCheckExistingUserAlgComp_Gen
        implements BusinessObjectEnterStatusAlgorithmSpot {

    private static final String PATH_LOGIN_ID = "loginId";
 
    private static final String PATH_USER_ID = "userId";
    private static final String UPDATE_STATUS = "READYTOUPDAT";
 
    
    private BusinessObject bo;
    private BusinessObjectInstanceKey boKey;
    private BusinessObjectInstance boInstance;
    private BusinessObjectStatusCode nextStatus;
    private boolean useDefaultNextStatus;
    

    @Override
    public BusinessObjectStatusCode getNextStatus() {
        return nextStatus;
    }

    @Override
    public BusinessObjectStatusTransitionConditionLookup getNextStatusCondition() {
        return null;
    }

    @Override
    public boolean getUseDefaultNextStatus() {
        return useDefaultNextStatus;
    }

    @Override
    public void setBusinessObject(BusinessObject bo) {
        this.bo = bo;
    }

    @Override
    public void setBusinessObjectKey(BusinessObjectInstanceKey boRequest) {
        boKey = boRequest;
    }

    @Override
    public void invoke() {
    	nextStatus = null;
    	useDefaultNextStatus = true;
        boInstance = BusinessObjectDispatcher.read(boKey, false);  
       
		
		String proposedUserId = boInstance.getString(PATH_LOGIN_ID);
        User_Id userId = new User_Id(proposedUserId);
        User user = userId.getEntity();
        if (user != null){
        	boInstance.set(PATH_USER_ID, proposedUserId);
        //	boInstance.set("userType", CmUserSetupProcUserFlgLookup.constants.EXISTS);
        	BusinessObjectDispatcher.fastUpdate(boInstance.getDocument());
        
        	useDefaultNextStatus = false;
        	nextStatus = new BusinessObjectStatusCode(bo.getId(),UPDATE_STATUS, false);
        }

       

    }

	@Override
	public boolean getForcePostProcessing() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BusinessObjectStatusReason_Id getStatusChangeReasonId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAction(BusinessObjectActionLookup arg0) {
		// TODO Auto-generated method stub
		
	}
    
   

}