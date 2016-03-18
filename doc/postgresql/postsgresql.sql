CREATE TABLE ACCOUNTS
(
  ID       SERIAL PRIMARY KEY NOT NULL,
  NAME     VARCHAR(40)        NOT NULL,
  FULLNAME VARCHAR(100)       NOT NULL,
  EMAIL    VARCHAR(200)       NOT NULL,
  MODE     VARCHAR(20)        NOT NULL,
  PASSWORD VARCHAR(80),
  ROLE     VARCHAR(16)        NOT NULL
);
CREATE UNIQUE INDEX ACCOUNTS_UQ_NAME ON ACCOUNTS (NAME);

CREATE TABLE ACCOUNT_GROUPS
(
  ID          SERIAL PRIMARY KEY NOT NULL,
  NAME        VARCHAR(40)        NOT NULL,
  DESCRIPTION VARCHAR(300)
);
CREATE UNIQUE INDEX ACCOUNT_GROUPS_UQ_NAME ON ACCOUNT_GROUPS (NAME);

CREATE TABLE ACCOUNT_GROUP_LINK
(
  ACCOUNT      INTEGER NOT NULL,
  ACCOUNTGROUP INTEGER NOT NULL,
  CONSTRAINT ACCOUNT_GROUP_LINK_PK PRIMARY KEY (ACCOUNT, ACCOUNTGROUP),
  CONSTRAINT ACCOUNT_GROUP_LINK_FK_ACCOUNT FOREIGN KEY (ACCOUNT) REFERENCES ACCOUNTS (ID) ON DELETE CASCADE,
  CONSTRAINT ACCOUNT_GROUP_LINK_FK_ACCOUNTGROUP FOREIGN KEY (ACCOUNTGROUP) REFERENCES ACCOUNT_GROUPS (ID) ON DELETE CASCADE
);
CREATE INDEX ACCOUNT_GROUP_LINK_FK_ACCOUNTGROUP ON ACCOUNT_GROUP_LINK (ACCOUNTGROUP);
CREATE INDEX ACCOUNT_GROUP_LINK_FK_ACCOUNT ON ACCOUNT_GROUP_LINK (ACCOUNT);

CREATE TABLE ACCOUNT_GROUP_MAPPING
(
  ID      SERIAL PRIMARY KEY NOT NULL,
  MAPPING VARCHAR(20)        NOT NULL,
  SOURCE  VARCHAR(200)       NOT NULL,
  GROUPID INTEGER            NOT NULL,
  CONSTRAINT ACCOUNT_GROUP_MAPPING_ACCOUNT_GROUPS FOREIGN KEY (GROUPID) REFERENCES ACCOUNT_GROUPS (ID) ON DELETE CASCADE
);
CREATE UNIQUE INDEX ACCOUNT_GROUP_MAPPING_UQ ON ACCOUNT_GROUP_MAPPING (MAPPING, SOURCE);
CREATE INDEX ACCOUNT_GROUP_MAPPING_ACCOUNT_GROUPS ON ACCOUNT_GROUP_MAPPING (GROUPID);

CREATE TABLE PROJECTS
(
  ID          SERIAL PRIMARY KEY    NOT NULL,
  NAME        VARCHAR(40)           NOT NULL,
  DESCRIPTION VARCHAR(500),
  DISABLED    BOOLEAN DEFAULT FALSE NOT NULL
);
CREATE UNIQUE INDEX PROJECTS_UQ_INDEX_9 ON PROJECTS (NAME);

CREATE TABLE BRANCHES
(
  ID          SERIAL PRIMARY KEY    NOT NULL,
  PROJECTID   INTEGER               NOT NULL,
  NAME        VARCHAR(120)          NOT NULL,
  DESCRIPTION VARCHAR(500),
  DISABLED    BOOLEAN DEFAULT FALSE NOT NULL,
  CONSTRAINT BRANCHES_FK_PROJECTS FOREIGN KEY (PROJECTID) REFERENCES PROJECTS (ID) ON DELETE CASCADE
);

