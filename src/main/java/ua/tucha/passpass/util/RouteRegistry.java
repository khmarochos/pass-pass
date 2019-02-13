package ua.tucha.passpass.util;

public class RouteRegistry {

    public static class NoteRouteRegistry {
        public static final String FIRST_LEVEL              = "/note";
        public static final String CREATE                   = FIRST_LEVEL + "/create";
        public static final String READ                     = FIRST_LEVEL + "/read";
        public static final String UPDATE                   = FIRST_LEVEL + "/update";
        public static final String DELETE                   = FIRST_LEVEL + "/delete";
    }

    public static class UserRouteRegistry {
        public static final String FIRST_LEVEL              = "/user";
        public static final String CREATE                   = FIRST_LEVEL + "/create";
        public static final String READ                     = FIRST_LEVEL + "/read";
        public static final String UPDATE                   = FIRST_LEVEL + "/update";
        public static final String DELETE                   = FIRST_LEVEL + "/delete";
        public static final String SIGN_UP                  = FIRST_LEVEL + "/sign-up"; // not the same as create
        public static final String SIGN_IN                  = FIRST_LEVEL + "/sign-in";
        public static final String CONFIRM_EMAIL            = FIRST_LEVEL + "/confirm-email";
    }

    public static class FailureRouteRegistry {
        public static final String FIRST_LEVEL              = "/failure";
        public static final String NOT_FOUND                = FIRST_LEVEL + "/404";
        public static final String INTERNAL_SERVER_ERROR    = FIRST_LEVEL + "/500";
        public static final String DEFAULT                  = FIRST_LEVEL + "/default";
    }

}
