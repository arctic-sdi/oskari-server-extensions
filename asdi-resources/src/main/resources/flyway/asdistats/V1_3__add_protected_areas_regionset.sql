INSERT INTO oskari_maplayer(type, url,
                    name, dataprovider_id,
                    locale,
                    attributes, internal, srs_name)
VALUES(
    'statslayer', 'resources://regionsets/protectedareas-3575.json',
    'protectedareas-3575', (SELECT MAX(id) FROM oskari_dataprovider),
    '{ "en" : {
        "name":"Protected Areas"
    }}',
    '{
        "statistics" : {
            "featuresUrl":"resources://regionsets/protectedareas-3575.json",
            "regionIdTag":"OBJECTID",
            "nameIdTag":"NAME"
        }
    }', true, 'EPSG:3575');


INSERT INTO 
    oskari_statistical_datasource_regionsets(datasource_id, layer_id, config)
VALUES(
    (SELECT id FROM oskari_statistical_datasource 
        WHERE locale like '%Your indicators%'),
    (SELECT id FROM oskari_maplayer WHERE type='statslayer' AND name = 'protectedareas-3575'),
    '{}');