CREATE UNIQUE INDEX BRANCHES_UQ ON BRANCHES (PROJECTID, NAME);
CREATE INDEX BRANCHES_FK_PROJECTS ON BRANCHES (PROJECTID);

CREATE TABLE BRANCH_TEMPLATE_DEFINITIONS
(
  BRANCHID                    INTEGER PRIMARY KEY NOT NULL,
  ABSENCEPOLICY               VARCHAR(20)         NOT NULL,
  SYNCINTERVAL                INTEGER             NOT NULL,
  SYNCHRONISATIONSOURCEID     VARCHAR(20)         NOT NULL,
  SYNCHRONISATIONSOURCECONFIG VARCHAR(2000),
  CONSTRAINT BRANCH_TEMPLATE_DEFINITIONS_FK_BRANCH FOREIGN KEY (BRANCHID) REFERENCES BRANCHES (ID) ON DELETE CASCADE
);

CREATE TABLE BRANCH_TEMPLATE_DEFINITION_PARAMS
(
  BRANCHID    INTEGER      NOT NULL,
  NAME        VARCHAR(120) NOT NULL,
  DESCRIPTION VARCHAR(500),
  EXPRESSION  VARCHAR(500),
  CONSTRAINT BRANCH_TEMPLATE_DEFINITION_PARAMS_PK PRIMARY KEY (BRANCHID, NAME),
  CONSTRAINT BRANCH_TEMPLATE_DEFINITION_PARAMS_FK_BRANCH_TEMPLATE_DEFINITION FOREIGN KEY (BRANCHID) REFERENCES BRANCH_TEMPLATE_DEFINITIONS (BRANCHID) ON DELETE CASCADE
);
CREATE INDEX BRANCH_TEMPLATE_DEFINITION_PARAMS_FK_BRANCH_TEMPLATE_DEFINITION ON BRANCH_TEMPLATE_DEFINITION_PARAMS (BRANCHID);

CREATE TABLE BRANCH_TEMPLATE_INSTANCES
(
  BRANCHID         INTEGER PRIMARY KEY NOT NULL,
  TEMPLATEBRANCHID INTEGER             NOT NULL,
  CONSTRAINT BRANCH_TEMPLATE_INSTANCES_FK_BRANCH FOREIGN KEY (BRANCHID) REFERENCES BRANCHES (ID) ON DELETE CASCADE,
  CONSTRAINT BRANCH_TEMPLATE_INSTANCES_FK_TEMPLATEBRANCH FOREIGN KEY (TEMPLATEBRANCHID) REFERENCES BRANCHES (ID) ON DELETE CASCADE
);
CREATE INDEX BRANCH_TEMPLATE_INSTANCES_FK_TEMPLATEBRANCH ON BRANCH_TEMPLATE_INSTANCES (TEMPLATEBRANCHID);

CREATE TABLE BRANCH_TEMPLATE_INSTANCE_PARAMS
(
  BRANCHID INTEGER      NOT NULL,
  NAME     VARCHAR(120) NOT NULL,
  VALUE    VARCHAR(500) NOT NULL,
  CONSTRAINT BRANCH_TEMPLATE_INSTANCE_PARAMS_PK PRIMARY KEY (BRANCHID, NAME),
  CONSTRAINT BRANCH_TEMPLATE_INSTANCE_PARAMS_FK_BRANCH_TEMPLATE_INSTANCE FOREIGN KEY (BRANCHID) REFERENCES BRANCH_TEMPLATE_INSTANCES (BRANCHID) ON DELETE CASCADE
);
CREATE INDEX BRANCH_TEMPLATE_INSTANCE_PARAMS_FK_BRANCH_TEMPLATE_INSTANCE ON BRANCH_TEMPLATE_INSTANCE_PARAMS (BRANCHID);

