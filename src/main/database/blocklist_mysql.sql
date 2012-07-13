INSERT INTO ofVersion (name, version) VALUES ('blocklist', 0);

CREATE TABLE ofBlocklist (
  blockId                BIGINT         NOT NULL AUTO_INCREMENT,
  ownerUsername        VARCHAR(255)    NOT NULL,
  withUsername         VARCHAR(255)    NOT NULL,
  creationDate         VARCHAR(15)          NOT NULL,
  modificationDate     VARCHAR(15)          NOT NULL,
  PRIMARY KEY (blockId),
  INDEX idx_blocklist_ownerUsername (ownerUsername),
  INDEX idx_blocklist_withUsername (withUsername)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

