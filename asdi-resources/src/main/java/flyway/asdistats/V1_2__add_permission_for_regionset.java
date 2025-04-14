package flyway.asdistats;

import org.oskari.user.Role;
import org.oskari.user.User;
import fi.nls.oskari.map.layer.OskariLayerService;
import fi.nls.oskari.service.OskariComponentManager;
import fi.nls.oskari.service.ServiceException;
import fi.nls.oskari.service.ServiceRuntimeException;
import fi.nls.oskari.service.UserService;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.oskari.permissions.PermissionService;
import org.oskari.permissions.PermissionServiceMybatisImpl;
import org.oskari.permissions.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Add permissions for regionset: countries
 */
public class V1_2__add_permission_for_regionset extends BaseJavaMigration {

    public void migrate(Context context) throws Exception {
        PermissionService service = new PermissionServiceMybatisImpl();
        for(Resource resToUpdate : getResources()) {
            Optional<Resource> dbRes = service.findResource(ResourceType.maplayer, resToUpdate.getMapping());
            if(dbRes.isPresent()) {
                resToUpdate = dbRes.get();
            }
            for(Role role : getRoles()) {
                if(resToUpdate.hasPermission(role, PermissionType.VIEW_LAYER)) {
                    // already had the permission
                    continue;
                }
                final Permission permission = new Permission();
                permission.setType(PermissionType.VIEW_LAYER);
                permission.setRoleId((int) role.getId());
                resToUpdate.addPermission(permission);
            }
            service.saveResource(resToUpdate);
        }
    }

    // statslayers described as layer resources for permissions handling
    protected List<Resource> getResources() {
        List<Resource> list = OskariComponentManager.getComponentOfType(OskariLayerService.class)
                .findByUrlAndName("resources://regionsets/ne_110m_countries-3575.json", "ne_110m_countries-3575")
                .stream().map(l -> {
                    Resource res = new Resource();
                    res.setType(ResourceType.maplayer);
                    res.setMapping(Integer.toString(l.getId()));
                    return res;
                }).collect(Collectors.toList());
        return list;
    }

    private List<Role> getRoles() {
        List<Role> list = new ArrayList<>();
        try {
            // "logged in" user
            list.add(Role.getDefaultUserRole());
            // guest user
            User guest = UserService.getInstance().getGuestUser();
            list.addAll(guest.getRoles());
        } catch (ServiceException ex) {
            throw new ServiceRuntimeException("Unable to get roles");
        }
        return list;
    }
}