CREATE TABLE BUILDS
(
  ID          SERIAL PRIMARY KEY NOT NULL,
  BRANCHID    INTEGER            NOT NULL,
  NAME        VARCHAR(150)       NOT NULL,
  DESCRIPTION VARCHAR(500),
  CREATION    VARCHAR(24)        NOT NULL,
  CREATOR     VARCHAR(40)        NOT NULL,
  CONSTRAINT BUILDS_FK_BRANCH FOREIGN KEY (BRANCHID) REFERENCES BRANCHES (ID) ON DELETE CASCADE
);
CREATE UNIQUE INDEX BUILDS_UQ ON BUILDS (BRANCHID, NAME);
CREATE INDEX BUILDS_FK_BRANCH ON BUILDS (BRANCHID);

CREATE TABLE BUILD_FILTERS
(
  ACCOUNTID INTEGER       NOT NULL,
  BRANCHID  INTEGER       NOT NULL,
  NAME      VARCHAR(120)  NOT NULL,
  TYPE      VARCHAR(150)  NOT NULL,
  -- TODO JSON
  DATA      VARCHAR(2000) NOT NULL,
  CONSTRAINT BUILD_FILTERS_PK PRIMARY KEY (ACCOUNTID, BRANCHID, NAME),
  CONSTRAINT BUILD_FILTERS_FK_ACCOUNT FOREIGN KEY (ACCOUNTID) REFERENCES ACCOUNTS (ID) ON DELETE CASCADE,
  CONSTRAINT BUILD_FILTERS_FK_BRANCH FOREIGN KEY (BRANCHID) REFERENCES BRANCHES (ID) ON DELETE CASCADE
);
CREATE INDEX BUILD_FILTERS_FK_ACCOUNT ON BUILD_FILTERS (ACCOUNTID);
CREATE INDEX BUILD_FILTERS_FK_BRANCH ON BUILD_FILTERS (BRANCHID);

CREATE TABLE CONFIGURATIONS
(
  ID      SERIAL PRIMARY KEY NOT NULL,
  TYPE    VARCHAR(150)       NOT NULL,
  NAME    VARCHAR(150)       NOT NULL,
  -- TODO JSON
  CONTENT VARCHAR(2000)      NOT NULL
);
CREATE UNIQUE INDEX CONFIGURATIONS_UQ ON CONFIGURATIONS (TYPE, NAME);

CREATE TABLE GLOBAL_AUTHORIZATIONS
(
  ACCOUNT INTEGER     NOT NULL,
  ROLE    VARCHAR(80) NOT NULL,
  CONSTRAINT GLOBAL_AUTHORIZATIONS_PK PRIMARY KEY (ACCOUNT, ROLE),
  CONSTRAINT GLOBAL_AUTHORIZATIONS_FK_ACCOUNT FOREIGN KEY (ACCOUNT) REFERENCES ACCOUNTS (ID) ON DELETE CASCADE
);
CREATE INDEX GLOBAL_AUTHORIZATIONS_FK_ACCOUNT ON GLOBAL_AUTHORIZATIONS (ACCOUNT);

CREATE TABLE GROUP_GLOBAL_AUTHORIZATIONS
(
  ACCOUNTGROUP INTEGER     NOT NULL,
  ROLE         VARCHAR(80) NOT NULL,
  CONSTRAINT GROUP_GLOBAL_AUTHORIZATIONS_PK PRIMARY KEY (ACCOUNTGROUP, ROLE),
  CONSTRAINT GROUP_GLOBAL_AUTHORIZATIONS_FK_ACCOUNT FOREIGN KEY (ACCOUNTGROUP) REFERENCES ACCOUNT_GROUPS (ID) ON DELETE CASCADE
);
CREATE INDEX GROUP_GLOBAL_AUTHORIZATIONS_FK_ACCOUNT ON GROUP_GLOBAL_AUTHORIZATIONS (ACCOUNTGROUP);

