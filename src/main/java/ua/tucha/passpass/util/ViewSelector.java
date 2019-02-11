package ua.tucha.passpass.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

@Slf4j
@Service
public class ViewSelector {

    // caches
    private static final HashMap<String, HashMap<String, String>>
            routeCategory = new HashMap<>();
    private static final String
            ROUTE_REGISTRY_CLASS_NAME = "ua.tucha.passpass.util.RouteRegistry";

    @PostConstruct
    public void init() throws ClassNotFoundException, IllegalAccessException {
        Class topClassToScan = Class.forName(ROUTE_REGISTRY_CLASS_NAME);
        for (Class subClassToScan : topClassToScan.getDeclaredClasses()) {
            HashMap<String, String> routeView = new HashMap<>();
            Field fields[] = subClassToScan.getDeclaredFields();
            for (Field field : fields) {
                String route = (String) field.get(null);
                routeView.put(route, route);
            }
            log.debug(">>> {}", this.getClass().getName());
            // Java regexps in a literal string look wretched... :-\\
            Pattern p = Pattern.compile("^"
                    + ROUTE_REGISTRY_CLASS_NAME.replace(".", "\\.")
                    + "\\.(\\w+)RouteRegistry$"
            );
            Matcher m = p.matcher(subClassToScan.getCanonicalName());
            if (m.find()) {
                routeCategory.put(m.group(1), routeView);
            }
        }
    }

    public String selectView(String route) {

        // Determine the category depending on the first level ("/user", "/note", etc.)
        Pattern p = Pattern.compile("^/([^/]+)/.+$");
        Matcher m = p.matcher(route);
        if (!m.find()) {
            log.debug("Pattern not matched for route {}", route);
            return null; // TODO: consider throwing an exception
        }
        String category = LOWER_HYPHEN.to(UPPER_CAMEL, m.group(1));

        // Choose the category's registry
        HashMap<String, String> routeView = routeCategory.get(category);
        if (routeView == null) {
            // The registry hasn't been found
            log.debug("Can't find information about route category {}", category);
            return null; // TODO: consider throwing an exception
        }

        // Select the view name
        String viewName = routeView.get(route);
        if (viewName == null) {
            log.debug("Can't find information about route {}", route);
            return null; // TODO: consider throwing an exception
        }
        log.debug("Decided to choose {}", viewName);

        // That's all
        return (viewName);

    }

}
