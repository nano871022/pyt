CREATE TABLE TBL_DOCUMENT (
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,sbank    VARCHAR2(30)       NULL
	,sdescription VARCHAR2(100)  NULL
	,sstate   VARCHAR2(30)       NOT NULL
	,dannulement DATETIME        NULL
	,dnote     DATETIME          NULL
	,snumbernote NUMBER          NULL
	,dregister DATETIME          NOT NULL
	,smoney   VARCHAR2(30)       NOT NULL
	,senterprise VARCHAR2(30)    NOT NULL
	,sdocumenttype VARCHAR2(30)  NOT NULL
	,svalue   VARCHAR2(20)       NULL   
);
ALTER TABLE TBL_DOCUMENT ADD PRIMARY KEY (scode);
ALTER TABLE TBL_DOCUMENT ADD FOREIGN KEY (sbank) REFERENCES TBL_BANK(scode);
ALTER TABLE TBL_DOCUMENT ADD FOREIGN KEY (senterprise) REFERENCES TBL_ENTERPRISE(scode);
ALTER TABLE TBL_DOCUMENT ADD FOREIGN KEY (smoney) REFERENCES TBL_PARAMETER(scode);
ALTER TABLE TBL_DOCUMENT ADD FOREIGN KEY (sdocumenttype) REFERENCES TBL_PARAMETER(scode);