CREATE TABLE GROUP_PROJECT_AUTHORIZATIONS
(
  ACCOUNTGROUP INTEGER     NOT NULL,
  PROJECT      INTEGER     NOT NULL,
  ROLE         VARCHAR(80) NOT NULL,
  CONSTRAINT GROUP_PROJECT_AUTHORIZATIONS_PK PRIMARY KEY (ACCOUNTGROUP, PROJECT, ROLE),
  CONSTRAINT GROUP_PROJECT_AUTHORIZATIONS_FK_ACCOUNT FOREIGN KEY (ACCOUNTGROUP) REFERENCES ACCOUNT_GROUPS (ID) ON DELETE CASCADE,
  CONSTRAINT GROUP_PROJECT_AUTHORIZATIONS_FK_PROJECT FOREIGN KEY (PROJECT) REFERENCES PROJECTS (ID) ON DELETE CASCADE
);
CREATE INDEX GROUP_PROJECT_AUTHORIZATIONS_FK_ACCOUNT ON GROUP_PROJECT_AUTHORIZATIONS (ACCOUNTGROUP);
CREATE INDEX GROUP_PROJECT_AUTHORIZATIONS_FK_PROJECT ON GROUP_PROJECT_AUTHORIZATIONS (PROJECT);

-- TODO Liquibase or Flyway
CREATE TABLE ONTRACK_VERSION
(
  VALUE   INTEGER   NOT NULL,
  UPDATED TIMESTAMP NOT NULL
);

CREATE TABLE PREDEFINED_PROMOTION_LEVELS
(
  ID          SERIAL PRIMARY KEY NOT NULL,
  ORDERNB     INTEGER            NOT NULL,
  NAME        VARCHAR(40)        NOT NULL,
  DESCRIPTION VARCHAR(500),
  IMAGETYPE   VARCHAR(40),
  -- TODO BLOB
  IMAGEBYTES  BLOB
);
CREATE UNIQUE INDEX PREDEFINED_PROMOTION_LEVELS_UQ ON PREDEFINED_PROMOTION_LEVELS (NAME);

CREATE TABLE PREDEFINED_VALIDATION_STAMPS
(
  ID          SERIAL PRIMARY KEY NOT NULL,
  NAME        VARCHAR(40)        NOT NULL,
  DESCRIPTION VARCHAR(500)       NOT NULL,
  IMAGETYPE   VARCHAR(40),
  -- TODO BLOB
  IMAGEBYTES  BLOB
);
CREATE UNIQUE INDEX PREDEFINED_VALIDATION_STAMPS_UQ ON PREDEFINED_VALIDATION_STAMPS (NAME);

CREATE TABLE PREFERENCES
(
  ACCOUNTID INTEGER       NOT NULL,
  TYPE      VARCHAR(150)  NOT NULL,
  CONTENT   VARCHAR(2000) NOT NULL,
  CONSTRAINT PREFERENCES_PK PRIMARY KEY (ACCOUNTID, TYPE),
  CONSTRAINT PREFERENCES_FK_ACCOUNT FOREIGN KEY (ACCOUNTID) REFERENCES ACCOUNTS (ID) ON DELETE CASCADE
);
CREATE INDEX PREFERENCES_FK_ACCOUNT ON PREFERENCES (ACCOUNTID);

CREATE TABLE PROJECT_AUTHORIZATIONS
(
  ACCOUNT INTEGER     NOT NULL,
  PROJECT INTEGER     NOT NULL,
  ROLE    VARCHAR(80) NOT NULL,
  CONSTRAINT PROJECT_AUTHORIZATIONS_PK PRIMARY KEY (ACCOUNT, PROJECT, ROLE),
  CONSTRAINT PROJECT_AUTHORIZATIONS_FK_ACCOUNT FOREIGN KEY (ACCOUNT) REFERENCES ACCOUNTS (ID) ON DELETE CASCADE,
  CONSTRAINT PROJECT_AUTHORIZATIONS_FK_PROJECT FOREIGN KEY (PROJECT) REFERENCES PROJECTS (ID) ON DELETE CASCADE
);
CREATE INDEX PROJECT_AUTHORIZATIONS_FK_ACCOUNT ON PROJECT_AUTHORIZATIONS (ACCOUNT);
CREATE INDEX PROJECT_AUTHORIZATIONS_FK_PROJECT ON PROJECT_AUTHORIZATIONS (PROJECT);

