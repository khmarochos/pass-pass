package ua.tucha.passpass.web.router;

import javax.xml.bind.Unmarshaller;

@Deprecated
class RouteUnmarshallListener extends Unmarshaller.Listener {

/*
    @Override
    public void afterUnmarshal(Object target, Object parent) {

        StringSubstitutor sub;

        HashMap<String, String> valuesMap = new HashMap<String, String>();

        if(parent != null && parent.getClass() == Route.class) {
            Route parentRoute = (Route) parent;
            valuesMap.put("parent.name", parentRoute.getName());
            valuesMap.put("parent.path", parentRoute.getPath());
            valuesMap.put("parent.view", parentRoute.getView());
        }

        if(target.getClass() == Route.class) {
            // TODO: it would be better to implement it as a foreach loop...
            Route targetRoute = (Route) target;
            String name = targetRoute.getName();
            if(name != null) {
                sub = new StringSubstitutor(valuesMap);
                name = sub.replace(name);
                targetRoute.setName(name);
                valuesMap.put("name", name);
            }
            String path = targetRoute.getPath();
            if(path != null) {
                sub = new StringSubstitutor(valuesMap);
                path = sub.replace(path);
                targetRoute.setPath(path);
                valuesMap.put("path", path);
            }
            String view = targetRoute.getView();
            if(view != null) {
                sub = new StringSubstitutor(valuesMap);
                view = sub.replace(view);
                targetRoute.setView(view);
                valuesMap.put("view", view);
            }
        }

    }
*/

}