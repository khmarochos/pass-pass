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
    private static final String[]
            routeCategories = {"Note", "User"}; // TODO: make it dynamic
    private static HashMap<String, HashMap<String, String>>
            routeCategory = new HashMap<>();

    @PostConstruct
    public void init() throws ClassNotFoundException, IllegalAccessException {
        for (String category : routeCategories) {
            HashMap<String, String> routeView = new HashMap<>();
            Class classToScan = Class.forName("ua.tucha.passpass.util.RouteRegistry$" + category + "RouteRegistry");
            Field fields[] = classToScan.getDeclaredFields();
            for (Field field : fields) {
                String route = (String) field.get(null);
                routeView.put(route, route);
            }
            routeCategory.put(category, routeView);
        }
    }

    public String selectView(String route) {
        Pattern p = Pattern.compile("^/([^/]+)/.+$");
        Matcher m = p.matcher(route);
        if(! m.find()) {
            log.debug("Pattern not matched for route {}", route);
            return null; // TODO: consider throwing an exception
        }
        String category = LOWER_HYPHEN.to(UPPER_CAMEL, m.group(1));
        HashMap<String, String> routeView = routeCategory.get(category);
        if(routeView == null) {
            log.debug("Can't find information about route category {}", category);
            return null; // TODO: consider throwing an exception
        }
        String viewName = routeView.get(route);
        if(viewName == null) {
            log.debug("Can't find information about route {}", route);
            return null; // TODO: consider throwing an exception
        }
        log.debug("Decided to choose {}", viewName);
        return(viewName);
    }

}