CREATE TABLE PROMOTION_LEVELS
(
  ID          SERIAL PRIMARY KEY NOT NULL,
  BRANCHID    INTEGER            NOT NULL,
  ORDERNB     INTEGER            NOT NULL,
  NAME        VARCHAR(40)        NOT NULL,
  DESCRIPTION VARCHAR(500),
  IMAGETYPE   VARCHAR(40),
  -- TODO BLOB
  -- IMAGEBYTES BINARY (32000),
  IMAGEBYTES  BLOB,
  CONSTRAINT PROMOTION_LEVELS_FK_BRANCH FOREIGN KEY (BRANCHID) REFERENCES BRANCHES (ID) ON DELETE CASCADE
);
CREATE UNIQUE INDEX PROMOTION_LEVELS_UQ ON PROMOTION_LEVELS (BRANCHID, NAME);
CREATE INDEX PROMOTION_LEVELS_FK_BRANCH ON PROMOTION_LEVELS (BRANCHID);

CREATE TABLE PROMOTION_RUNS
(
  ID               SERIAL PRIMARY KEY NOT NULL,
  BUILDID          INTEGER            NOT NULL,
  PROMOTIONLEVELID INTEGER            NOT NULL,
  CREATION         VARCHAR(24)        NOT NULL,
  CREATOR          VARCHAR(40)        NOT NULL,
  DESCRIPTION      VARCHAR(500),
  CONSTRAINT PROMOTIONS_RUNS_FK_BUILD FOREIGN KEY (BUILDID) REFERENCES BUILDS (ID) ON DELETE CASCADE,
  CONSTRAINT PROMOTIONS_RUNS_FK_PROMOTION_LEVEL FOREIGN KEY (PROMOTIONLEVELID) REFERENCES PROMOTION_LEVELS (ID) ON DELETE CASCADE
);
CREATE INDEX PROMOTIONS_RUNS_FK_BUILD ON PROMOTION_RUNS (BUILDID);
CREATE INDEX PROMOTIONS_RUNS_FK_PROMOTION_LEVEL ON PROMOTION_RUNS (PROMOTIONLEVELID);

CREATE TABLE SETTINGS
(
  CATEGORY VARCHAR(200)  NOT NULL,
  NAME     VARCHAR(150)  NOT NULL,
  -- TODO JSON
  VALUE    VARCHAR(2000) NOT NULL,
  CONSTRAINT SETTINGS_PK PRIMARY KEY (CATEGORY, NAME)
);

CREATE TABLE SHARED_BUILD_FILTERS
(
  BRANCHID INTEGER       NOT NULL,
  NAME     VARCHAR(120)  NOT NULL,
  TYPE     VARCHAR(150)  NOT NULL,
  DATA     VARCHAR(2000) NOT NULL,
  CONSTRAINT SHARED_BUILD_FILTERS_PK PRIMARY KEY (BRANCHID, NAME),
  CONSTRAINT SHARED_BUILD_FILTERS_FK_BRANCH FOREIGN KEY (BRANCHID) REFERENCES BRANCHES (ID) ON DELETE CASCADE
);
CREATE INDEX SHARED_BUILD_FILTERS_FK_BRANCH ON SHARED_BUILD_FILTERS (BRANCHID);

