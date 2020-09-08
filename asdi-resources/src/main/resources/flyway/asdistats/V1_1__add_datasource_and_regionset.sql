INSERT INTO oskari_maplayer(type, url,
                    name, dataprovider_id,
                    locale,
                    attributes, internal, srs_name)
VALUES(
    'statslayer', 'resources://regionsets/ne_110m_countries-3575.json',
    'ne_110m_countries-3575', (SELECT MAX(id) FROM oskari_dataprovider),
    '{ "en" : {
        "name":"Countries"
    }}',
    '{
        "statistics" : {
            "featuresUrl":"resources://regionsets/ne_110m_countries-3575.json",
            "regionIdTag":"ISO_A2",
            "nameIdTag":"NAME"
        }
    }', true, 'EPSG:3575');


-- datasource for "omat indikaattorit"
INSERT INTO oskari_statistical_datasource (locale, config, plugin)
VALUES ('{"fi":{"name":"Omat indikaattorit"},"sv":{"name":"Dina indikatorer"},"en":{"name":"Your indicators"}}',
'{}', 'UserStats');


INSERT INTO 
    oskari_statistical_datasource_regionsets(datasource_id, layer_id, config)
VALUES(
    (SELECT id FROM oskari_statistical_datasource 
        WHERE locale like '%Your indicators%'),
    (SELECT id FROM oskari_maplayer WHERE type='statslayer' AND name = 'ne_110m_countries-3575'),
    '{}');
