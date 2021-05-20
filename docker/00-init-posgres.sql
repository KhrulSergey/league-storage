CREATE ROLE docker WITH LOGIN PASSWORD 'docker';
CREATE DATABASE league_id;
CREATE DATABASE league_core;
CREATE DATABASE league_storage;

GRANT ALL PRIVILEGES ON DATABASE league_id TO docker;
GRANT ALL PRIVILEGES ON DATABASE league_core TO docker;
GRANT ALL PRIVILEGES ON DATABASE league_storage TO docker;