CREATE TABLE VALIDATION_STAMPS
(
  ID              SERIAL PRIMARY KEY NOT NULL,
  BRANCHID        INTEGER            NOT NULL,
  ORDERNB         INTEGER            NOT NULL,
  NAME            VARCHAR(40)        NOT NULL,
  OWNER           INTEGER,
  PROMOTION_LEVEL INTEGER,
  DESCRIPTION     VARCHAR(500),
  IMAGETYPE       VARCHAR(40),
  -- TODO BLOB
  -- IMAGEBYTES BINARY (32000),
  IMAGEBYTES      BLOB,
  CONSTRAINT VALIDATION_STAMPS_FK_OWNER FOREIGN KEY (OWNER) REFERENCES ACCOUNTS (ID) ON DELETE SET NULL,
  CONSTRAINT VALIDATION_STAMPS_FK_BRANCH FOREIGN KEY (BRANCHID) REFERENCES BRANCHES (ID) ON DELETE CASCADE,
  CONSTRAINT VALIDATION_STAMPS_FK_PROMOTION_LEVEL FOREIGN KEY (PROMOTION_LEVEL) REFERENCES PROMOTION_LEVELS (ID) ON DELETE SET NULL
);
CREATE UNIQUE INDEX VALIDATION_STAMPS_UQ ON VALIDATION_STAMPS (BRANCHID, NAME);
CREATE INDEX VALIDATION_STAMPS_FK_BRANCH ON VALIDATION_STAMPS (BRANCHID);
CREATE INDEX VALIDATION_STAMPS_FK_OWNER ON VALIDATION_STAMPS (OWNER);
CREATE INDEX VALIDATION_STAMPS_FK_PROMOTION_LEVEL ON VALIDATION_STAMPS (PROMOTION_LEVEL);

CREATE TABLE VALIDATION_RUNS
(
  ID                SERIAL PRIMARY KEY NOT NULL,
  BUILDID           INTEGER            NOT NULL,
  VALIDATIONSTAMPID INTEGER            NOT NULL,
  CONSTRAINT VALIDATION_RUNS_FK_BUILD FOREIGN KEY (BUILDID) REFERENCES BUILDS (ID) ON DELETE CASCADE,
  CONSTRAINT VALIDATION_RUNS_FK_VALIDATION_STAMP FOREIGN KEY (VALIDATIONSTAMPID) REFERENCES VALIDATION_STAMPS (ID) ON DELETE CASCADE
);
CREATE INDEX VALIDATION_RUNS_FK_BUILD ON VALIDATION_RUNS (BUILDID);
CREATE INDEX VALIDATION_RUNS_FK_VALIDATION_STAMP ON VALIDATION_RUNS (VALIDATIONSTAMPID);

CREATE TABLE VALIDATION_RUN_STATUSES
(
  ID                    SERIAL PRIMARY KEY NOT NULL,
  VALIDATIONRUNID       INTEGER            NOT NULL,
  VALIDATIONRUNSTATUSID VARCHAR(40)        NOT NULL,
  CREATION              VARCHAR(24)        NOT NULL,
  CREATOR               VARCHAR(40)        NOT NULL,
  DESCRIPTION           VARCHAR(500),
  CONSTRAINT VALIDATION_RUN_STATUSES_FK_VALIDATIONRUNID FOREIGN KEY (VALIDATIONRUNID) REFERENCES VALIDATION_RUNS (ID) ON DELETE CASCADE
);
CREATE INDEX VALIDATION_RUN_STATUSES_FK_VALIDATIONRUNID ON VALIDATION_RUN_STATUSES (VALIDATIONRUNID);


