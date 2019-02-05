package ua.tucha.passpass.util;

public class RouteRegistry {

    public class NoteRouteRegistry {
        public static final String FIRST_LEVEL = "/note";
        public static final String CREATE      = FIRST_LEVEL + "/create";
        public static final String READ        = FIRST_LEVEL + "/read";
        public static final String UPDATE      = FIRST_LEVEL + "/update";
        public static final String DELETE      = FIRST_LEVEL + "/delete";
    }

    public class UserRouteRegistry {
        public static final String FIRST_LEVEL = "/user";
        public static final String CREATE      = FIRST_LEVEL + "/create";
        public static final String READ        = FIRST_LEVEL + "/read";
        public static final String UPDATE      = FIRST_LEVEL + "/update";
        public static final String DELETE      = FIRST_LEVEL + "/delete";
        public static final String SIGN_UP     = FIRST_LEVEL + "/sign-up"; // not the same as create
        public static final String SIGN_IN     = FIRST_LEVEL + "/sign-in";
    }

}
