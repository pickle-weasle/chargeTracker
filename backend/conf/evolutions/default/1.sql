# --- !Ups

CREATE TABLE stations (
    id              VARCHAR(50) PRIMARY KEY ,
    name            VARCHAR(255),
    address         VARCHAR(255),
    charging_speed  DOUBLE NOT NULL
);

CREATE TABLE sessions (
    id              VARCHAR(50) PRIMARY KEY,
    station_id      VARCHAR(50) NOT NULL REFERENCES stations(id),
    start_time      TIMESTAMP NOT NULL,
    peak_rate       DECIMAL(10,4) NOT NULL,
    off_peak_rate   DECIMAL(10,4) NOT NULL,
    charging_speed  DOUBLE NOT NULL,
    end_time        TIMESTAMP,
    cost            DECIMAL(10,2)
);

INSERT INTO stations (id, name, address, charging_speed) VALUES
  ('1',  'Dublin Airport',              'Terminal 2, Dublin',                         150.0),
  ('2',  'IFSC',                        'International Financial Services Centre, Dublin 1', 22.0),
  ('3',  'Dundrum Town Centre',         'Sandyford Road, Dundrum, Dublin 16',         50.0),
  ('4',  'Liffey Valley',               'Fonthill Road, Dublin 22',                   50.0),
  ('5',  'The Square Tallaght',         'Tallaght, Dublin 24',                        22.0),
  ('6',  'Blanchardstown Centre',       'Blanchardstown, Dublin 15',                  50.0),
  ('7',  'Grand Canal Dock',            'Grand Canal Dock, Dublin 2',                 7.4),
  ('8',  'Phoenix Park Visitor Centre', 'Phoenix Park, Dublin 8',                     22.0),
  ('9',  'Sandyford Business District', 'Sandyford, Dublin 18',                       150.0),
  ('10', 'Croke Park',                  'Jones Road, Dublin 3',                    50.0);

# --- !Downs

DROP TABLE sessions;
DROP TABLE stations;