CREATE TABLE ENTITY_DATA
(
  ID               SERIAL PRIMARY KEY NOT NULL,
  NAME             VARCHAR(150)       NOT NULL,
  PROJECT          INTEGER,
  BRANCH           INTEGER,
  PROMOTION_LEVEL  INTEGER,
  VALIDATION_STAMP INTEGER,
  BUILD            INTEGER,
  PROMOTION_RUN    INTEGER,
  VALIDATION_RUN   INTEGER,
  -- TODO JSON
  VALUE            VARCHAR(10000)     NOT NULL,
  CONSTRAINT ENTITY_DATA_FK_BRANCH FOREIGN KEY (BRANCH) REFERENCES BRANCHES (ID) ON DELETE CASCADE,
  CONSTRAINT ENTITY_DATA_FK_BUILD FOREIGN KEY (BUILD) REFERENCES BUILDS (ID) ON DELETE CASCADE,
  CONSTRAINT ENTITY_DATA_FK_PROJECT FOREIGN KEY (PROJECT) REFERENCES PROJECTS (ID) ON DELETE CASCADE,
  CONSTRAINT ENTITY_DATA_FK_PROMOTION_LEVEL FOREIGN KEY (PROMOTION_LEVEL) REFERENCES PROMOTION_LEVELS (ID) ON DELETE CASCADE,
  CONSTRAINT ENTITY_DATA_FK_PROMOTION_RUN FOREIGN KEY (PROMOTION_RUN) REFERENCES PROMOTION_RUNS (ID) ON DELETE CASCADE,
  CONSTRAINT ENTITY_DATA_FK_VALIDATION_RUN FOREIGN KEY (VALIDATION_RUN) REFERENCES VALIDATION_RUNS (ID) ON DELETE CASCADE,
  CONSTRAINT ENTITY_DATA_FK_VALIDATION_STAMP FOREIGN KEY (VALIDATION_STAMP) REFERENCES VALIDATION_STAMPS (ID) ON DELETE CASCADE
);
CREATE INDEX ENTITY_DATA_FK_BRANCH ON ENTITY_DATA (BRANCH);
CREATE INDEX ENTITY_DATA_FK_BUILD ON ENTITY_DATA (BUILD);
CREATE INDEX ENTITY_DATA_FK_PROJECT ON ENTITY_DATA (PROJECT);
CREATE INDEX ENTITY_DATA_FK_PROMOTION_LEVEL ON ENTITY_DATA (PROMOTION_LEVEL);
CREATE INDEX ENTITY_DATA_FK_PROMOTION_RUN ON ENTITY_DATA (PROMOTION_RUN);
CREATE INDEX ENTITY_DATA_FK_VALIDATION_RUN ON ENTITY_DATA (VALIDATION_RUN);
CREATE INDEX ENTITY_DATA_FK_VALIDATION_STAMP ON ENTITY_DATA (VALIDATION_STAMP);

CREATE TABLE EVENTS
(
  ID               SERIAL PRIMARY KEY NOT NULL,
  EVENT_TYPE       VARCHAR(120)       NOT NULL,
  PROJECT          INTEGER,
  BRANCH           INTEGER,
  PROMOTION_LEVEL  INTEGER,
  VALIDATION_STAMP INTEGER,
  BUILD            INTEGER,
  PROMOTION_RUN    INTEGER,
  VALIDATION_RUN   INTEGER,
  REF              VARCHAR(20),
  EVENT_VALUES     VARCHAR(500)       NOT NULL,
  EVENT_TIME       VARCHAR(24)        NOT NULL,
  EVENT_USER       VARCHAR(40)        NOT NULL,
  CONSTRAINT EVENTS_FK_BRANCH FOREIGN KEY (BRANCH) REFERENCES BRANCHES (ID) ON DELETE CASCADE,
  CONSTRAINT EVENTS_FK_BUILD FOREIGN KEY (BUILD) REFERENCES BUILDS (ID) ON DELETE CASCADE,
  CONSTRAINT EVENTS_FK_PROJECT FOREIGN KEY (PROJECT) REFERENCES PROJECTS (ID) ON DELETE CASCADE,
  CONSTRAINT EVENTS_FK_PROMOTION_LEVEL FOREIGN KEY (PROMOTION_LEVEL) REFERENCES PROMOTION_LEVELS (ID) ON DELETE CASCADE,
  CONSTRAINT EVENTS_FK_PROMOTION_RUN FOREIGN KEY (PROMOTION_RUN) REFERENCES PROMOTION_RUNS (ID) ON DELETE CASCADE,
  CONSTRAINT EVENTS_FK_VALIDATION_RUN FOREIGN KEY (VALIDATION_RUN) REFERENCES VALIDATION_RUNS (ID) ON DELETE CASCADE,
  CONSTRAINT EVENTS_FK_VALIDATION_STAMP FOREIGN KEY (VALIDATION_STAMP) REFERENCES VALIDATION_STAMPS (ID) ON DELETE CASCADE
);
CREATE INDEX EVENTS_FK_BRANCH ON EVENTS (BRANCH);
CREATE INDEX EVENTS_FK_BUILD ON EVENTS (BUILD);
CREATE INDEX EVENTS_FK_PROJECT ON EVENTS (PROJECT);
CREATE INDEX EVENTS_FK_PROMOTION_LEVEL ON EVENTS (PROMOTION_LEVEL);
CREATE INDEX EVENTS_FK_PROMOTION_RUN ON EVENTS (PROMOTION_RUN);
CREATE INDEX EVENTS_FK_VALIDATION_RUN ON EVENTS (VALIDATION_RUN);
CREATE INDEX EVENTS_FK_VALIDATION_STAMP ON EVENTS (VALIDATION_STAMP);

