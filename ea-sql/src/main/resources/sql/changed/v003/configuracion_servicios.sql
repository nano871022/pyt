ALTER TABLE TBL_MARKER_SERVICE       MODIFY COLUMN sconfiguration VARCHAR2(30) NULL;
ALTER TABLE TBL_FIELD_SERVICE_SEARCH MODIFY COLUMN sconfiguration VARCHAR2(30) NULL;
ALTER TABLE TBL_MARKER               MODIFY COLUMN sconfiguration VARCHAR2(30) NULL;
UPDATE TBL_MARKER_SERVICE       SET sconfiguration = NULL;
UPDATE TBL_FIELD_SERVICE_SEARCH SET sconfiguration = NULL;
UPDATE TBL_MARKER               SET sconfiguration = NULL;
ALTER TABLE TBL_MARKER_SERVICE       ADD CONSTRAINT KY_MS_FOREIGN  FOREIGN KEY (sconfiguration) REFERENCES TBL_CONFIGURATION(scode);
ALTER TABLE TBL_FIELD_SERVICE_SEARCH ADD CONSTRAINT KY_FSS_FOREIGN FOREIGN KEY (sconfiguration) REFERENCES TBL_CONFIGURATION(scode);
ALTER TABLE TBL_MARKER               ADD CONSTRAINT KY_M_FOREIGN   FOREIGN KEY (sconfiguration) REFERENCES TBL_CONFIGURATION(scode);
UPDATE TBL_MARKER_SERVICE       SET sconfiguration = (SELECT SCODE FROM TBL_CONFIGURATION);
UPDATE TBL_FIELD_SERVICE_SEARCH SET sconfiguration = (SELECT SCODE FROM TBL_CONFIGURATION);
UPDATE TBL_MARKER               SET sconfiguration = (SELECT SCODE FROM TBL_CONFIGURATION);