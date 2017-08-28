package com.splwg.cm.domain.configtools;

import com.ibm.icu.math.BigDecimal;
import com.splwg.base.api.businessObject.BusinessObjectDispatcher;
import com.splwg.base.api.businessObject.BusinessObjectInstance;
import com.splwg.base.api.businessObject.BusinessObjectInstanceKey;
import com.splwg.base.api.businessObject.COTSInstanceList;
import com.splwg.base.api.businessObject.COTSInstanceListNode;
import com.splwg.base.api.businessObject.COTSInstanceNode;
import com.splwg.base.api.datatypes.Date;
import com.splwg.base.api.datatypes.DateTime;
import com.splwg.base.api.datatypes.Money;
import com.splwg.base.api.lookup.BusinessObjectActionLookup;
import com.splwg.base.api.testers.AlgorithmImplementationTestCase;
import com.splwg.base.domain.common.algorithm.Algorithm;
import com.splwg.base.domain.common.algorithm.AlgorithmComponentCache;
import com.splwg.base.domain.common.algorithm.Algorithm_Id;
import com.splwg.base.domain.common.businessObject.BusinessObject_Id;
import com.splwg.base.domain.common.businessObject.BusinessObjectEnterStatusAlgorithmSpot;
import com.splwg.base.domain.common.displayProfile.DisplayProfile_Id;
import com.splwg.base.domain.security.user.User_Id;
import com.splwg.base.domain.security.userGroup.UserGroup_Id;
import com.splwg.base.support.context.ContextHolder;
import com.splwg.base.support.schema.BusinessObjectInfo;
import com.splwg.base.support.schema.BusinessObjectInfoCache;
import com.splwg.shared.common.ApplicationError;
import com.splwg.shared.logging.Logger;
import com.splwg.shared.logging.LoggerFactory;
import com.splwg.base.domain.common.language.Language_Id;
import com.splwg.tax.domain.caseManagement.auditCase.AuditCase_Id;

public class CmCheckExistingUserAlgComp_Test extends AlgorithmImplementationTestCase {
	
	 private static final Logger logger = LoggerFactory.getLogger(CmCheckExistingUserAlgComp.class);

	 private static final Algorithm_Id ALG_CD = new Algorithm_Id("CM-TEST");
	 private static final String PATH_USP_ID = "auditCaseId";
	 
	 @Override
	protected Class getAlgorithmImplementationClass() {
	        return CmCheckExistingUserAlgComp_Impl.class;
	    }
	
	public void testAlgorithm() {

		AuditCase_Id auditCaseId = new AuditCase_Id("51037984585503");
   	 	BusinessObjectInstance boInstance = BusinessObjectInstance.create(auditCaseId.getEntity().getBusinessObject());
   	 
        boInstance.set(PATH_USP_ID, auditCaseId.getIdValue());
        boInstance = BusinessObjectDispatcher.read(boInstance);
        
   
        BusinessObjectInfo boInfo = BusinessObjectInfoCache.getRequiredBusinessObjectInfo(auditCaseId.getEntity()
                .getBusinessObject().getId()
                .getTrimmedValue());
        BusinessObjectInstanceKey boKey = new BusinessObjectInstanceKey(boInfo, boInstance.getDocument());
        
        
        Algorithm algorithm = ALG_CD.getEntity();
        CmCheckExistingUserAlgComp algComp = (CmCheckExistingUserAlgComp) AlgorithmComponentCache
                .getAlgorithmComponent(
                        algorithm.getId(), BusinessObjectEnterStatusAlgorithmSpot.class);
        algComp.setBusinessObject(auditCaseId.getEntity().getBusinessObject());
        algComp.setBusinessObjectKey(boKey);
      

        try {
            algComp.invoke();
            saveChanges();
        } catch (ApplicationError e) {
            fail(e.getMessage());
        }
    }


}
