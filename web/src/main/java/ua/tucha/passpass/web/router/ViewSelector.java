package ua.tucha.passpass.web.router;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ua.tucha.passpass.web.router.model.Route;
import ua.tucha.passpass.web.router.model.ViewRegistry;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;

@Slf4j
@Service
public class ViewSelector {

    // caches
    private static ViewRegistry viewRegistry;
    private static JXPathContext jxPathContext;

    @PostConstruct
    public void init() throws IllegalAccessException, JAXBException, IOException, NoSuchMethodException, InvocationTargetException {
        viewRegistry = (ViewRegistry) JAXBContext
                .newInstance(ViewRegistry.class)
                .createUnmarshaller()
                .unmarshal(new ClassPathResource("routes.xml").getFile());
        jxPathContext = JXPathContext.newContext(viewRegistry);
        log.debug("Unmarshalled ViewRegistry is ready: {}", viewRegistry);
        interpolateParameters(null, viewRegistry);
    }

    private void interpolateParameters(
            Object parentCandidate,
            Object targetCandidate
    ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        final HashMap<String, String> substitutionMap = new HashMap<String, String>();
        final StringSubstitutor sub = new StringSubstitutor(substitutionMap);

        if (parentCandidate != null && parentCandidate.getClass() == Route.class) {
            final Route parent = (Route) parentCandidate;
            substitutionMap.put("parent.name", parent.getName());
            substitutionMap.put("parent.path", parent.getPath());
            substitutionMap.put("parent.view", parent.getView());
        }

        List<Route> childList = new ArrayList<>();

        if (targetCandidate != null) {
            if (targetCandidate.getClass() == Route.class) {
                final Route target = (Route) targetCandidate;
                String[] parameterList = {"name", "path", "view"};
                for (String parameterName : parameterList) {
                    final String getterName = LOWER_UNDERSCORE.to(LOWER_CAMEL, "get_" + parameterName);
                    final String setterName = LOWER_UNDERSCORE.to(LOWER_CAMEL, "set_" + parameterName);
                    final Method getterMethod = Route.class.getMethod(getterName);
                    final Method setterMethod = Route.class.getMethod(setterName, String.class);
                    final String parameterValue = (String) getterMethod.invoke(target);
                    if (parameterValue != null) {
                        final String newParameterValue = sub.replace(parameterValue);
                        setterMethod.invoke(target, newParameterValue);
                        substitutionMap.put(parameterName, newParameterValue);
                    }
                }
                childList = target.getRouteList();
            } else if (targetCandidate.getClass() == ViewRegistry.class) {
                ViewRegistry target = (ViewRegistry) targetCandidate;
                childList = target.getRouteList();
            }
        }

        if (childList != null) {
            for (Route child : childList) {
                interpolateParameters(targetCandidate, child);
            }
        }

    }

    public Route getRouteByName(String name) {
        String xPathExpression = "";
        ArrayList<String> chunkList = new ArrayList<>(Arrays.asList(name.split("/")));
        chunkList.remove(0);
        for (String chunk : chunkList) {
            xPathExpression = xPathExpression + "/routeList[name='" + chunk + "']";
        }
        return (Route) jxPathContext.getValue(xPathExpression);
    }

    public String selectViewByName(String name) {
        return getRouteByName(name).getView();
    }

    public String selectPathByName(String name) {
        return getRouteByName(name).getPath();
    }

}
