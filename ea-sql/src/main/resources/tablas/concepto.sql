CREATE TABLE TBL_CONCEPT(
 	 scode    VARCHAR2(30)        NOT NULL
	,screater VARCHAR2(100)       NULL
	,sdeleter VARCHAR2(100)       NULL
	,supdater VARCHAR2(100)       NULL
	,dcreate  DATETIME            NOT NULL DEFAULT NOW()
	,ddelete  DATETIME            NULL
	,dupdate  DATETIME            NULL
	,sexpenseaccount VARCHAR2(30) NOT NULL
	,sbilltopay  VARCHAR2(30)     NOT NULL
	,sdescription VARCHAR2(30)    NULL
	,senterprise VARCHAR2(30)     NOT NULL
	,sstate VARCHAR2(30)          NOT NULL
	,ssubconcept VARCHAR2(30)     NULL
);
ALTER TABLE TBL_CONCEPT ADD PRIMARY KEY (scode);
ALTER TABLE TBL_CONCEPT ADD FOREIGN KEY (sstate) REFERENCES TBL_PARAMETER(scode);
ALTER TABLE TBL_CONCEPT ADD FOREIGN KEY (senterprise) REFERENCES TBL_ENTERPRISE(scode);
ALTER TABLE TBL_CONCEPT ADD FOREIGN KEY (sexpenseaccount) REFERENCES TBL_ACCOUNTING_ACCOUNT(scode);
ALTER TABLE TBL_CONCEPT ADD FOREIGN KEY (sbilltopay) REFERENCES TBL_ACCOUNTING_ACCOUNT(scode);