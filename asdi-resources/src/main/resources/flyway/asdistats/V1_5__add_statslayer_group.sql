INSERT INTO oskari_maplayer_group(locale, parentid, selectable, order_number)
VALUES(
    '{"fi":{"name":"Tilastointiyksiköt"}, "sv":{"name":"Statistiska enheter"}, "en":{"name":"Statistical units"}}',
    -1,
    true,
    1000000);

INSERT INTO oskari_maplayer_group_link(maplayerid, groupid, order_number)
VALUES (
    (SELECT id FROM oskari_maplayer WHERE type='statslayer' AND name = 'ne_110m_countries-3575'),
    (SELECT id FROM oskari_maplayer_group WHERE locale LIKE '%"fi":{"name":"Tilastointiyksiköt"}%'),
    1000001
);

INSERT INTO oskari_maplayer_group_link(maplayerid, groupid, order_number)
VALUES (
    (SELECT id FROM oskari_maplayer WHERE type='statslayer' AND name = 'protectedareas-3575'),
    (SELECT id FROM oskari_maplayer_group WHERE locale LIKE '%"fi":{"name":"Tilastointiyksiköt"}%'),
    1000002
);
