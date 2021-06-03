CREATE TABLE TBL_DETAIL(
 	 scode    VARCHAR2(30)        NOT NULL
	,screater VARCHAR2(100)       NULL
	,sdeleter VARCHAR2(100)       NULL
	,supdater VARCHAR2(100)       NULL
	,dcreate  DATETIME            NOT NULL DEFAULT NOW()
	,ddelete  DATETIME            NULL
	,dupdate  DATETIME            NULL
	,sicaactivity      VARCHAR2(30)NULL
	,sspendingcategory VARCHAR2(30)NULL
	,scostcenter       VARCHAR2(30)NULL
	,scodedocument     VARCHAR2(50)NOT NULL
	,sconcept          VARCHAR2(30)NULL
	,sdescription      VARCHAR2(100)NULL
	,nconsumptiontax   NUMBER NULL
	,sentry            VARCHAR2(30)NULL
	,sobservation      VARCHAR2(200)NULL
	,ndiscountrate     NUMBER NULL
	,nivarate          NUMBER NULL
	,nrow              NUMBER NULL
	,sactivityrateica  VARCHAR2(30)NULL
	,sthird            VARCHAR2(30)NULL
	,sconcepttype      VARCHAR2(30)NULL
	,ngrossvalue 	   NUMBER NULL
	,nconsumptionvalue NUMBER NULL
	,ndiscountvalue    NUMBER NULL
	,nivavalue 		   NUMBER NULL
	,nnetworth         NUMBER NULL
);
ALTER TABLE TBL_DETAIL ADD CONSTRAINT KY_DTL_PRIMARY PRIMARY KEY (scode);
ALTER TABLE TBL_DETAIL ADD CONSTRAINT KY_DTL_ARI_FOREING FOREIGN KEY (sactivityrateica) REFERENCES TBL_ICA_ACTIVITY (scode);
ALTER TABLE TBL_DETAIL ADD CONSTRAINT KY_DTL_SC_FOREING FOREIGN KEY (sspendingcategory) REFERENCES TBL_PARAMETER (scode);
ALTER TABLE TBL_DETAIL ADD CONSTRAINT KY_DTL_SV_FOREING FOREIGN KEY (sconcept) REFERENCES TBL_SERVICE (scode);
ALTER TABLE TBL_DETAIL ADD CONSTRAINT KY_DTL_EY_FOREING FOREIGN KEY (sentry)  REFERENCES TBL_ENTRY (scode);
ALTER TABLE TBL_DETAIL ADD CONSTRAINT KY_DTL_TD_FOREING FOREIGN KEY (sthird)  REFERENCES TBL_ENTERPRISE (scode);
ALTER TABLE TBL_DETAIL ADD CONSTRAINT KY_DTL_PM_FOREING FOREIGN KEY (sconcepttype)  REFERENCES TBL_PARAMETER (scode);
