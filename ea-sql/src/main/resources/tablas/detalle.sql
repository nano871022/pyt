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
ALTER TABLE TBL_DETAIL ADD PRIMARY KEY (scode);
ALTER TABLE TBL_DETAIL ADD FOREIGN KEY (sactivityrateica) TBL_ICA_ACTIVITY REFERENCES (scode);
ALTER TABLE TBL_DETAIL ADD FOREIGN KEY (sspendingcategory) TBL_PARAMETER REFERENCES (scode);
ALTER TABLE TBL_DETAIL ADD FOREIGN KEY (sconcept) TBL_SERVICE REFERENCES (scode);
ALTER TABLE TBL_DETAIL ADD FOREIGN KEY (sentry) TBL_ENTRY REFERENCES (scode);
ALTER TABLE TBL_DETAIL ADD FOREIGN KEY (sthird) TBL_THIRD REFERENCES (scode);
ALTER TABLE TBL_DETAIL ADD FOREIGN KEY (sconcepttype) TBL_PARAMETER REFERENCES (scode);
