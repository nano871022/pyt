CREATE TABLE TBL_DOCUMENTS(
 	 scode    VARCHAR2(30)       NOT NULL
	,screater VARCHAR2(100)      NULL
	,sdeleter VARCHAR2(100)      NULL
	,supdater VARCHAR2(100)      NULL
	,dcreate  DATETIME           NOT NULL DEFAULT NOW()
	,ddelete  DATETIME           NULL
	,dupdate  DATETIME           NULL
	,scontrollerclass VARCHAR2(100) NOT NULL
	,sdoctype VARCHAR2(30)       NOT NULL
	,nedit    NUMBER             NULL
	,sfieldlable VARCHAR2(50)    NOT NULL
	,sfieldname  VARCHAR2(50)    NOT NULL
	,nnullable   NUMBER          NULL
	,sobjectsearchdto VARCHAR2(100) NULL
	,sputfieldname VARCHAR2(50)  NULL
	,sputnameshow  VARCHAR2(50)  NULL
	,sputnameassign VARCHAR2(50) NULL
	,sselectnamegroup VARCHAR2(100) NULL
);
ALTER TABLE TBL_DOCUMENTS ADD PRIMARY KEY (scode);
ALTER TABLE TBL_DOCUMENTS ADD FOREIGN KEY (sdoctype) REFERENCES TBL_PARAMETER(scode);