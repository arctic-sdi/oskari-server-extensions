package flyway.asdistats;

import fi.nls.oskari.map.layer.OskariLayerService;
import fi.nls.oskari.service.OskariComponentManager;
import org.oskari.permissions.model.Resource;
import org.oskari.permissions.model.ResourceType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Add permissions for regionset: protected areas
 */
public class V1_4__add_permission_for_protected_areas_regionset extends V1_2__add_permission_for_regionset {

    // statslayers described as layer resources for permissions handling
    protected List<Resource> getResources() {
        List<Resource> list = OskariComponentManager.getComponentOfType(OskariLayerService.class)
                .findByUrlAndName("resources://regionsets/protectedareas-3575.json", "protectedareas-3575")
                .stream().map(l -> {
                    Resource res = new Resource();
                    res.setType(ResourceType.maplayer);
                    res.setMapping(Integer.toString(l.getId()));
                    return res;
                }).collect(Collectors.toList());
        return list;
    }
}