CREATE TABLE PROPERTIES
(
  ID               SERIAL PRIMARY KEY NOT NULL,
  TYPE             VARCHAR(150)       NOT NULL,
  PROJECT          INTEGER,
  BRANCH           INTEGER,
  PROMOTION_LEVEL  INTEGER,
  VALIDATION_STAMP INTEGER,
  BUILD            INTEGER,
  PROMOTION_RUN    INTEGER,
  VALIDATION_RUN   INTEGER,
  SEARCHKEY        VARCHAR(200),
  JSON             VARCHAR(2000),
  CONSTRAINT PROPERTIES_FK_BRANCH FOREIGN KEY (BRANCH) REFERENCES BRANCHES (ID) ON DELETE CASCADE,
  CONSTRAINT PROPERTIES_FK_BUILD FOREIGN KEY (BUILD) REFERENCES BUILDS (ID) ON DELETE CASCADE,
  CONSTRAINT PROPERTIES_FK_PROJECT FOREIGN KEY (PROJECT) REFERENCES PROJECTS (ID) ON DELETE CASCADE,
  CONSTRAINT PROPERTIES_FK_PROMOTION_LEVEL FOREIGN KEY (PROMOTION_LEVEL) REFERENCES PROMOTION_LEVELS (ID) ON DELETE CASCADE,
  CONSTRAINT PROPERTIES_FK_PROMOTION_RUN FOREIGN KEY (PROMOTION_RUN) REFERENCES PROMOTION_RUNS (ID) ON DELETE CASCADE,
  CONSTRAINT PROPERTIES_FK_VALIDATION_RUN FOREIGN KEY (VALIDATION_RUN) REFERENCES VALIDATION_RUNS (ID) ON DELETE CASCADE,
  CONSTRAINT PROPERTIES_FK_VALIDATION_STAMP FOREIGN KEY (VALIDATION_STAMP) REFERENCES VALIDATION_STAMPS (ID) ON DELETE CASCADE
);
CREATE INDEX PROPERTIES_FK_BRANCH ON PROPERTIES (BRANCH);
CREATE INDEX PROPERTIES_FK_BUILD ON PROPERTIES (BUILD);
CREATE INDEX PROPERTIES_FK_PROJECT ON PROPERTIES (PROJECT);
CREATE INDEX PROPERTIES_FK_PROMOTION_LEVEL ON PROPERTIES (PROMOTION_LEVEL);
CREATE INDEX PROPERTIES_FK_PROMOTION_RUN ON PROPERTIES (PROMOTION_RUN);
CREATE INDEX PROPERTIES_FK_VALIDATION_RUN ON PROPERTIES (VALIDATION_RUN);
CREATE INDEX PROPERTIES_FK_VALIDATION_STAMP ON PROPERTIES (VALIDATION_STAMP);
CREATE INDEX PROPERTIES_IX_TYPE ON PROPERTIES (TYPE);
